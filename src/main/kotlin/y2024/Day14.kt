package y2024

import common.Point2D
import common.findAllLong
import common.readFileLines
import common.extensions.product
import common.extensions.productOf

object Day14 {
    private data class Robot(val pos: Point2D, val vel: Point2D) {
        companion object {
            fun parse(string: String): Robot =
                string.findAllLong().map { it.toInt() }.toList()
                    .let { Robot(Point2D(it[0], it[1]), Point2D(it[2], it[3])) }
        }

        fun move(xMax: Int = 101, yMax: Int = 103) {
            pos += vel
            pos.x = Math.floorMod(pos.x, xMax)
            pos.y = Math.floorMod(pos.y, yMax)
        }
    }

    private fun safetyFactor(robots: List<Robot>, xMax: Int, yMax: Int): Long {
        val middleX = xMax / 2
        val middleY = yMax / 2
        return robots.filter { it.pos.x != middleX && it.pos.y != middleY }
            .partition { it.pos.x < middleX }
            .toList()
            .map { subList ->
                subList.partition { it.pos.y < middleY }
                    .toList()
                    .productOf { it.size.toLong() }
            }.product()
    }

    fun partOne(lines: List<String>, xMax: Int = 101, yMax: Int = 103): Long {
        val robots = lines.map { Robot.parse(it) }
        repeat(100) {
            robots.forEach { it.move(xMax, yMax)}
        }
        return safetyFactor(robots, xMax, yMax)
    }

    private fun print(robots: List<Robot>) {
        val grid = Array(101) { CharArray(103) { ' ' } }
        for (r in robots) {
            grid[r.pos.x][r.pos.y] = '#'
        }
        println(grid.joinToString(separator = "\n") { it.joinToString(separator = "") })
    }

    fun partTwo(lines: List<String>): Int {
        val robots = lines.map { Robot.parse(it) }
        var seconds = 0
        // Assumes that a tree is formed if all robots are on a unique place
        while (robots.mapTo(mutableSetOf()) { it.pos }.size != robots.size) {
            robots.forEach(Robot::move)
            seconds++
        }
        return seconds
    }
}

fun main() {
    val input = readFileLines(14, 2024)
    println(Day14.partOne(input))
    println(Day14.partTwo(input))
}