package y2020

import com.microsoft.z3.IntExpr
import com.microsoft.z3.Status
import common.Kontext
import common.math.crt
import common.readFileLines

object Day13 {

    fun parseSchedule(text: List<String>): Pair<Int, List<Int>> {
        return text.first().toInt() to text[1].split(",").mapNotNull { it.toIntOrNull() }
    }

    fun partOne(lines: List<String>): Int {
        val (earliestDeparture, schedule) = parseSchedule(lines)
        val (id, timeBetween) = schedule.map {
            val x = (earliestDeparture / it)
            val earliestArrival = if (x * it == earliestDeparture) x * it else (x + 1) * it
            it to (earliestArrival - earliestDeparture)
        }.minBy {
            it.second
        }
        return id * timeBetween
    }

    fun partTwoZ3(lines: List<String>): Long {

        val schedule: List<IndexedValue<Int>> =
            lines[1].split(",")
                .mapIndexedNotNull { index, busId ->
                    if (busId == "x") null else IndexedValue(index, busId.toInt())
                }

        with(Kontext()) {
            val timeStamp: IntExpr = mkIntConst("t")
            val N = mkIntConst("N")

            var t = 0L
            var mult: Long = schedule.first().value.toLong()

            for ((offset, busId) in schedule.drop(1)) {
                val solver = mkSolver()
                solver.add(timeStamp gt 0)
                solver.add((timeStamp plus offset) % busId eq 0)
                solver.add(timeStamp eq t + N * mult)
                check(solver.check() == Status.SATISFIABLE)
                t = solver.model.eval(timeStamp, false).toLongOrNull()
                    ?: error("Could not evaluate $timeStamp")
                mult *= busId
            }
            return t
        }
    }


    /**
     * We search the smallest t s.t.
     *  t % x1 == 0
     *  t % x2 == 1
     *  ...
     *  t % xn = n
     *  (only for xi != x)
     *
     *  Which can be rewritten as
     *  t = x1 - 0 mod x1
     *  t = x2 - 1 mod x2
     *  ...
     *  t = xn - n mod xn
     *
     *  Which we can solve using the chinese remainder theorem.
     */
    fun partTwo(lines: List<String>): Long {
        val ids: List<String> = lines[1].split(",")

        val coefficient2Mod = ids.withIndex()
            .mapNotNull { it.value.toIntOrNull()?.let { x -> it.index to x } }
            .map { (index, x) -> (x - index) to x }
        return crt(coefficient2Mod)

    }
}


fun main() {
    println(Day13.partOne(readFileLines(13, 2020)))
    println(Day13.partTwoZ3(readFileLines(13, 2020)))
}