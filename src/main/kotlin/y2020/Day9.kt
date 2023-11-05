package y2020

import common.readFileLines

object Day9 {

    private fun List<Long>.sumOf2(start: Int, length: Int, target: Long): Boolean {
        for (i in start..(start + length)) {
            for (j in (i + 1)..(start + length)) {
                if (this[i] + this[j] == target) return true
            }
        }
        return false
    }

    fun partOne(lines: List<String>, preamble: Int = 25): Long {
        return lines.map {
            it.toLong()
        }.let {
            it.withIndex()
                .drop(preamble)
                .first { (index, number) ->
                    !it.sumOf2(index - preamble, preamble, number)
                }
        }.value
    }

    private fun List<Long>.contiguousSet(start: Int, target: Long): List<Long>? {
        var sum = 0L
        for (i in start..this.lastIndex) {
            sum += this[i]
            if (sum == target) return this.subList(start, i + 1)
            if (sum > target) return null
        }
        return null
    }

    fun partTwo(lines: List<String>, preamble: Int = 25): Long {
        val target: Long = partOne(lines, preamble)
        val numbers = lines
            .map { it.toLong() }
            .filter { it < target }
        val contiguousSet: List<Long> = numbers
            .withIndex()
            .firstNotNullOf { (start, _) -> numbers.contiguousSet(start, target) }
        return contiguousSet.min() + contiguousSet.max()
    }
}

fun main() {
    println(Day9.partOne(readFileLines(9, 2020)))
    println(Day9.partTwo(readFileLines(9, 2020)))
}