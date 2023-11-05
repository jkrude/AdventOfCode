package common

fun <T> List<List<T>>.indices2d() =
    this.indices.flatMap { i -> this[i].indices.map { j -> i to j } }

fun <T> Array<Array<T>>.indices2d() =
    this.indices.flatMap { i -> this[i].indices.map { j -> i to j } }

fun <X> List<Triple<X, X, X>>.unzip(): Triple<ArrayList<X>, ArrayList<X>, ArrayList<X>> {
    val (firstList, secondList, thirdList) = fold(
        listOf(
            ArrayList<X>(),
            ArrayList<X>(),
            ArrayList<X>()
        )
    ) { foldingList: List<ArrayList<X>>, triple: Triple<X, X, X> ->
        foldingList[0].add(triple.first)
        foldingList[1].add(triple.second)
        foldingList[2].add(triple.third)
        foldingList
    }
    return Triple(firstList, secondList, thirdList)
}

fun <T> List<List<T>>.getOrNull(ij: Pair<Int, Int>) = this.getOrNull(ij.first)?.getOrNull(ij.second)
fun <T, R> Pair<T, T>.map(lambda: (T) -> (R)): Pair<R, R> = lambda(first) to lambda(second)
fun <T, R> Triple<T, T, T>.map(lambda: (T) -> (R)): Triple<R, R, R> =
    Triple(lambda(first), lambda(second), lambda(third))

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

operator fun List<List<Int>>.get(ij: Pair<Int, Int>) = this[ij.first][ij.second]
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

fun <T> Collection<T>.plusElement(supplier: (Collection<T>) -> T): List<T> {
    return this.plusElement(supplier(this))
}