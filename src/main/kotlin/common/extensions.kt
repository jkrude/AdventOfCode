package common


fun List<String>.toCharList2D(): List<List<Char>> = this.map { it.toList() }

fun <T, R> Pair<T, T>.map(lambda: (T) -> (R)): Pair<R, R> = lambda(first) to lambda(second)
fun <T, R> Triple<T, T, T>.map(lambda: (T) -> (R)): Triple<R, R, R> =
    Triple(lambda(first), lambda(second), lambda(third))

inline fun Boolean.runIfTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

fun <T> Iterable<T>.onlyUnique(): Set<T> {
    val elementCounts: Map<T, Int> = this.groupingBy { it }.eachCount()
    return elementCounts.filterValues { it == 1 }.keys
}

infix fun Int.autoRange(to: Int) = if (this < to) this..to else this downTo to
operator fun Pair<Int, Int>.rangeTo(to: Pair<Int, Int>) =
    (this.first autoRange to.first).flatMap { x -> (this.second autoRange to.second).map { x to it } }

fun IntRange.extendedBy(symmetric: Int): IntRange {
    require(symmetric > 0)
    return (start - symmetric)..(endInclusive + symmetric)
}

fun IntRange.extendedBy(lower: Int, upper: Int): IntRange {
    require(lower > 0 && upper > 0)
    return start - lower..endInclusive + upper
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