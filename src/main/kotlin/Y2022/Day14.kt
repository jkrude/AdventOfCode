package Y2022

import autoRange
import common.Point2D
import common.x2y
import readFileLines
import kotlin.math.max
import kotlin.math.min

private operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>): Pair<Int, Int> =
    (this.first + pair.first) to (this.second + pair.second)

private operator fun Pair<Point2D, Point2D>.contains(xy: Point2D): Boolean {
    val (from, to) = this
    val (x, y) = xy
    return (x in from.x autoRange to.x) && (y in from.y autoRange to.y)
}

private infix fun Point2D.onStraightLine(line: Pair<Point2D, Point2D>): Boolean {
    val (from, to) = line
    return ((this.x == from.x || this.x == to.x) && (this.y in from.y autoRange to.y))
            || ((this.y == from.y || this.y == to.y) && (this.x in from.x autoRange to.x))
}

class Day14 {
    companion object {

        class GroundMap(pathsDescription: List<String>) {
            private val sandMap = mutableSetOf<Point2D>()
            val paths: List<List<Pair<Point2D, Point2D>>> = pathsDescription.map { pathString ->
                pathString
                    .split(" -> ")
                    .map { pathNode ->
                        val asSplit = pathNode.split(",").map { nodeXorY ->
                            nodeXorY.toInt()
                        }
                        asSplit.first() x2y asSplit.last()
                    }
                    .windowed(2).map {
                        it.first() to it.last()
                    }
            }

            var maxDepth = paths.flatten().maxOf {
                val (from, to) = it
                max(from.y, to.y)
            }
                private set

            val minX = paths.flatten().minOf {
                val (from, to) = it
                min(from.x, to.x)
            }
            val maxX = paths.flatten().maxOf {
                val (from, to) = it
                max(from.x, to.x)
            }

            var hasInfiniteGround = false
                private set

            fun addInfiniteGround() {
                hasInfiniteGround = true
                maxDepth += 2
            }

            fun besideMap(xy: Point2D): Boolean = xy.x < minX || xy.x > maxX

            fun isSolid(xy: Point2D): Boolean {
                if (hasInfiniteGround && xy.y == this.maxDepth) return true
                if (xy in sandMap) return true
                for (path in paths) {
                    for (pathSegment: Pair<Point2D, Point2D> in path) {
                        if (xy onStraightLine pathSegment) return true
                    }
                }
                return false
            }

            fun hasSand(xy: Point2D): Boolean = xy in sandMap

            fun addSand(xy: Point2D): Boolean = sandMap.add(xy)
            fun notBelowMap(currSand: Point2D): Boolean = hasInfiniteGround || currSand.y <= this.maxDepth
        }

        fun visualize(groundMap: GroundMap) {
            val width = groundMap.maxX - groundMap.minX
            val height = groundMap.maxDepth + 3
            val map2D = Array(height) { Array(width + 1) { "." } }
            for (i in map2D.indices) {
                for (j in map2D.first().indices) {
                    val curr = (494 + j) x2y i
                    if (groundMap.hasSand(curr)) map2D[i][j] = "O"
                    else if (groundMap.isSolid(curr)) map2D[i][j] = "#"
                }
            }
            println(
                map2D.joinToString("\n") { row ->
                    row.joinToString("")
                }
            )
        }

        private fun fallDown(sandStart: Point2D, groundMap: GroundMap): Boolean {
            var currSand = sandStart
            var fellAtLeastOnce = false
            while (groundMap.notBelowMap(currSand)) {
                val moveOffset = listOf((0 x2y 1), (-1 x2y 1), (1 x2y 1))
                    .firstOrNull { !groundMap.isSolid(currSand + it) }
                    ?: break
                fellAtLeastOnce = true
                currSand = (currSand + moveOffset)
            }
            if (!groundMap.notBelowMap(currSand)) return false
            groundMap.addSand(currSand)
            return fellAtLeastOnce
        }

        private fun simulate(groundMap: GroundMap): Int {
            val sandStart = 500 x2y 0
            var sandCounter = 0
            while (fallDown(sandStart, groundMap)) {
                sandCounter++
            }
            return sandCounter
        }

        fun partOne(paths: List<String>): Int {
            val groundMap = GroundMap(paths)
            return simulate(groundMap)
        }


        fun partTwo(lines: List<String>): Int {
            val groundMap = GroundMap(lines)
            groundMap.addInfiniteGround()
            visualize(groundMap)
            return simulate(groundMap) + 1
        }
    }
}

fun main() {
    println(Day14.partOne(readFileLines(14)))
    println(Day14.partTwo(readFileLines(14)))
}