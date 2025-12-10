package y2025

import common.Direction
import common.extensions.takePair
import common.math.distinctPairs
import common.readFileLines
import java.lang.Long.signum
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day9 {

    data class Point(val x: Long, val y: Long)
    data class Edge(val start: Point, val end: Point) {
        val isHorizontal: Boolean = start.y == end.y

        val minY = min(start.y, end.y)
        val maxY = max(start.y, end.y)
        val minX = min(start.x, end.x)
        val maxX = max(start.x, end.x)
        val dx = end.x - start.x
        val dy = end.y - start.y

        init {
            require(start.x == end.x || start.y == end.y) { "Illegal edge ${start}-${end}" }
        }

        fun contains(point: Point) = point.x in minX..maxX && point.y in minY..maxY
        infix fun crosses(other: Edge): Boolean {
            return if (isHorizontal == other.isHorizontal) {
                // Both horizontal or both vertical - check for overlap
                if (isHorizontal) {
                    // Both horizontal: same y and overlapping x ranges
                    start.y == other.start.y && maxX >= other.minX && minX <= other.maxX
                } else {
                    // Both vertical: same x and overlapping y ranges
                    start.x == other.start.x && maxY >= other.minY && minY <= other.maxY
                }
            } else {
                // One horizontal, one vertical - check if they cross
                if (isHorizontal) {
                    // This is horizontal, other is vertical
                    other.start.x in minX..maxX && start.y in other.minY..other.maxY
                } else {
                    // This is vertical, other is horizontal
                    start.x in other.minX..other.maxX && other.start.y in minY..maxY
                }
            }
        }

        fun endpointWith(other: Edge): Point? {
            if (start == other.start) return start
            if (start == other.end) return start
            if (end == other.start) return end
            if (end == other.end) return end
            return null
        }

        infix fun crossProduct(other: Edge): Long =
            (this.dx * other.dy) - (this.dy * other.dx)
    }

    data class Rectangle(val p1: Point, val p2: Point) {
        val corners: List<Point>
        val edges: List<Edge>
        val minY = min(p1.y, p2.y)
        val maxY = max(p1.y, p2.y)
        val minX = min(p1.x, p2.x)
        val maxX = max(p1.x, p2.x)

        init {
            require(p1.x != p2.x && p1.y != p2.y) // Both points differ in x and in y
            corners = mutableListOf(p1, Point(p1.x, p2.y), p2, Point(p2.x, p1.y))
            edges = corners.zipWithNext { a, b -> Edge(a, b) }.plusElement(Edge(corners.last(), p1))
        }

        val area = area(p1, p2)

        // Don't count edges as inside
        fun fullyContains(point: Point): Boolean {
            val inside = point.x in minX..maxX && point.y in minY..maxY
            return inside && !this.edges.any { it.contains(point) }
        }
    }

    fun area(a: Point, b: Point): Long =
        (abs(a.x - b.x) + 1L) * (abs(a.y - b.y) + 1L)

    fun parsePoints(lines: List<String>): List<Point> =
        lines.map { it.split(",").map(String::toLong).takePair().let { Point(it.first, it.second) } }


    fun partOne(lines: List<String>): Long =
        parsePoints(lines)
            .distinctPairs()
            .maxOf { (a, b) -> area(a, b) }


    /**
     * Converting relative side (left or right) to absolute directions.
     */
    fun Edge.whichSideIsPolygonNormalized(insideIsRight: Boolean): Direction {
        val relativeToDirection = if (this.isHorizontal) {
            if (this.start.x < this.end.x) Direction.SOUTH else Direction.NORTH
        } else {
            if (this.start.y < this.end.y) Direction.WEST else Direction.EAST
        }
        return if (insideIsRight) relativeToDirection else relativeToDirection.opposite()
    }

    /**
     * We solve part two by successivly throwing out rectangles that are invalid.
     * The core part is knowing for any edge of our polygon which side of the edge is the inside of the polygon.
     * This way we can determine whether a rectangle is inside even if it does not cross any edge of the polygon.
     * The total runtime is O(N^2) going over all rectangles and comparing with each point (edge), N being the number of given points.
     * We assume that there are no edges directly adjacent. E.g. (0,0), (0,2), (2,1), (0,1).
     */
    fun partTwo(lines: List<String>): Long {
        val points = parsePoints(lines)
        val edges: List<Edge> = points.plusElement(points.first()).zipWithNext { a, b -> Edge(a, b) }
        val verticalEdges = edges.filter { !it.isHorizontal }.sortedByDescending { it.minX }
        // Calculate the total rotation by following the path of edges
        // +1 for Left Turn (CCW), -1 for Right Turn (CW)
        // Note as edges also includes (last,first) we have to drop the last edge.
        val totalRotation = edges.zipWithNext().dropLast(1).sumOf { (current, next) ->
            // The cross product magnitude will be large, but we only care about the sign.
            // sign > 0 is Left/CCW, sign < 0 is Right/CW
            val cross = current.crossProduct(next)

            // Normalize to simple +1 or -1 steps
            // (Assuming rectilinear: cross will never be 0 for corners)
            signum(cross)
        }
        // -4 means -360° implies a counter clockwise rotation
        // 4 means 360° implies a clockwise rotation
        require(totalRotation == -4 || totalRotation == 4)
        val insideIsRight = totalRotation == 4


        val potentialRectangle = points.distinctPairs()
            .mapNotNull { (p1, p2) ->
                // skip straight lines
                if (p1.x == p2.x || p1.y == p2.y) null
                else Rectangle(p1, p2)
            }.filterNot { rect ->
                points.any { rect.fullyContains(it) }
            }


        return potentialRectangle.filterNot { rect ->
            // Filter out rectangles that cross with any edge.
            // A bad crossing would cut two edges of our rectangle.
            // The exception is that the edge is exactly one side of the rectangle.
            edges.any { edge ->
                rect.edges.count { rectEdge ->
                    rectEdge.crosses(edge)
                            && rectEdge.endpointWith(edge) == null // edges share no common endpoint
                            && rectEdge.isHorizontal != edge.isHorizontal // one is vertical the other horizontal
                } >= 2
            }
        }.filter { rect ->
            // Get the closest edge to the right of this rectangle.
            // Check whether WEST of the edge (where our rectangle is) is also the inside of the rectangle.
            // This check is necessary for something like an outer corner of the polygon (|_),
            // where the top and right points of the L form our rectangle.
            val rigthSideX = rect.maxX
            val firstEdgeToTheRight: Edge = verticalEdges.first { it.minX >= rigthSideX }
            firstEdgeToTheRight.whichSideIsPolygonNormalized(insideIsRight) == Direction.WEST
        }.maxOf { it.area }
    }
}

fun main() {
    val input = readFileLines(9, 2025)
    println(Day9.partOne(input))
    println(Day9.partTwo(input))
}