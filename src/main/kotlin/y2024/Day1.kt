package y2024

import arrow.core.unzip
import common.extensions.map
import common.extensions.takePair
import common.readFileLines
import kotlin.math.abs

object Day1 {
    private fun parse(lines: List<String>) = lines
        .map { it.split("   ")
            .map { s -> s.toInt() }
            .takePair() }
        .unzip()
    fun partOne(lines: List<String>): Int {
        val (left, right) = parse(lines).map { it.sorted() }
        return left.zip(right).sumOf { (f, s) -> abs(f - s) }
    }

    fun partTwo(lines: List<String>): Int {
        val (left, right) = parse(lines)
        val counts = right.groupBy { it }.mapValues { (_, list) -> list.size }
        return left.sumOf { it * counts.getOrDefault(it,0) }
    }
}

fun main() {
    val input = readFileLines(1, 2024)
    println(Day1.partOne(input))
    println(Day1.partTwo(input))
}