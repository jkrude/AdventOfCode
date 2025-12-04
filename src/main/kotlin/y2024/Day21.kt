package y2024

import common.algorithms.permutations
import common.extensions.Idx2D
import common.extensions.minus
import common.readFileLines
import kotlin.math.abs

object Day21 {
    interface KeyPad {
        val gap: Idx2D
        val codeToIdx2D: Map<Char, Idx2D>
        val pathCache: MutableMap<Pair<Idx2D, Idx2D>, List<List<Char>>>

        fun getValidPaths(start: Idx2D, end: Idx2D): List<List<Char>> {
            pathCache[start to end]?.let { return it } // return the cached result
            val delta: Pair<Int, Int> = end - start
            if (delta.first == 0 && delta.second == 0) {
                // return a path containing no moves
                return listOf(listOf<Char>()).also { pathCache[start to end] = it }
            }

            val moves = MutableList(abs(delta.first)) {
                if (delta.first < 0) '^' else 'v'
            }
            repeat(abs(delta.second)) {
                moves.add(if (delta.second < 0) '<' else '>')
            }

            // Generate all unique permutations of the required moves.
            val validPaths: List<List<Char>> = permutations(moves.size)
                .map { indices -> indices.map { moves[it] } }
                .filter { charPath: List<Char> -> // Check that the path does not hit the gap.
                    var (row, column) = start
                    for (move in charPath) {
                        when (move) {
                            '^' -> row--
                            'v' -> row++
                            '<' -> column--
                            '>' -> column++
                        }
                        if (row == this.gap.first && column == this.gap.second) {
                            return@filter false
                        }
                    }
                    return@filter true
                }.toList()
            require(validPaths.isNotEmpty(), { "Could not find a valid path from $start to $end with gap $gap" })
            pathCache[start to end] = validPaths
            return validPaths
        }
    }

    object NumericKeyboard : KeyPad {
        override val gap: Idx2D = (3 to 0)
        override val pathCache: MutableMap<Pair<Idx2D, Idx2D>, List<List<Char>>> = mutableMapOf()
        override val codeToIdx2D = mapOf(
            '7' to (0 to 0), '8' to (0 to 1), '9' to (0 to 2),
            '4' to (1 to 0), '5' to (1 to 1), '6' to (1 to 2),
            '1' to (2 to 0), '2' to (2 to 1), '3' to (2 to 2),
            '0' to (3 to 1), 'A' to (3 to 2)
        )
    }

    object DirectionalKeyboard : KeyPad {
        override val gap: Idx2D = (0 to 0)
        override val pathCache: MutableMap<Pair<Idx2D, Idx2D>, List<List<Char>>> = mutableMapOf()
        override val codeToIdx2D = mapOf(
            '^' to (0 to 1), 'A' to (0 to 2),
            '<' to (1 to 0), 'v' to (1 to 1), '>' to (1 to 2)
        )
    }

    class KeypadSolver(private val numRobots: Int = 2) {

        val minCostCache = mutableMapOf<Triple<Int, Char, Char>, Long>()
        fun solveCode(code: String): Long =
            // Level 0 is the numeric keypad
            ("A$code").toList() // We always start on A.
                .windowed(2) // The end character becomes the next start.
                .sumOf { (start, end) ->
                    getMinCost(0, start, end)
                }

        /**
         * Find the sequence of button presses that minimizes total moves.
         * Solved by using dynamic programming, having the hierarchy-depth, start and end character as keys.
         * Each sequence at depth N depends on the sequence of N+1.
         * Except for the last depth (numRobots) which is the door keypad.
         */
        private fun getMinCost(depth: Int, start: Char, end: Char): Long {
            val key = Triple(depth, start, end)
            minCostCache[key]?.let { return it }

            val keypad = if (depth == 0) NumericKeyboard else DirectionalKeyboard
            val startPos = keypad.codeToIdx2D[start]!!
            val endPos = keypad.codeToIdx2D[end]!!

            // Get all valid Manhattan paths (sequences of <,>,^,v)
            val paths = keypad.getValidPaths(startPos, endPos)

            return paths.minOf { path ->
                // If this is the last robot (controlled by User), the cost is just the length of the sequence
                if (depth == numRobots) path.size.toLong() + 1L // +1 for the final 'A' press
                else {
                    // The controlling robot always starts at A
                    (listOf('A') + path + listOf('A')).windowed(2)
                        .sumOf { (current, next) ->
                            getMinCost(depth + 1, current, next)
                        }
                }
            }.also { minCostCache[key] = it }
        }
    }

    fun solve(input: List<String>, solver: KeypadSolver): Long =
        input.sumOf { code ->
            val numericPart: Long = code.substringBefore('A').toLong() // "029A" -> 29
            val sequenceLength: Long = solver.solveCode(code)
            sequenceLength * numericPart
        }

    fun partOne(lines: List<String>): Long = solve(lines, KeypadSolver(2))
    fun partTwo(lines: List<String>): Long = solve(lines, KeypadSolver(25))
}

fun main() {
    val input = readFileLines(21, 2024)
    println(Day21.partOne(input))
    println(Day21.partTwo(input))
}