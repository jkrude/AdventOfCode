package y2022

import common.readFileLines

//total score is the sum of your scores
/**
 *
 * A for Rock, B for Paper, and C for Scissors
 * X for Rock, Y for Paper, and Z for Scissors
 *
 * score for the shape you selected
 *  1 for Rock,
 *  2 for Paper,
 *  3 for Scissors
 * outcome of round (0 lost, 3 draw, 6 win)
 */

fun valueOf(symbol: String): Int =
    when (symbol) {
        "X" -> 1
        "Y" -> 2
        "Z" -> 3
        else -> throw IllegalArgumentException()
    }

fun toRing(symbol: String): Int =
    when (symbol) {
        "X", "A" -> 0
        "Y", "B" -> 1
        "Z", "C" -> 2
        else -> throw IllegalArgumentException()
    }

fun fromRing(symbol: Int): String =
    when (symbol) {
        0 -> "X"
        1 -> "Y"
        2 -> "Z"
        else -> throw IllegalArgumentException()
    }

fun result(opponent: Int, you: Int): Int {
    return when (you) {
        opponent -> 3
        (opponent + 1) % 3 -> 6
        else -> 0
    }
}

fun followStrategy(lines: List<String>): Int {

    return lines.sumOf {
        val (opponent, you) = it.split(" ")
        val resultPoints = result(toRing(opponent), toRing(you))
        val playPoints = valueOf(you)
        println("$resultPoints + $playPoints")
        resultPoints + playPoints
    }
}

// X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win
fun chooseResponse(opponent: Int, result: String): Int {
    return when (result) {
        "X" -> Math.floorMod(opponent - 1, 3) // lose
        "Y" -> opponent // draw
        "Z" -> (opponent + 1) % 3 // win
        else -> throw IllegalArgumentException()
    }
}

fun figureOutStrategy(lines: List<String>): Int {
    return lines.sumOf {
        val (opponent, you) = it.split(" ")
        val response = fromRing(chooseResponse(toRing(opponent), you))
        val resultPoints = result(toRing(opponent), toRing(response))
        val playPoints = valueOf(response)
        println("$resultPoints + $playPoints")
        resultPoints + playPoints
    }
}

fun main() {
    println(followStrategy(readFileLines(2, 2022)))
    println(figureOutStrategy(readFileLines(2, 2022)))
}