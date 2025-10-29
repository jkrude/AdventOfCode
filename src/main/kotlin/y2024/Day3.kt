package y2024

import common.extensions.product
import common.readFileText

object Day3 {
    private const val mulPattern = """mul\((\d{1,3}),(\d{1,3})\)"""

    private fun MatchResult.mul() = this.groupValues.drop(1).map(String::toLong).product()

    fun partOne(text: String): Long = mulPattern.toRegex().findAll(text).sumOf { it.mul() }

    val partTwoRegex = "do\\(\\)|don't\\(\\)|$mulPattern".toRegex()

    data class Item(val idx: Int, val enabled: Boolean, val value: Long = 0L)

    fun partTwo(text: String): Long {
        fun next(last: Item): Item? =
            partTwoRegex.find(text, last.idx)
                ?.let { match ->
                    val nextIdx = match.range.last + 1
                    if (last.enabled && match.groups[1] != null) {
                        Item(nextIdx, true, match.mul())
                    } else {
                        Item(nextIdx, match.value == "do()")
                    }
                }
        return generateSequence(Item(0, true, 0L), ::next).sumOf { it.value }
    }
}

fun main() {
    val input = readFileText(3, 2024)
    println(Day3.partOne(input))
    println(Day3.partTwo(input))
}