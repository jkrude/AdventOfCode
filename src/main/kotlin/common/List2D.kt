package common

import kotlin.math.max

typealias List2D<T> = List<List<T>>

typealias BooleanList2D = List2D<Boolean>

object Lists2D {

    fun of(linesList: List<String>, trueChar: Char = '#', falseChar: Char = '.'): BooleanList2D =
        linesList.map { line ->
            line.map { c ->
                when (c) {
                    trueChar -> true
                    falseChar -> false
                    else -> throw IllegalArgumentException("Found char $c that was neither $trueChar nor $falseChar")
                }
            }
        }

    fun BooleanList2D.print(trueChar: Char = '#', falseChar: Char = '.'): String =
        print { if (it) trueChar.toString() else falseChar.toString() }

    fun <T> List2D<T>.print(transform: (T) -> String): String =
        this.joinToString(separator = "\n") {
            it.joinToString("", transform = transform)
        }

    fun Set<Point2D>.printAsMap(
        present: Char = '#',
        empty: Char = '.',
        minWidth: Int? = null,
        minHeight: Int? = null
    ): String {
        val maxY = this.maxOf { it.y }.let { if (minHeight == null) it else max(minHeight, it) }
        val maxX = this.maxOf { it.x }.let { if (minWidth == null) it else max(minWidth, it) }
        val stringBuilder = StringBuilder()
        for (y in maxY downTo 0) {
            for (x in 0..maxX) {
                if (Point2D(x, y) in this) stringBuilder.append(present)
                else stringBuilder.append(empty)
                if (x == maxX) stringBuilder.append("\n")
            }
        }
        return stringBuilder.toString()
    }

    fun <T> iterateUntilStable(map2D: List2D<T>, update: (List2D<T>) -> List2D<T>): List2D<T> {
        fun updateWithChange(map2D: List2D<T>): Pair<Boolean, List2D<T>> {
            val next = update(map2D)
            return (map2D == next) to next
        }
        return iterateUntilNoChange(map2D, ::updateWithChange)
    }

    fun <T> iterateUntilNoChange(map2D: List2D<T>, update: (List2D<T>) -> Pair<Boolean, List2D<T>>): List2D<T> {
        var last: List2D<T> = map2D
        do {
            val (changed, next) = update(last)
            if (!changed) return next
            last = next
        } while (true)
    }


    fun <T> List2D<T>.indices2d() =
        this.indices.flatMap { i -> this[i].indices.map { j -> i to j } }

    operator fun <T> List2D<T>.get(i: Int, j: Int) = this[i][j]

    fun <T> List2D<T>.getOrNull(i: Int, j: Int) = this.getOrNull(i)?.getOrNull(j)

    fun <T> List2D<T>.getOrNull(ij: Pair<Int, Int>) = this.getOrNull(ij.first, ij.second)

    fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> = listOf(
        (first + 1) to second,
        (first - 1) to second,
        first to (second + 1),
        first to (second - 1)
    )

    fun Pair<Int, Int>.allNeighbour(): List<Pair<Int, Int>> {
        val (i, j) = this
        return listOf(
            (i + 1 to j + 1),
            (i + 1 to j),
            (i + 1 to j - 1),
            (i to j + 1),
            (i to j - 1),
            (i - 1 to j - 1),
            (i - 1 to j),
            (i - 1 to j + 1)
        )
    }

    operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>) = (first + pair.first) to (second + pair.second)
    operator fun Pair<Int, Int>.minus(pair: Pair<Int, Int>) = (first - pair.first) to (second - pair.second)
    operator fun <T> List2D<T>.get(ij: Pair<Int, Int>): T = this[ij.first][ij.second]
    fun <T, V> List2D<T>.map2DIndexed(transform: (Pair<Int, Int>, T) -> V): List2D<V> {
        return this.mapIndexed { i, ts ->
            ts.mapIndexed { j, t ->
                transform(i to j, t)
            }
        }
    }

    fun <T, V> List2D<T>.map2D(transform: (T) -> V): List2D<V> {
        return this.map {
            it.map(transform)
        }
    }
}