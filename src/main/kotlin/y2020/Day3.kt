package y2020

import common.Point2D
import common.readFileLines

object Day3 {

    data class TreeMap(
        private val trees: Array<BooleanArray>,
        val width: Int = trees.first().size,
        val height: Int = trees.size
    ) {
        constructor(lines: List<String>) : this(parseMap(lines))

        fun get(ij: Point2D): Boolean {
            return trees[ij.y][ij.x % width]
        }
    }

    fun parseMap(lines: List<String>): Array<BooleanArray> {
        return Array(lines.size) { y ->
            BooleanArray(lines[y].length) { x ->
                lines[y][x] == '#'
            }
        }
    }

    fun TreeMap.treesEncountered(right: Int, down: Int): Int {
        var treesEncountered = 0
        val position = Point2D(0, 0)
        while (position.y <= this.height - 1) {
            treesEncountered += if (this.get(position)) 1 else 0
            position.x += right
            position.y += down
        }
        return treesEncountered
    }

    fun partOne(lines: List<String>): Int {
        val treeMap = TreeMap(lines)
        val right = 3
        val down = 1
        return treeMap.treesEncountered(right, down)
    }

    fun partTwo(lines: List<String>): Long =
        TreeMap(lines).run {
            listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
                .map { (right, down) -> this.treesEncountered(right, down).toLong() }
                .reduce { acc, i -> acc * i }
        }
}

fun main() {
//    println(Day3.partOne(readFileLines(3, 2020)))
    println(Day3.partTwo(readFileLines(3, 2020)))
}