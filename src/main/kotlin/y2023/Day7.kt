package y2023

import common.readFileLines

object Day7 {

    private fun String.frequencies(): Map<Char, Int> = this.toSet().associateWith { this.count { c -> c == it } }

    private enum class HandType { High, Pair, TwoPair, Three, FullHouse, Four, Five }

    private open class Hand(val cards: String, val bid: Int) : Comparable<Hand> {
        protected open val type = valueOf(cards)

        protected fun valueOf(hand: String): HandType {
            val frequencies = hand.frequencies().values
            return when {
                5 in frequencies -> HandType.Five
                4 in frequencies -> HandType.Four
                3 in frequencies && 2 in frequencies -> HandType.FullHouse
                3 in frequencies -> HandType.Three
                frequencies.count { it == 2 } == 2 -> HandType.TwoPair
                2 in frequencies -> HandType.Pair
                else -> HandType.High
            }
        }

        protected open fun numeric(char: Char) =
            when (char) {
                'T' -> 10
                'J' -> 11
                'Q' -> 12
                'K' -> 13
                'A' -> 14
                else -> char.digitToInt()
            }

        override fun compareTo(other: Hand): Int {
            if (type != other.type) return type.ordinal - other.type.ordinal
            for (i in 0..4) {
                (numeric(cards[i]) - numeric(other.cards[i])).let { if (it != 0) return it }
            }
            return 0
        }
    }

    private class JokerHand(cards: String, bid: Int) : Hand(cards, bid) {
        override val type: HandType = valueOfJoker()

        private fun valueOfJoker(): HandType {
            if (cards.all { it == 'J' }) return HandType.Five
            val highest = cards.replace("J", "").frequencies().maxBy { it.value }
            // It's always best to add jokers to the card with the highest frequency
            return valueOf(cards.replace('J', highest.key))
        }

        override fun numeric(char: Char): Int = super.numeric(char).let { return if (it == 11) 1 else it }

    }

    private fun winnings(lines: List<String>, constructor: (String, Int) -> Hand) =
        lines.map { line -> line.split(" ").let { constructor(it[0], it[1].toInt()) } }
            .sorted()
            .withIndex()
            .sumOf { (index, hand) -> (index + 1L) * hand.bid }

    fun partOne(lines: List<String>): Long = winnings(lines, ::Hand)

    fun partTwo(lines: List<String>): Long = winnings(lines, ::JokerHand)
}


fun main() {
    val input = readFileLines(7, 2023)
    println(Day7.partOne(input))
    println(Day7.partTwo(input))
}