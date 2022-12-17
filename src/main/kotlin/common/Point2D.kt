package common

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

}

fun Point2D.of(pair: Pair<Int, Int>) = Point2D(pair.first, pair.second)
infix fun Int.x2y(other: Int) = Point2D(this, other)