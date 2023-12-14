package y2022

import common.Point2D
import common.algorithms.Search
import common.extensions.autoRange
import common.readFileLines
import common.x2y
import kotlin.math.max
import kotlin.math.min

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
            val paths: List<List<Pair<Point2D, Point2D>>>

            var maxDepth: Int
                private set

            val minX: Int
            val maxX: Int

            var hasInfiniteGround = false
                private set

            init {
                paths = pathsDescription.map { pathString ->
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
                maxDepth = paths.flatten().maxOf {
                    val (from, to) = it
                    max(from.y, to.y)
                }
                minX = paths.flatten().minOf {
                    val (from, to) = it
                    min(from.x, to.x)
                }
                maxX = paths.flatten().maxOf {
                    val (from, to) = it
                    max(from.x, to.x)
                }
            }

            fun addInfiniteGround() {
                hasInfiniteGround = true
                maxDepth += 2
            }

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
            var sandCounter = 0

            fun neighbours(point: Point2D): List<Point2D> {
                val (x, y) = point
                return listOf(
                    (x x2y y + 1),
                    (x - 1 x2y y + 1),
                    (x + 1 x2y y + 1)
                ).filter { !groundMap.isSolid(it) }
            }

            Search.startingFrom(500 x2y 0)
                .neighbors(::neighbours)
                .onEachVisit {
                    sandCounter++
                    groundMap.addSand(it)
                }
                .executeBfs()
            return sandCounter
        }
    }
}

fun main() {
    println(Day14.partOne(readFileLines(14, 2022)))
    println(Day14.partTwo(readFileLines(14, 2022)))
}