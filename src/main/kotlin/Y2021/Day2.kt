import java.io.File

class Day2 {

    companion object {

        fun readReport(): List<Pair<String, Int>> =
            File("src/main/resources/day2.txt").readLines().map { it.split(" ") }.map { it[0] to it[1].toInt() }


        fun partOne(data: List<Pair<String, Int>>): Int =

            data.groupBy({ it.first }) { it.second }
                .mapValues { (_, values) -> values.sum() }
                .let { it.getValue("forward") * (it.getValue("down") - it.getValue("up")) }

        fun partTwo(data: List<Pair<String, Int>>): Int {
            var (aim, pos, dist) = listOf(0, 0, 0)
            for ((command, amount) in data) {
                when (command) {
                    "forward" -> {
                        pos += amount
                        dist += aim * amount
                    }

                    "up" -> aim -= amount
                    "down" -> aim += amount
                }
            }
            return pos * dist
        }
    }
}

fun main() {
    println("Part One ${Day2.partOne(Day2.readReport())}")
    println("Part Two ${Day2.partTwo(Day2.readReport())}")
}