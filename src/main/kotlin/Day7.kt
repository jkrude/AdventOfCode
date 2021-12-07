import java.io.File
import java.io.FileNotFoundException
import kotlin.math.abs
import kotlin.math.ceil

class Day7 {

    companion object {

        fun readReport(): List<Int> =
            File("src/main/resources/day7.txt").readLines()[0].split(",").map { it.toInt() }
                ?: throw FileNotFoundException()

        // cost optimum (minima)  = median
        fun partOne(data: List<Int>): Int =
            data.sumOf { abs(it - data.sorted()[data.size / 2]) }

        // cost optimum (minima)  = ceiled average
        fun partTwo(data: List<Int>): Int =
            data.map { abs(it - ceil(data.average())) }.sumOf { it * (it + 1) / 2 }.toInt()
    }
}

fun main() {
    println("Part 1: Cost: ${Day7.partOne(Day7.readReport())}")
    println("Part 2: Cost: ${Day7.partTwo(Day7.readReport())}")

}