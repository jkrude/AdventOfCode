import kotlin.math.abs
import kotlin.math.ceil

class Day7 {

    companion object {

        fun readReport(fileLines: List<String> = readFileLines(7)): List<Int> =
            fileLines[0].split(",").map { it.toInt() }

        // cost optimum (minima)  = median
        fun partOne(crabPositions: List<Int>): Int =
            crabPositions.sumOf { abs(it - crabPositions.sorted()[crabPositions.size / 2]) }

        // cost optimum (minima)  = ceiled average
        fun partTwo(crabPositions: List<Int>): Int = crabPositions
            .map { abs(it - ceil(crabPositions.average())) }
            .sumOf { it * (it + 1) / 2 }.toInt()
    }
}

fun main() {
    println("Part 1: Cost: ${Day7.partOne(Day7.readReport())}")
    println("Part 2: Cost: ${Day7.partTwo(Day7.readReport())}")
}