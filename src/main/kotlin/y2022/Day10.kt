package y2022

import common.readFileLines

class Day10 {
    companion object {

        fun partOne(instructions: List<String>): Int {
            val counter = Counter(instructions)
            var sum = 0
            for (i in 1..230) {
                counter.nextCycle()
                if (i < 230 && (i - 20) % 40 == 0) {
                    sum += i * counter.value
                }
            }
            return sum
        }

        class Counter(
            instructions: List<String>,
            private val iter: Iterator<String> = instructions.iterator()
        ) {
            var value = 1
            private var amt = 0
            private var coolDown = 0

            private fun nextInstruction() = if (iter.hasNext()) iter.next() else "noop"
            var currentCycle = 1

            fun nextCycle() {
                currentCycle++
                if (coolDown == 0) {
                    value += amt
                    amt = 0
                    parseInstruction(nextInstruction())
                } else coolDown--
            }

            private fun parseInstruction(instruction: String) {
                if (instruction != "noop") {
                    amt = instruction.substringAfter("addx ").toInt()
                    coolDown = 1
                }
            }
        }

        fun partTwo(instructions: List<String>) {
            var spritePosition = 1
            val display = Array(6) { Array(40) { " " } }
            var displayCursor = 0
            var displayRow = 0

            val counter = Counter(instructions)
            fun drawPixel() {
                if ((spritePosition - displayCursor) in (-1..1)) {
                    display[displayRow][displayCursor] = "#"
                }
            }

            fun updateCursor() {
                displayCursor = (displayCursor + 1) % 40
                if (displayCursor == 0) displayRow++
            }

            while (!(displayRow == 5 && displayCursor == 39)) {
                counter.nextCycle()
                spritePosition = counter.value
                drawPixel()
                updateCursor()
            }
            println(display.joinToString(separator = "\n") {
                it.joinToString(separator = "")
            })
        }
    }
}

fun main() {
    println(Day10.partOne(readFileLines(1, 20220)))
    Day10.partTwo(readFileLines(1, 20220))
}