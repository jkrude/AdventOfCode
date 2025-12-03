package y2025

import common.readFileLines
import kotlin.system.measureTimeMillis

object Day3 {

    /**
     * Greedy algorithm finding the topK largest elements maximizing:
     * 10^(topK)*n_1 + 10^(topK-1)*n_2 + ... + 10^0*n_k,
     * where n_i is the first found the highest element.
     * The algorithm is linear in topK and the length of each list.
     * Specifically, we get the worst time of topK * (n-topK+1), with n being the length of each list.
     *
     * The worst case is having the largest element first but scanning until the end for a higher one.
     * With n=10 and topK=3 we would get:
     *  1. scan goes from 0 to 7 (8,9 have to be left)
     *  2. scan goes from 1 to 8
     *  3. scan goes from 2 to 9
     *  Following this logic each scan starts one further to the right but also searches for one more place to the end.
     * This gives us a runtime of:
     * (n-(k-1) + ((n-1)-(k-2)) + ((n-2)-(k-3)) + ...
     * = (n-k+1) + (n-k+1) + ... + n-k+1
     * = k * (n-k+1)
     */
    fun maxJoltage(lines: List<String>, topK: Int): Long =
        lines.sumOf { bankStr ->
            // find highest 12 numbers in order
            var maxJoltage = 0L
            val indexedBank: List<IndexedValue<Int>> = bankStr.mapIndexed { index, char ->
                IndexedValue(index, char.digitToInt())
            }
            var lastIndex = -1
            repeat(topK) { k ->
                // Find the largest element after lastIndex
                val remainingAfterThis = topK - k - 1
                val searchEnd = indexedBank.size - remainingAfterThis // leave space for remaining
                // Use for-loop and extra variables for more performance.
                val nextHighest: IndexedValue<Int> = indexedBank.subList(lastIndex + 1, searchEnd).maxBy { it.value }
                lastIndex = nextHighest.index
                // Add the element to our running sum. The first element has the highest exponent 10^topK.
                // In order to avoid `pow` and floating conversions, we multiply by 10 in each iteration.
                maxJoltage = maxJoltage * 10L + nextHighest.value
            }
            return@sumOf maxJoltage
        }

    fun partOne(lines: List<String>): Long = maxJoltage(lines, 2)

    fun partTwo(input: List<String>): Long = maxJoltage(input, 12)
}

fun main() {
    val input = readFileLines(3, 2025)
    val t = measureTimeMillis {
        println(Day3.partOne(input))
        println(Day3.partTwo(input))
    }
    println("Took $t ms")
}