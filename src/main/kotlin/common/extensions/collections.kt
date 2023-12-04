package common.extensions

import kotlin.experimental.ExperimentalTypeInference

fun List<String>.toCharList2D(): List<List<Char>> = this.map { it.toList() }
fun <T> Iterable<T>.onlyUnique(): Set<T> {
    val elementCounts: Map<T, Int> = this.groupingBy { it }.eachCount()
    return elementCounts.filterValues { it == 1 }.keys
}

fun <T> Collection<T>.plusElement(supplier: (Collection<T>) -> T): List<T> {
    return this.plusElement(supplier(this))
}

fun <T> MutableMap<T, Int>.inc(key: T, default: Int = 0) {
    this[key] = this.getOrDefault(key, default) + 1
}

fun <T> MutableMap<T, Int>.dec(key: T, default: Int = 0) {
    this[key] = this.getOrDefault(key, default) - 1
}

fun Iterable<Long>.product(): Long = this.reduce { acc, t -> acc * t }
fun Iterable<Int>.product(): Int = this.reduce { acc, t -> acc * t }

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(transform: (T) -> Long): Long = this.map(transform).reduce { acc, t -> acc * t }

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(transform: (T) -> Int): Int = this.map(transform).reduce { acc, t -> acc * t }
