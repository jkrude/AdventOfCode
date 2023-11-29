package y2021

import java.io.File

val testData =
    File("src/y2021.y2021.y2021.y2021.y2021.y2021.y2021.y2021.y2021.y2021.y2021.y2021.y2021.y2021.main/resources/day10.txt").readLines()

fun Char.isOpening() = this == '[' || this == '(' || this == '{' || this == '<'

fun getClosing(char: Char): Char {
    return when (char) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalArgumentException("Got: $char")
    }
}

fun errorCode(bracked: Char): Int {
    return when (bracked) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException("Got: $bracked")
    }
}

fun firstError(line: String): Char? {
    val expected: MutableList<Char> = ArrayList()
    for (bracket: Char in line) {
        if (bracket.isOpening()) {
            expected.add(getClosing(bracket))
        } else {
            if (expected.isEmpty() || bracket != expected.last()) {
                return bracket
            } else {
                if (expected.isNotEmpty()) expected.removeLast()
            }
        }
    }
    return null
}


fun partOne() {
    var sum = 0
    for (line in testData) {
        val expected: MutableList<Char> = ArrayList()
        for (bracket: Char in line) {
            if (bracket.isOpening()) {
                expected.add(getClosing(bracket))
            } else {
                if (expected.isEmpty() || bracket != expected.last()) {
                    sum += errorCode(bracket)
                    break
                } else {
                    if (expected.isNotEmpty()) expected.removeLast()
                }
            }
        }
    }
    println(sum)
}

fun pointTable(br: Char) =
    when (br) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw IllegalArgumentException("Got: $br")
    }

fun partTwo() {

    val incomplete = testData.filter { firstError(it) == null }
    println(incomplete)
    val scores: MutableList<Long> = ArrayList()
    val completion: MutableList<String> = ArrayList()
    for (line in incomplete) {
        val stack: MutableList<Char> = ArrayList()
        for (c in line) {
            if (c.isOpening()) stack.add(c)
            else stack.removeLast()
        }
        stack.reverse()
        completion.add(stack.map { getClosing(it) }.joinToString(separator = ""))
        scores.add(stack.map { pointTable(getClosing(it)).toLong() }.fold(0L) { acc, i -> acc * 5L + i })
    }
    println(scores)
    scores.sort()
    println(scores[scores.size / 2])
}

fun main() {
    partTwo()
}