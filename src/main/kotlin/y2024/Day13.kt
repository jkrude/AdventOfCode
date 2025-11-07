package y2024

import com.microsoft.z3.Status
import common.*
import common.extensions.takePair

object Day13 {

    val button = """Button [AB]: X\+(\d+), Y\+(\d+)""".toRegex()
    val prize = """Prize: X=(\d+), Y=(\d+)""".toRegex()

    private data class ButtonPrize(val a: Pair<Long, Long>, val b: Pair<Long, Long>, val prize: Pair<Long, Long>)

    private fun parse(lines: List<String>, offset: Long): ButtonPrize {
        val buttonA = button.find(lines[0])!!.groupValues.drop(1).map { it.toLong() }.takePair()
        val buttonB = button.find(lines[1])!!.groupValues.drop(1).map { it.toLong() }.takePair()
        val prize = prize.find(lines[2])!!.groupValues.drop(1).map { it.toLong() + offset }.takePair()
        return ButtonPrize(buttonA, buttonB, prize)

    }

    private fun parseText(input: String, offset: Long = 0L) = input.split("\n\n").map { parse(it.lines(), offset) }


    private fun winOrNull(buttonPrize: ButtonPrize, max: Int?): Long? {

        val (a, b, prize) = buttonPrize

        with(Kontext()) {
            val optim = mkOptimize()
            val i = mkIntConst("I")
            optim.add(i ge 0)
            if (max != null) optim.add(i le 100)

            val j = mkIntConst("J")
            optim.add(j ge 0)
            if (max != null) optim.add(j le max)

            optim.add((i * a.first + j * b.first) eq prize.first)
            optim.add((i * a.second + j * b.second) eq prize.second)

            val target = i + j

            val handle = optim.minimize(target)
            val result: Status = optim.check()
            if (result != Status.SATISFIABLE) return null
            return optim.model.eval(i, false).toLong() * 3 + optim.model.eval(j, false).toLong()
        }
    }

    fun partOne(lines: String): Long {
        val buttonPrizes: List<ButtonPrize> = parseText(lines)
        return buttonPrizes.mapNotNull { winOrNull(it, 100) }.sum()

    }

    fun partTwo(lines: String): Long {
        val buttonPrizes: List<ButtonPrize> = parseText(lines, 10000000000000L)
        return buttonPrizes.mapNotNull { winOrNull(it, null) }.sum()

    }
}

fun main() {
    val input = readFileText(13, 2024)
    println(Day13.partOne(input))
    println(Day13.partTwo(input))
}