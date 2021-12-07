import java.io.File
import java.io.FileNotFoundException
import kotlin.math.abs
import kotlin.math.round

class Day7 {

    companion object {

        fun readReport(): List<Int> =
            File("src/main/resources/day7.txt").readLines()[0].split(",").map { it.toInt() }
                ?: throw FileNotFoundException()

        private fun cost(data: List<Int>, pivot: Int) = data.sumOf { abs(pivot - it) }

        private fun weightedCost(data: List<Int>, pivot: Int) = data.map { abs(pivot - it) }.sumOf { n -> n * (n + 1) / 2 }

        private fun findOptimum(data: List<Int>, costs: (List<Int>, Int) -> Int): Int {
            // optimisation in 1D: Find x such that costs(data, x) is minimal
            fun costsUp(pivot: Int): Int = costs(data, pivot + 1)
            fun costsDown(pivot: Int): Int = costs(data, pivot - 1)
            var pivot = round(data.average()).toInt()
            val initialDirection = if (costsUp(pivot) < costs(data, pivot)) 1 else -1
            while (costsUp(pivot) < costs(data, pivot)
                || costsDown(pivot) < costs(data, pivot)
            ) {
                pivot += initialDirection
            }
            return costs(data, pivot).also { print(pivot) }
        }

        fun partOne(data: List<Int>): Int {
            return findOptimum(data, Day7::cost)
        }

        fun partTwo(data: List<Int>): Int {
            return findOptimum(data, Day7::weightedCost)
        }
    }
}

fun main() {
    println("Part 1: Cost: ${Day7.partOne(Day7.readReport())}")
    println("Part 2: Cost: ${Day7.partTwo(Day7.readReport())}")

}