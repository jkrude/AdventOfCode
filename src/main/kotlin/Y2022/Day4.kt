package Y2022

import readFileLines

class Day4 {
    companion object {
        fun doEnclose(firstRange: IntRange, secondRange: IntRange) =
            (firstRange.contains(secondRange.first) && firstRange.contains(secondRange.last))
                    || secondRange.contains(firstRange.first) && secondRange.contains(firstRange.last)

        fun format(line: String): Pair<IntRange, IntRange> {
            val (first, second) = line
                .split(",")
                .map { range ->
                    val (a, b) =
                        range.split("-")
                            .map { it.toInt() }
                    a..b
                }
            return first to second

        }

        fun partOne(lines: List<String>): Int =
            lines.count {
                val (firstRange, secondRange) = format(it)
                doEnclose(firstRange, secondRange)
            }

        fun partTwo(lines: List<String>): Int =
            lines.count() {
                val (firstRange, secondRange) = format(it)
                firstRange.contains(secondRange.first)
                        || firstRange.contains(secondRange.last)
                        || secondRange.contains(firstRange.first)
                        || secondRange.contains(firstRange.last)
            }
    }
}

fun main() {
    println(Day4.partOne(readFileLines(4)))
    println(Day4.partTwo(readFileLines(4)))
}