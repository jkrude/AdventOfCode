package y2021

import common.map
import common.readFile

class Day13 {

    companion object {

        fun importData(
            fileText: String = readFile(
                13,
                2021
            ).readText()
        ): Pair<List<Pair<Int, Int>>, List<Pair<String, Int>>> {
            val (dotsInterim, foldsInterim) = fileText.split("\n\n").map { it.split("\n") }
            val dots = dotsInterim.map { it.split(",") }.map { it[0].toInt() to it[1].toInt() }
            val regex = """([xy]=\d+)""".toRegex()
            val folds = foldsInterim.map { regex.find(it)?.value ?: throw IllegalArgumentException() }
                .map { it.split("=") }
                .map { it[0] to it[1].toInt() }
            return dots to folds
        }

        fun partOne(dotsInput: List<Pair<Int, Int>>, folds: List<Pair<String, Int>>): Int {
            var dots = dotsInput
            val (direction, line) = folds.first()
            when (direction) {
                "y" -> dots = dots.map { (x, y) -> if (y < line) (x to y) else x to (2 * line - y) }.distinct()
                "x" -> dots = dots.map { (x, y) -> if (x < line) (x to y) else (2 * line - x) to y }.distinct()
            }
            return dots.count()
        }

        fun partTwo(dots: List<Pair<Int, Int>>, folds: List<Pair<String, Int>>): Set<Pair<Int, Int>> {

            fun List<Int>.apply(x: Int) = this.fold(x) { acc, i -> if (acc > i) 2 * i - acc else acc }

            val (xFold, yFold) = folds.partition { it.first == "x" }
                .map { it.map { (_, foldLine) -> foldLine } }

            return dots.map { (x, y) -> xFold.apply(x) to yFold.apply(y) }.toSet()
        }

        fun partTwoProjected(dots: List<Pair<Int, Int>>, folds: List<Pair<String, Int>>): Set<Pair<Int, Int>> {

            // Directly project point to final destination
            fun Int.project(f: Int) = if (this % ((f + 1) * 2) > (f)) (f - 1) - this % (f + 1) else this % (f + 1)
            // Formula if every folding edge remains in the space
            //fun Int.project(f: Int) = if (this % (f * 2) > f) f - this % f else this % f
            val (foldLastX, foldLastY) = folds.partition { it.first == "x" }.map { it.minOf { (_, y) -> y } }
            return dots.map { (x, y) -> x.project(foldLastX) to y.project(foldLastY) }.toSet()

        }


        fun printGrid(dots: Set<Pair<Int, Int>>) {
            val xMax = dots.maxOf { (x, _) -> x } + 1
            val yMax = dots.maxOf { (_, y) -> y } + 1
            val asGrid = Array(yMax) { Array(xMax) { '.' } }
            println("Dimensions=$xMax, $yMax")
            dots.forEach { (x, y) -> asGrid[y][x] = '#' }
            for (row in asGrid) {
                println(row.joinToString(separator = ""))
            }
            println()
        }
    }
}


fun main() {

    val (dots, folds) = Day13.importData()
    println("PART 1: ${Day13.partOne(dots, folds)}")
    Day13.printGrid(Day13.partTwo(dots, folds))
    Day13.printGrid(Day13.partTwoProjected(dots, folds))

}