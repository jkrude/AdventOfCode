package Y2022

import indices2d
import readFileLines

class Day8 {
    companion object {

        private fun parseToTreeMap(lines: List<String>) = lines.map {
            it.map { char ->
                char.digitToInt()
            }
        }

        //  tree is visible if all the other trees between it and an edge of the grid are shorter than it
        fun partOne(lines: List<String>): Int {
            val rows = lines.size
            val columns = lines[0].length
            val treeMap: List<List<Int>> = parseToTreeMap(lines)
            val visible = Array(rows) { Array(columns) { false } }
            fun inTopHalf(i: Int) = i <= rows / 2
            fun inLeftHalf(j: Int) = j <= columns / 2

            for (i in 1 until (rows - 1)) {
                for (j in 1 until (columns - 1)) {
                    // top and left
                    visible[i][j] = treeMap.subList(0, i).all { it[j] < treeMap[i][j] }
                            || treeMap[i].subList(0, j).all { it < treeMap[i][j] }
                            || treeMap.subList(i + 1, rows).all { it[j] < treeMap[i][j] }
                            || treeMap[i].subList(j + 1, columns).all { it < treeMap[i][j] }
                }
            }

            for (i in 0 until rows) {
                visible[i][0] = true
                visible[i][columns - 1] = true
            }
            for (j in 0 until columns) {
                visible[0][j] = true
                visible[rows - 1][j] = true
            }
            for (i in 0 until rows) {
                println(visible[i].joinToString(separator = " ") { if (it) "1" else "0" })
            }
            return visible.flatten().count { it }
        }

        fun List<List<Int>>.above(i: Int, j: Int) = this.subList(0, i)

        fun partTwo(lines: List<String>): Int {
            val treeMap = parseToTreeMap(lines)
            val rows = treeMap.size
            val columns = treeMap[0].size

            fun above(i: Int, j: Int): Int {
                if (i == 0) return 0
                var cnt = 0
                for (k in (i - 1) downTo 0) {
                    if (treeMap[k][j] >= treeMap[i][j]) return cnt + 1
                    else cnt++
                }
                return cnt
            }

            fun below(i: Int, j: Int): Int {
                if (i == rows - 1) return 0
                var cnt = 0
                for (k in (i + 1) until rows) {
                    if (treeMap[k][j] >= treeMap[i][j]) return cnt + 1
                    else cnt++
                }
                return cnt
            }

            fun left(i: Int, j: Int): Int {
                if (j == 0) return 0
                var cnt = 0
                for (k in (j - 1) downTo 0) {
                    if (treeMap[i][k] >= treeMap[i][j]) return cnt + 1
                    else cnt++
                }
                return cnt
            }

            fun right(i: Int, j: Int): Int {
                if (j == columns - 1) return 0
                var cnt = 0
                for (k in (j + 1) until rows) {
                    if (treeMap[i][k] >= treeMap[i][j]) return cnt + 1
                    else cnt++
                }
                return cnt
            }

            fun lookAt(list: List<Int>, value: Int): Int {
                return list.indexOfFirst { it >= value } + 1
            }

            fun lookAround(i: Int, j: Int): Int {
                if (i == 0 || i == rows - 1 || j == 0 || j == columns - 1) return 0
                val height = treeMap[i][j]
                return lookAt(treeMap.subList(0, i).map { it[j] }, height) *
                        lookAt(treeMap.subList(i + 1, rows).map { it[j] }, height) *
                        lookAt(treeMap[i].subList(0, j), height) *
                        lookAt(treeMap[i].subList(j + 1, columns), height)
            }

            return treeMap.indices2d().maxOf {
                val (i, j) = it
                above(i, j) * left(i, j) * below(i, j) * right(i, j)
            }
        }
    }
}

fun main() {
    val testData = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent().lines()
    println(Day8.partTwo(readFileLines(8)))
//    println(Day8.partOne(readFileLines(8)))
}