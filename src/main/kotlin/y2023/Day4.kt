package y2023

import common.extensions.findGroupValues
import common.extensions.map
import common.readFileLines

object Day4 {

    data class Card(val id: Int, val winning: List<Int>, val your: List<Int>) {
        val yourWinningNumbers = your.count { it in this.winning }
        val points: Long =
            if (yourWinningNumbers == 0) 0L
            else 1L.shl(yourWinningNumbers - 1)
    }

    private val r = Regex("""Card\s+(\d+): ([\d\s]+) \| ([\d\s]+)""")
    private val nbrReg = Regex("""(\d+)""")

    private fun toCard(line: String): Card {
        val (id, winningStr, yoursStr) = r.findGroupValues(line)
        val (winning, yours) = (winningStr to yoursStr).map { numberStr ->
            nbrReg.findAll(numberStr).map { it.value.toInt() }.toList()
        }
        return Card(id.toInt(), winning, yours)
    }

    fun partOne(lines: List<String>) = lines.map { toCard(it) }.sumOf { it.points }

    fun partTwo(lines: List<String>): Long {
        val copiesOfCard = LongArray(lines.size) { 1L } // copies of the card with id-1

        lines.map { toCard(it) }.forEach { card ->
            (card.id + 1..card.id + card.yourWinningNumbers).forEach { cardCopyId ->
                if (cardCopyId <= copiesOfCard.size) copiesOfCard[cardCopyId - 1] += copiesOfCard[card.id - 1]
            }
        }
        return copiesOfCard.sum()
    }
}

fun main() {
    val input = readFileLines(4, 2023)
    println(Day4.partOne(input))
    println(Day4.partTwo(input))
}