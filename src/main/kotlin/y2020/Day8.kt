package y2020

import common.readFileLines

object Day8 {

    private fun parseInt(instruction: String): Int {
        val nbr = instruction.split(" ").last()
        return if (nbr.first() == '+') nbr.drop(1).toInt()
        else -nbr.drop(1).toInt()
    }


    fun partOne(lines: List<String>): Int {

        val visitedLines: MutableSet<Int> = mutableSetOf()

        var accumulator = 0
        var currentLine = 0
        while (currentLine <= lines.lastIndex) {
            with(lines[currentLine]) {
                when {
                    this.startsWith("nop") -> currentLine++
                    this.startsWith("acc") -> accumulator += parseInt(this)
                        .also { currentLine++ }

                    this.startsWith("jmp") -> currentLine += parseInt(this)
                }
                if (currentLine in visitedLines) {
                    return accumulator
                }
                visitedLines.add(currentLine)
            }
        }
        return accumulator
    }

    private fun nextLine(instruction: String): Int {
        return when {
            instruction.startsWith("nop") -> 1
            instruction.startsWith("acc") -> 1
            instruction.startsWith("jmp") -> parseInt(instruction)
            else -> throw IllegalArgumentException(instruction)
        }
    }


    private fun nextCycleOrNull(nextPositions: List<Int>, start: Int): Int? {
        val visited = mutableSetOf(start)
        var current = start
        while (current <= nextPositions.lastIndex) {
            nextPositions[current].let { next ->
                if (next in visited) return current
                current = next
            }
            visited.add(current)
        }
        return null
    }

    // Replace the line at changeIndex by switching nop to jmp or vise versa
    private fun List<String>.flipNopAndJmp(changeIndex: Int) =
        this.mapIndexed { index, line ->
            if (index != changeIndex) line
            else {
                if (line.startsWith("nop")) line.replace("nop", "jmp")
                else line.replace("jmp", "nop")
            }
        }

    private fun getNextPositions(lines: List<String>): List<Int> {
        return lines.mapIndexed { index, instruction ->
            index + nextLine(instruction)
        }
    }

    fun partTwo(lines: List<String>): Int {
        // For each line index store the index of the next instruction
        val nextPositions = getNextPositions(lines)
        var current = 0
        do {
            val cycleIndex = nextCycleOrNull(nextPositions, current) ?: break
            if (lines[current].startsWith("nop") || lines[current].startsWith("jmp")) {
                val newLines = lines.flipNopAndJmp(current)
                if (nextCycleOrNull(getNextPositions(newLines), 0) == null) return partOne(newLines)
            }
            current = cycleIndex
        } while (current <= lines.lastIndex)
        return partOne(lines)
    }
}

fun main() {
    println(Day8.partOne(readFileLines(8, 2020)))
    println(Day8.partTwo(readFileLines(8, 2020)))
}