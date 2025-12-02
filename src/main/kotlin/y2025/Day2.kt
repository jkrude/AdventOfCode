package y2025

import common.readFileText

object Day2 {

    private fun parse(input: String): List<LongRange> =
        input.split(',')
            .map {
                it.split('-').map { n -> n.toLong() }
            }.map { (a, b) -> a..b }

    private fun sumOfRepeating(input: String, repeatingTest: (Long) -> Boolean): Long =
        parse(input).parallelStream()
            .mapToLong { range -> range.filter(repeatingTest).sum() }
            .sum()

    private fun testRepeatingTwice(numberLong: Long): Boolean {
        val number = numberLong.toString()
        if (number.length % 2 != 0) return false
        val half = number.length / 2
        return number.regionMatches(0, number, half, half)
    }

    fun partOne(input: String): Long = sumOfRepeating(input, ::testRepeatingTwice)

    private fun testRepeating(numberLong: Long): Boolean {
        val number = numberLong.toString()
        if (number.length % 2 != 0) false
        val half = number.length / 2
        return (1..half).any { chunkSize ->
            allChunksEqual(number, chunkSize)
        }
    }

    /**
     * allChunksEqual effectively computes:
     *  val parts = number.chunked(chunkSize)
     *  return parts.all { it == parts.first() }
     *
     *  However, the optimized version avoids any extra allocations (list and substrings).
     *  This is about 5 times faster (tested on the puzzle input)
     */
    fun allChunksEqual(number: String, chunkSize: Int): Boolean {
        val len = number.length
        if (chunkSize <= 0 || len % chunkSize != 0) return false
        val step = chunkSize
        var i = step
        while (i < len) {
            if (!number.regionMatches(0, number, i, step)) return false
            i += step
        }
        return true
    }

    fun partTwo(input: String): Long =
        sumOfRepeating(input, ::testRepeating)
}

fun main() {
    val input = readFileText(2, 2025)
    println(Day2.partOne(input))
    println(Day2.partTwo(input))
}