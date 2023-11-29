package y2021

import common.readFileLines

class Day6 {
    companion object {

        fun readReport(fileLines: List<String> = readFileLines(6, 2021)): List<Int> =
            fileLines.first().split(",").map { it.toInt() }

        fun partOne(data: List<Int>): Int {
            // Naive implementation as task suggests.
            val fishies = data.toMutableList()
            repeat(80) {
                for (fishIdx in fishies.indices) {
                    fishies[fishIdx] = if (fishies[fishIdx] == 0) 6.also { fishies.add(8) } else fishies[fishIdx] - 1
                }
            }
            return fishies.size
        }

        fun partTwo(initialPopulation: List<Int>): Long {
            // Position in array == day until reproduction
            // Each day the dayUntilReproduce decreases by one -> move all - 1 -> or move idx + 1.
            // Use ring-buffer where idx are all fish with dayUntilReproduction == 0..
            // reproduction means there are fishies[idx] more fish for dayUntilReproduce==7
            val fishies: Array<Long> = Array(9) { 0L }
            initialPopulation.groupBy { it }.forEach { (key, value) -> fishies[key] = value.size.toLong() }
            var idx = 0
            repeat(256) {
                fishies[(idx + 7) % 9] += fishies[idx]
                idx = (idx + 1) % 9
            }
            return fishies.sum()
        }
    }
}

fun main() {
    val data = Day6.readReport()
    //println(d.y2021.partOne(data))
    println(Day6.partTwo(data))
}
