package common.extensions

import common.Point2D
import kotlin.math.max

typealias List2D<T> = List<List<T>>
typealias Idx2D = Pair<Int, Int>
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

    fun <T> List2D<T>.getOrNull(ij: Idx2D) = this.getOrNull(ij.first, ij.second)

    operator fun <T> List2D<T>.get(ij: Idx2D): T = this[ij.first][ij.second]

    operator fun <T> MutableList<MutableList<T>>.set(ij: Idx2D, value: T) {
        this[ij.first][ij.second] = value
    }

    operator fun <T> MutableList<MutableList<T>>.set(ij: Point2D, value: T) {
        this[ij.x][ij.y] = value
    }

    infix fun <T> List<List<T>>.containsIndex(ij: Idx2D) = getOrNull(ij) != null

    fun <T, V> List2D<T>.map2DIndexed(transform: (Idx2D, T) -> V): List2D<V> {
        return this.mapIndexed { i, ts ->
            ts.mapIndexed { j, t ->
                transform(i to j, t)
            }
        }
    }

    fun <T, V> List2D<T>.map2D(transform: (T) -> V): List2D<V> =
        this.map { it.map(transform) }

    fun <T> List2D<T>.column(idx: Int): List<T> {
        return buildList {
            for (i in this@column.indices)
                add(this@column[i][idx])
        }
    }

    fun <T> List2D<T>.columns(): List<List<T>> {
        return buildList {
            for (i in this@columns.first().indices)
                add(this@columns.column(i))
        }
    }

    // Rotate any 2D-List assuming every sub-list has the same size.
    // Note this is not the same as transposing.
    fun <T> List2D<T>.rotatedClockwise(): List2D<T> {
        val rotated = ArrayList<ArrayList<T>>()
        for (i in this.first().indices) {
            rotated.add(ArrayList(this.first().size))
        }
        for (row in this.reversed()) {
            for ((j, e) in row.withIndex()) {
                rotated[j].add(e)
            }
        }
        return rotated
    }

}
