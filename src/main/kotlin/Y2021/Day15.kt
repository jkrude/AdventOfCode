class Day15 {

    companion object {

        fun parse(fileLines: List<String> = readFileLines(15)) =
            fileLines.map { line -> line.map { it.digitToInt() } }

        fun partOne(map: List<List<Int>>): Int {

            val end = map.size - 1 to map.first().size - 1
            fun findPathRec(path: List<Pair<Int, Int>>, currSum: Int, bestSumIn: Int): Int {
                var bestSum = bestSumIn
                val curr = path.last()
                for (next in listOf((curr.first + 1) to curr.second, curr.first to (curr.second + 1))) {
                    val nextRisk = map.getOrNull(next) ?: continue
                    if (next == end) return currSum + nextRisk
                    if (next !in path && currSum + nextRisk < bestSum) {
                        val pathSum = findPathRec(path + next, currSum + nextRisk, bestSum)
                        if (pathSum < bestSum) {
                            bestSum = pathSum
                        }
                    }
                }
                return bestSum
            }

            return findPathRec(listOf(0 to 0), 0, Int.MAX_VALUE)

        }
    }
}

fun main() {
    println(Day15.partOne(Day15.parse()))
}