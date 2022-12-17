package Y2022

import readFileText


class Day5 {
    companion object {
        fun splitInput(fileText: String): Pair<Array<MutableList<Char>>, MutableList<Pair<Int, Pair<Int, Int>>>> {
            val (stacksString, movesString) = fileText.split("\n\n")
            val stackLines = stacksString.lines()
            val numberOfStacks = stackLines.last().split(" ").last().toInt()
            val stacks = Array<MutableList<Char>>(numberOfStacks) { mutableListOf() }

            for (line in stackLines.reversed().drop(1)) {
                for ((idx, char) in line.withIndex()) {
                    if (char == '[') stacks[idx / 4].add(line[idx + 1])
                }
            }
            val moves = mutableListOf<Pair<Int, Pair<Int, Int>>>()
            val pattern = """move (\d+) from (\d+) to (\d+)""".toRegex()
            for (line in movesString.lines()) {
                val (amt, from, destination) = pattern.find(line)!!.groupValues.drop(1).map { it.toInt() }
                moves.add(amt to (from - 1 to destination - 1)) // start by index 0
            }
            return stacks to moves
        }

        private fun moveCrates9000(
            stacks: Array<MutableList<Char>>,
            amt: Int, fromTo: Pair<Int, Int>
        ) {
            val (from, dest) = fromTo
            repeat(amt) {
                stacks[dest].add(stacks[from].removeLast()) // Move one by one
            }
        }

        private fun moveCrates9001(
            stacks: Array<MutableList<Char>>,
            amt: Int, fromTo: Pair<Int, Int>
        ) {
            val (from, dest) = fromTo
            stacks[dest].addAll(stacks[from].takeLast(amt)) // move at once
            repeat(amt) {
                stacks[from].removeLast() // remove them
            }
        }

        private fun moveCrates(
            fileText: String, crane: (
                stacks: Array<MutableList<Char>>,
                amt: Int, fromTo: Pair<Int, Int>
            ) -> Unit
        ): String {
            val (stacks, moves) = splitInput(fileText)
            for ((amt, fromTo) in moves) {
                crane(stacks, amt, fromTo)
            }
            return stacks.joinToString(separator = "") { it.last().toString() }
        }

        fun partOne(fileText: String) = moveCrates(fileText, ::moveCrates9000)
        fun partTwo(fileText: String) = moveCrates(fileText, ::moveCrates9001)

    }
}

fun main() {
    println(Day5.partOne(readFileText(5)))
    println(Day5.partTwo(readFileText(5)))
}