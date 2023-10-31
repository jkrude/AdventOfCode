package y2021

import common.readFileLines
import kotlin.math.pow

class Day3 {

    companion object {
        fun partOne(lines: List<String>): Int {
            lines.first().indices
                .map { idx -> lines.groupBy { it[idx] }.maxByOrNull { (_, v) -> v.size }?.key ?: throw Exception() }
                .joinToString(separator = "")
                .toInt(radix = 2)
                .let { return it * ((2f.pow(lines.first().length) - 1).toInt() - it) }
        }

        // Life support = oxygen * co2 rating
        fun partTwo(input: List<String>): Int {

            fun filter(data: List<String>, byMajority: Boolean): Int {
                var filtered = data
                for (i in data.first().indices) {
                    val (ones, zeros) = filtered.partition { it[i] == '1' }
                    filtered = when {
                        ones.size == zeros.size -> if (byMajority) ones else zeros
                        (ones.size < zeros.size) xor byMajority -> ones // conditional inversion
                        else -> zeros
                    }
                    if (filtered.size == 1) break
                }
                return filtered.first().toInt(radix = 2)
            }

            return filter(input, true) * filter(input, false)
        }
    }
}

fun main() {

    println(Day3.partTwo(readFileLines(3)))
}