package common.extensions

import kotlin.math.abs

operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>) = (first + pair.first) to (second + pair.second)
operator fun Pair<Int, Int>.minus(pair: Pair<Int, Int>) = (first - pair.first) to (second - pair.second)
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

fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> = listOf(
    (first + 1) to second,
    (first - 1) to second,
    first to (second + 1),
    first to (second - 1)
)

fun <T, R> Pair<T, T>.map(lambda: (T) -> (R)): Pair<R, R> = lambda(first) to lambda(second)
fun <T, V, R> Pair<T, V>.mapFirst(lambda: (T) -> (R)): Pair<R, V> = lambda(first) to second
fun <T, V, R> Pair<V, T>.mapSecond(lambda: (T) -> (R)): Pair<V, R> = first to lambda(second)

fun <T> List<T>.takePair(): Pair<T, T> = this[0] to this[1]

fun <T> Pair<T, T>.forBoth(lambda: (T) -> Unit) {
    lambda(first)
    lambda(second)
}

fun <T, R> Triple<T, T, T>.map(lambda: (T) -> (R)): Triple<R, R, R> =
    Triple(lambda(first), lambda(second), lambda(third))

operator fun Pair<Int, Int>.rangeTo(to: Pair<Int, Int>) =
    (this.first autoRange to.first).flatMap { x -> (this.second autoRange to.second).map { x to it } }

fun <T, R> Pair<T, R>.swap() = this.second to this.first

fun Idx2D.manhattenTo(other: Idx2D) =
    abs(this.first - other.first) + abs(this.second - other.second)