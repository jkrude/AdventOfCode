import java.io.File

fun <T> List<List<T>>.indices2d() =
    this.indices.flatMap { i -> this[i].indices.map { j -> i to j } }

fun readFileLines(day: Int) = File("src/main/resources/day$day.txt").readLines()

fun List<List<Int>>.getOrNull(ij: Pair<Int, Int>) = this.getOrNull(ij.first)?.getOrNull(ij.second)