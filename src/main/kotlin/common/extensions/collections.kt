package common.extensions

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