package common.extensions

import java.util.*
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

fun <K, V> MutableMap<K, MutableList<V>>.addTo(key: K, value: V) {
    this.getOrPut(key) { mutableListOf() }.add(value)
}

fun <T, K, V> Iterable<T>.associateToList(transform: (T) -> Pair<K, V>): MutableMap<K, MutableList<V>> {
    val map = mutableMapOf<K, MutableList<V>>()
    this.forEach { item ->
        val (key, value) = transform(item)
        map.addTo(key, value)
    }
    return map
}


fun Iterable<Long>.product(): Long = this.reduce { acc, t -> acc * t }
fun Iterable<Int>.product(): Int = this.reduce { acc, t -> acc * t }

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(transform: (T) -> Long): Long = this.map(transform).reduce { acc, t -> acc * t }

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
fun <T> Iterable<T>.productOf(transform: (T) -> Int): Int = this.map(transform).reduce { acc, t -> acc * t }

/**
 * Collect the top k elements form an iterable.
 * The elements are returned ascending if used as min-heap and defencing if used with max-heap.
 */
fun <T> Iterable<T>.collectTopK(k: Int, heapComparator: Comparator<T>): Collection<T> {
    if (k <= 0) return emptyList()

    val heap = PriorityQueue(k, heapComparator)

    for (item in this) {
        if (heap.size < k) {
            heap.add(item)
        } else if (heapComparator.compare(item, heap.peek()) > 0) {
            heap.poll()
            heap.add(item)
        }
    }
    return heap
}

fun <T : Comparable<T>> Iterable<T>.takeMinK(k: Int): List<T> {
    return collectTopK(k, Collections.reverseOrder()).sorted()
}

fun <T : Comparable<T>> Iterable<T>.takeMaxK(k: Int): List<T> {
    return collectTopK(k, naturalOrder()).sortedDescending()
}

inline fun <T, R : Comparable<R>> Iterable<T>.takeMinKBy(k: Int, crossinline selector: (T) -> R): List<T> {
    return collectTopK(k, compareByDescending(selector)).sortedBy(selector)
}

inline fun <T, R : Comparable<R>> Iterable<T>.takeMaxKBy(k: Int, crossinline selector: (T) -> R): List<T> {
    return collectTopK(k, compareBy(selector)).sortedByDescending(selector)
}