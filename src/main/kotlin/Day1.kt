import java.io.FileNotFoundException

fun readReport(day: Int): List<String> = object {}.javaClass.getResource("day$day.txt")?.readText()?.split("\n") ?: throw FileNotFoundException()


class Day1 {

    fun partOne(input: List<String>): Int {
        return input.map(Integer::parseInt).windowed(2)
            .count { (a, b) -> a < b }
    }

    fun partTwo(input: List<String>): Int {
        return input
            .map(Integer::parseInt)
            .windowed(4)
            .count { it[0] < it[3] } //x_i + x_{i + 1} + x_{i+2} < x_{i+1} + x_{i+2} + x_{i+3} === x_i < x_{i+3}
    }

}

fun main() {
    println(Day1().partTwo(readReport(1)))
}