import java.io.FileNotFoundException
import kotlin.math.abs

class Day5 {

    companion object {
        fun readReport(): List<String> = object {}.javaClass.getResource("Y2021/day5.txt")?.readText()
            ?.split("\n") ?: throw FileNotFoundException()

        fun convertReport(data: List<String>): List<Line> {
            val lines: MutableList<Line> = ArrayList()
            for (line in data) {
                val (from, to) = line.split(" -> ")
                val (fromX, fromY) = from.split(",").map { it.toInt() }
                val (toX, toY) = to.split(",").map { it.toInt() }
                lines.add(Line(fromX, fromY, toX, toY))
            }
            return lines
        }

        fun partOne(lines: List<Line>): Int {
            val map: Array<Array<Int>> = Array(1000) { Array(1000) { 0 } }
            lines.map { it.getSegment() }.flatten().forEach { (x, y) -> map[x][y]++ }
            return map.flatten().count { it > 1 }
        }
    }

}

data class Line(val fromX: Int, val fromY: Int, val toX: Int, val toY: Int) {

    fun getSegment(): List<Pair<Int, Int>> {
        return when {
            (fromX == toX) -> (0..abs(fromY - toY)).map { fromX to (minOf(fromY, toY) + it) }
            (fromY == toY) -> (0..abs(fromX - toX)).map { (minOf(fromX, toX) + it) to fromY }
            else -> {
                val signX = if (fromX > toX) -1 else 1 // if toX < fromX range has to go down
                val signY = if (fromY > toY) -1 else 1
                return (0..abs(fromX - toX)).map { fromX + (it * signX) to (fromY) + (it * signY) }
            }
        }
    }
}

fun main() {
    println(Day5.partOne(Day5.convertReport((Day5.readReport()))))
}