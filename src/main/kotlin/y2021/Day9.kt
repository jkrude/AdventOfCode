package y2021

import common.extensions.Idx2D
import common.extensions.Lists2D.getOrNull
import common.extensions.Lists2D.indices2d
import common.extensions.neighbours
import common.extensions.product
import common.readFileLines

class Day9 {

    companion object {

        fun readReport(fileLines: List<String> = readFileLines(9, 2021)): List<List<Int>> =
            fileLines.map { it.map { c -> c.digitToInt() } }

        // local minimum if all y2021.neighbours are greater
        private fun List<List<Int>>.isLocalMin(ij: Idx2D): Boolean =
            (ij).neighbours().all { this[ij.first][ij.second] < (this.getOrNull(it) ?: Int.MAX_VALUE) }

        private fun List<List<Int>>.getBasinSize(localMin: Idx2D): Int {
            // idea of recursive flood fill with 4 y2021.neighbours
            fun getBasinSizeRec(ij: Idx2D, comp: Int, visited: MutableSet<Idx2D>): Int {
                val curr: Int = this.getOrNull(ij) ?: return 0
                if (ij in visited || curr == 9) return 0
                if (curr >= comp) {
                    visited.add(ij)
                    return 1 + (ij.neighbours().sumOf { getBasinSizeRec(it, curr, visited) })
                }
                return 0
            }

            val curr: Int = this.getOrNull(localMin) ?: return 0
            return getBasinSizeRec(localMin, curr, HashSet())
        }

        fun partOne(heightMap: List<List<Int>>): Int =
            heightMap.indices2d()
                .filter { heightMap.isLocalMin(it) }
                .sumOf { (i, j) -> heightMap[i][j] + 1 }

        fun partTwo(heightMap: List<List<Int>>): Int =
            heightMap.indices2d()
                .filter { heightMap.isLocalMin(it) }
                .map { heightMap.getBasinSize(it) }
                .sortedDescending()
                .subList(0, 3)
                .product()
    }
}

fun main() {
    println("PART 1: ${Day9.partOne(Day9.readReport())}")
    println("PART 2: ${Day9.partTwo(Day9.readReport())}")
}