package y2021

import common.extensions.Lists2D.getOrNull
import common.extensions.allNeighbour
import common.readFileLines

class Octopus(var energyLevel: Int) {

    var hasFlashed = false
    var flashed: Int = 0

    fun reset() {
        this.energyLevel = if (energyLevel > 9) 0 else energyLevel
        this.hasFlashed = false
    }

    fun inc(): Boolean {
        energyLevel++
        return (energyLevel > 9 && !hasFlashed).also {
            if (it) {
                hasFlashed = true
                flashed++
            }
        }
    }
}

class Day11 {

    companion object {

        fun parse(input: List<String> = readFileLines(1, 20211)) =
            input.map { it.map { c -> Octopus(c.digitToInt()) } }

        private fun <T> List<List<T>>.iterate2D(): List<Pair<Int, Int>> =
            this.indices.flatMap { i -> this[i].indices.map { j -> i to j } }

        private fun List<List<Octopus>>.update(idx2D: Pair<Int, Int>) {
            if (this.getOrNull(idx2D)?.inc() == true) {
                idx2D.allNeighbour().forEach { this.update(it) }
            }
        }

        fun partOne(octGrid: List<List<Octopus>>): Int {
            repeat(100) {
                octGrid.iterate2D().forEach { octGrid.update(it) }
                octGrid.flatten().forEach { it.reset() }
            }
            return octGrid.flatten().sumOf { it.flashed }
        }

        fun partTwo(octGrid: List<List<Octopus>>): Int {
            var iteration = 0
            do {
                octGrid.flatten().forEach { it.reset() }
                octGrid.iterate2D().forEach { octGrid.update(it) }
                iteration++

            } while (!(octGrid.flatten().all { it.hasFlashed }))
            return iteration
        }
    }
}

fun main() {
    println("PART 1: ${Day11.partOne(Day11.parse())}")
    println("PART 2: ${Day11.partTwo(Day11.parse())}")
}