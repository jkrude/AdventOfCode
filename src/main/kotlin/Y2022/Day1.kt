package Y2022

import readFileLines

fun maxNCalories(lines: List<String>, n: Int): Int {
    val highestCounts: Array<Int> = Array(n) { 0 }
    var currentCount = 0
    for ((idx, line) in lines.withIndex()) {
        if (line != "") {
            currentCount += line.toInt()
            if (idx != lines.lastIndex) continue
        }
        val loser = highestCounts.minOrNull() ?: throw Exception()
        val idxLoser = highestCounts.indexOf(loser)
        if (loser != -1) {
            highestCounts[idxLoser] = currentCount
        }
        currentCount = 0

    }
    return highestCounts.sum()
}

fun bestN(lines: List<String>, n: Int): Int {
    val highestCounts: Array<Int> = Array(n) { 0 }
    var currentCount = 0
    for ((idx, line) in lines.withIndex()) {
        if (line != "") {
            currentCount += line.toInt()
            if (idx != lines.lastIndex) continue
        }
        if (currentCount > highestCounts[0]) {
            highestCounts[0] = currentCount
            highestCounts.sort()
        }
        currentCount = 0
    }
    return highestCounts.sum()
}

fun main() {
    val lines: List<String> = readFileLines(1)
    println(maxNCalories(lines, 1))
    println(bestN(lines, 3))
}

