package Y2022

import readFileLines
import kotlin.math.abs

class Day9 {
    companion object {

        // Figure out the number of unique positions the tails visited
        fun partOne(instructions: List<String>, tailLength: Int): Int {
            var posHead = (15 to 11)

            val tailPositions = Array(tailLength) { posHead }
            val visited = mutableSetOf(posHead)

            for (move in instructions) {
                val amount = move.substring(2).toInt()
                repeat(amount) {
                    var (posHeadX, posHeadY) = posHead
                    when (move[0]) {
                        'U' -> posHeadX -= 1
                        'D' -> posHeadX += 1
                        'L' -> posHeadY -= 1
                        'R' -> posHeadY += 1
                    }
                    posHead = posHeadX to posHeadY
                    for ((idx, knot) in tailPositions.withIndex()) {
                        val currHead = if (idx == 0) posHead else tailPositions[idx - 1]
                        if (!currHead.touches(knot)) {
                            tailPositions[idx] = moveTail(knot, currHead)
                        }
                    }
                    visited.add(tailPositions.last())
                }
            }
            visualizeVisited(visited)
            return visited.size
        }

        private fun visualizeVisited(visited: Set<Pair<Int, Int>>) {
            val map = Array(22) { Array(27) { "-" } }
            for (i in 0..21) {
                for (j in 0..26) {
                    if ((i to j) in visited) map[i][j] = "#"
                }
            }
            println(map.joinToString(separator = "\n") { it.joinToString(separator = "") })
        }

        private fun visualizeTail(head: Pair<Int, Int>, tail: Array<Pair<Int, Int>>) {
            val map = Array(22) { Array(27) { "-" } }
            map[head.first][head.second] = "H"
            for ((idx, knot) in tail.withIndex()) {
                map[knot.first][knot.second] = (idx + 1).toString()
            }
            println(map.joinToString(separator = "\n") { it.joinToString(separator = "") })
        }

        private fun moveTail(posTail: Pair<Int, Int>, posHead: Pair<Int, Int>): Pair<Int, Int> {
            var (x, y) = posTail
            if (posTail.first == posHead.first || posTail.second != posHead.second) {
                y += if (posHead.second > posTail.second) 1 else -1
            }
            if (posTail.second == posHead.second || posTail.first != posHead.first) {
                x += if (posHead.first > posTail.first) 1 else -1
            }
            return x to y
        }
    }
}

private fun Pair<Int, Int>.touches(posTail: Pair<Int, Int>): Boolean =
    abs(this.first - posTail.first) <= 1 && abs(this.second - posTail.second) <= 1


private operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>): Pair<Int, Int> {
    return (this.first + pair.first) to (this.second + pair.second)
}

fun main() {
    val testData = """
       R 5
       U 8
       L 8
       D 3
       R 17
       D 10
       L 25
       U 20
    """.trimIndent().lines()
    println(Day9.partOne(readFileLines(9), 9))
}
