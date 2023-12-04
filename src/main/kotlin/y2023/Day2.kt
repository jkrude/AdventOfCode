package y2023

import common.extensions.List2D
import common.extensions.productOf
import common.readFileLines

object Day2 {
    private data class Game(val id: Int, val subsets: List2D<Reveal>)
    private data class Reveal(val amount: Int, val color: String)

    //Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    private fun parse(line: String): Game {
        fun parseSubset(subset: String) =
            subset.split(", ").map { it.split(" ").let { (nbr, color) -> Reveal(nbr.toInt(), color) } }

        val (game, revealed) = line.split(": ")
        val sets = revealed.split("; ")
        val subsets: List2D<Reveal> = sets.map { parseSubset(it) }
        val gameId = game.split(" ").last().toInt()
        return Game(gameId, subsets)
    }

    fun partOne(lines: List<String>): Int {
        // only 12 red cubes, 13 green cubes, and 14 blue cubes
        return lines.map { parse(it) }.filter { game ->
            game.subsets.flatten().all { (amount, color) ->
                when (color) {
                    "red" -> amount <= 12
                    "green" -> amount <= 13
                    "blue" -> amount <= 14
                    else -> error(color)
                }
            }
        }.sumOf { it.id }
    }

    fun partTwo(lines: List<String>): Long {

        fun powerOfSubset(set: List2D<Reveal>): Long =
            set.flatten()
                .groupBy { it.color }
                .mapValues { (_, values) -> values.maxOf { it.amount } }
                .values.productOf(Int::toLong)

        return lines.map { parse(it) }.sumOf { game ->
            powerOfSubset(game.subsets)
        }
    }
}

fun main() {
    val input = readFileLines(2, 2023)
    println(Day2.partOne(input))
    println(Day2.partTwo(input))
}