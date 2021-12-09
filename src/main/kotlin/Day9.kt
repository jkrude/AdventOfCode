class Day9 {

    companion object {

        fun readReport(fileLines: List<String> = readFileLines(9)): List<List<Int>> =
            fileLines.map { it.map { c -> c.digitToInt() } }

        private fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> = listOf(
            (first + 1) to second,
            (first - 1) to second,
            first to (second + 1),
            first to (second - 1)
        )

        // local minimum if all neighbours are greater
        private fun List<List<Int>>.isLocalMin(ij: Pair<Int, Int>): Boolean =
            (ij).neighbours().all { this[ij.first][ij.second] < (this.getOrNull(it) ?: Int.MAX_VALUE) }

        private fun List<List<Int>>.getBasinSize(localMin: Pair<Int, Int>): Int {
            // idea of recursive flood fill with 4 neighbours
            fun getBasinSizeRec(ij: Pair<Int, Int>, comp: Int, visited: MutableSet<Pair<Int, Int>>): Int {
                val curr: Int = this.getOrNull(ij) ?: return 0
                if (ij in visited || curr == 9) return 0
                if (curr >= comp) {
                    visited.add(ij)
                    return 1 + (ij.neighbours().sumOf { getBasinSizeRec(it, curr, visited) })
                }
                return 0
            }

            val curr: Int = this.getOrNull(localMin) ?: return 0
            return getBasinSizeRec(localMin, curr, HashSet())
        }

        fun partOne(testData: List<List<Int>>): Int =
            testData.indices2d()
                .filter { testData.isLocalMin(it) }
                .sumOf { (i, j) -> testData[i][j] + 1 }

        fun partTwo(heightMap: List<List<Int>>): Int =
            heightMap.indices2d()
                .filter { heightMap.isLocalMin(it) }
                .map { heightMap.getBasinSize(it) }
                .sortedDescending().subList(0, 3)
                .reduce { acc, i -> acc * i }
    }
}

fun main() {
    println("PART 1: ${Day9.partOne(Day9.readReport())}")
    println("PART 2: ${Day9.partTwo(Day9.readReport())}")
}