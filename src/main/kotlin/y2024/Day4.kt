package y2024

import common.extensions.Idx2D
import common.extensions.List2D
import common.extensions.Lists2D.get
import common.extensions.Lists2D.getOrNull
import common.extensions.Lists2D.indices2d
import common.extensions.plus
import common.extensions.toCharList2D
import common.math.crossProduct
import common.readFileLines

object Day4 {
    // Step-Differences in all directions vertical, horizontal and diagonal.
    private val steps: List<Pair<Int, Int>> = (-1..1).crossProduct(-1..1).filter { it.first != 0 || it.second != 0 }
    private fun Idx2D.move(step: Idx2D): Sequence<Idx2D> = generateSequence(this) { it + step }
    private fun Idx2D.xmasPattern(): List<Sequence<Pair<Int, Int>>> {
        return steps.map { step -> this.move(step).take(4) }
    }

    fun partOne(lines: List<String>): Int {
        val map: List<List<Char>> = lines.toCharList2D()
        val xmas: List<Char> = "XMAS".toList()
        // Count the number of XMAS patterns starting from each index.
        return map.indices2d().sumOf { ij ->
            if (map[ij] != 'X') 0
            ij.xmasPattern().count { indices ->
                indices.map { idx2d -> map.getOrNull(idx2d) }.toList() == xmas
            }
        }
    }

    private fun List2D<Char>.xmas(ij: Idx2D): Boolean {
        val (i, j) = ij
        fun msOrSm(left: Idx2D, right: Idx2D): Boolean {
            val c1 = this.getOrNull(left) ?: false
            val c2 = this.getOrNull(right) ?: false
            return (c1 == 'S' && c2 == 'M') || (c1 == 'M' && c2 == 'S')
        }
        return msOrSm(i - 1 to j - 1, i + 1 to j + 1) && msOrSm(i + 1 to j - 1, i - 1 to j + 1)
    }

    fun partTwo(lines: List<String>): Int {
        val map: List<List<Char>> = lines.toCharList2D()
        return map.indices2d().filter { map[it] == 'A' }.count { map.xmas(it) }
    }
}

fun main() {
    val input = readFileLines(4, 2024)
    println(Day4.partOne(input))
    println(Day4.partTwo(input))  // higher than 1659
}