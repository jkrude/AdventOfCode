package y2021

import common.readFileLines

class Day1 {

    companion object {

        fun readReport(fileLines: List<String> = readFileLines(1, 2021)): List<Int> =
            fileLines.map { it.toInt() }

        fun partOne(depths: List<Int>): Int {
            return depths.windowed(2)
                .count { (a, b) -> a < b }
        }

        fun partTwo(depths: List<Int>): Int {
            return depths
                .windowed(4)
                .count { it[0] < it[3] } //x_i + x_{i + 1} + x_{i+2} < x_{i+1} + x_{i+2} + x_{i+3} === x_i < x_{i+3}
        }
    }
}

fun main() {
    println(Day1.partTwo(Day1.readReport()))
}