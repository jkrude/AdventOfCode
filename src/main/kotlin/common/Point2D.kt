package common

import common.extensions.List2D
import kotlin.math.abs

data class Point2D(var x: Int = 0, var y: Int = 0) {

    operator fun plusAssign(scala: Int) {
        x += scala
        y += scala
    }

    operator fun plusAssign(other: Point2D) {
        x += other.x
        y += other.y
    }

    operator fun timesAssign(scala: Int) {
        x *= scala
        y *= scala
    }

    inline fun map(operation: (Int) -> Int) =
        Point2D(operation(this.x), operation(this.y))


    operator fun times(other: Point2D) =
        Point2D(x * other.x, y * other.y)

    operator fun times(scala: Int) =
        map { it * scala }

    operator fun plus(scala: Int) =
        map { it + scala }

    operator fun plus(other: Point2D) =
        Point2D(this.x + other.x, this.y + other.y)

    fun manhattanDistanceTo(other: Point2D): Int =
        abs(this.x - other.x) + abs(this.y - other.y)

    fun fourNeighbours(): List<Point2D> {
        val (i, j) = this
        return listOf(
            Point2D(i + 1, j),
            Point2D(i, j + 1),
            Point2D(i, j - 1),
            Point2D(i - 1, j),
        )
    }


    fun eightNeighbours(): List<Point2D> {
        val (i, j) = this
        return listOf(
            Point2D(i + 1, j + 1),
            Point2D(i + 1, j),
            Point2D(i + 1, j - 1),
            Point2D(i, j + 1),
            Point2D(i, j - 1),
            Point2D(i - 1, j - 1),
            Point2D(i - 1, j),
            Point2D(i - 1, j + 1)
        )
    }

    companion object {
        fun of(pair: Pair<Int, Int>) = Point2D(pair.first, pair.second)
    }

}
infix fun Int.x2y(other: Int) = Point2D(this, other)

fun <T> List2D<T>.getOrNull(ij: Point2D) = this.getOrNull(ij.x)?.getOrNull(ij.y)

infix fun <T> List2D<T>.containsPoint(point: Point2D) = point.x in this.indices && point.y in this[point.x].indices
operator fun <T> List2D<T>.get(ij: Point2D): T = this.getOrNull(ij.x)?.getOrNull(ij.y)!!