package y2021

import common.readFileLines

class Day8 {

    companion object {

        // groupedSignals: length -> signals of that length
        // toBeDecoded: all words as set of char
        // completeData: groupedSignals zip toBeDecoded
        fun readReport(
            fileLines: List<String> = readFileLines(
                8,
                2021
            )
        ): List<Pair<Map<Int, Set<Set<Char>>>, List<Set<Char>>>> {
            val signals: MutableList<Map<Int, Set<Set<Char>>>> = ArrayList()
            val toBeDecoded: MutableList<List<Set<Char>>> = ArrayList()
            for (line in fileLines) {
                val (sig: String, dec: String) = line.split(" | ")
                val sigMap = sig.split(" ")
                    .map { it.toSet() } // convert to list of set of chars
                    .groupBy { it.size }
                    .mapValuesTo(mutableMapOf()) { it.value.toSet() }
                signals.add(sigMap)
                toBeDecoded.add(dec.split(" ").map { it.toSet() })
            }
            return signals zip toBeDecoded
        }

        fun partOne(data: List<Pair<Map<Int, Set<Set<Char>>>, List<Set<Char>>>>): Int =
            data.map { it.second }
                .sumOf { words -> words.count { it.size in setOf(2, 4, 3, 7) } }


        private fun <T> Iterable<Set<T>>.bigCap(): Set<T> =
            this.reduce { acc, chars -> acc intersect chars }


        fun partTwo(completeData: List<Pair<Map<Int, Set<Set<Char>>>, List<Set<Char>>>>): Int {

            //Only 3 has 4 char in common with the other elements of group 5
            //All elements in group 6 have {a,b,f,g} in common -> Out of {2,5} only 2 has a f -> |intersection == 4|
            fun id5(word: Set<Char>, signals: Map<Int, Set<Set<Char>>>): Int {
                if ((signals[5]!!.minus(element = word).all { (it intersect word).size == 4 })) return 3
                if ((word intersect signals[6]!!.bigCap()).size == 4) return 5
                return 2
            }

            // 2 is represented by {c,f} only 6 of group[6] does not contain both
            //All elements in group 5 have {a,d,g} in common -> Out of {9,0} only 9 has a d -> |intersection == 3|
            fun id6(word: Set<Char>, signals: Map<Int, Set<Set<Char>>>): Int {
                if (signals[2]!!.first().any { it !in word }) return 6
                if ((word intersect signals[5]!!.bigCap()).size == 3) return 9
                return 0
            }

            fun dec(word: Set<Char>, signals: Map<Int, Set<Set<Char>>>): Int =
                when (word.size) {
                    2 -> 1
                    3 -> 7
                    4 -> 4
                    5 -> id5(word, signals)
                    6 -> id6(word, signals)
                    7 -> 8
                    else -> throw Exception("words should not be longer than 7")
                }

            return completeData.sumOf { (signals, toBeDecoded) ->
                toBeDecoded
                    .map { dec(it, signals) } // list of decoded integer
                    .joinToString(separator = "")
                    .toInt()
            }
        }
    }
}


fun main() {
    println(Day8.partTwo(Day8.readReport()))
}