package y2024

import common.readFileLines

object Day22 {

    fun next(secret: Long): Long {
        val step1 = (secret xor (secret * 64L)) % 16777216
        val step2 = (step1 xor (step1 / 32L)) % 16777216
        return (step2 xor (step2 * 2048L)) % 16777216
    }

    fun partOne(lines: List<String>): Long {
        return lines.map(String::toLong).sumOf { n ->
            var s = n
            repeat(2000) { s = next(s) }
            s
        }
    }

    fun hashFourDigits(values: List<Int>): Int {
        // Combine all four values into a single unique integer.
        // Single digits range from -9 to 9, which is 19 possible values.
        // We can map each to 0-18 by adding 9.
        return ((values[0] + 9) * 19 * 19 * 19) +
                ((values[1] + 9) * 19 * 19) +
                ((values[2] + 9) * 19) +
                (values[3] + 9)
    }

    data class HashValue(var maxBuyerId: Int = -1, var totalPrize: Long = 0)

    fun partTwo(lines: List<String>): Long {

        val prizes: List<List<Int>> = lines
            .map(String::toLong)
            .map { initial ->
                generateSequence(initial) { next(it) }
                    .take(2_001) // seed + 2_000 generated secrets
                    .map { it % 10 } // Take the last digit: secret -> prize.
                    .map { it.toInt() }
                    .toList()
            }
        // Sum up the revenue for each pattern.
        // For each buyer we only count the first occurrence of a pattern, therefore, we store the buyerId.
        val revenuePerChanges: MutableMap<Int, HashValue> = mutableMapOf()
        prizes.forEachIndexed { buyerId, prizeList ->
            prizeList
                .windowed(2) { (a, b) -> b - a }
                .windowed(4) // Changes of four consecutive prizes.
                .forEachIndexed { idx, changes: List<Int> ->
                    val key = hashFourDigits(changes)
                    val value: HashValue = revenuePerChanges[key] ?: HashValue().also { revenuePerChanges[key] = it }
                    if (value.maxBuyerId < buyerId) {
                        value.maxBuyerId = buyerId
                        value.totalPrize += prizeList[idx + 4] // We associate the changes-pattern with the last digit, not the first.
                    }
                }
        }
        return revenuePerChanges.values.maxOf { it.totalPrize }
    }

}

fun main() {
    val input = readFileLines(22, 2024)
    println(Day22.partOne(input))
    println(Day22.partTwo(input))
}