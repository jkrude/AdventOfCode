package y2024

import common.findAllLong
import common.readFileText

object Day11 {

    private fun blink(stone: Long): Sequence<Long> {
        return when {
            stone == 0L -> sequenceOf(1L)
            stone.toString().length % 2 == 0 -> stone.toString().let { lStr ->
                val splitPoint = lStr.length / 2
                sequenceOf(
                    lStr.substring(0, splitPoint).toLong(),
                    lStr.substring(startIndex = splitPoint).toLong()
                )
            }

            else -> sequenceOf(stone * 2024L)
        }
    }

    fun partOne(input: String): Int {
        var stones = input.findAllLong()
        repeat(25) {
            stones = stones.flatMap { blink(it) }
        }
        return stones.count()
    }


    fun partTwo(input: String): Long {

        // [i][j] How many stones exists when starting with i after j blinks.
        val stonesLookup = mutableMapOf<Pair<Int, Long>, Long>()
        fun cache(iteration: Int, stone: Long, result: Long): Long {
            stonesLookup[iteration to stone] = result
            return result
        }

        fun blinkRecursive(iteration: Int, stone: Long): Long {
            if (iteration == 0) return cache(iteration, stone, 1L)
            return stonesLookup[iteration to stone] ?:  cache(
                iteration,
                stone,
                blink(stone).sumOf { blinkRecursive(iteration - 1, it) }
            )
        }
        return input.findAllLong().sumOf { blinkRecursive(75, it) }
    }
}

fun main() {
    val input = readFileText(11, 2024)
    println(Day11.partOne(input))
    println(Day11.partTwo(input))
}