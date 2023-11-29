package y2021

import common.readFileLines

class Day12 {

    companion object {

        private const val START = "start"
        private const val END = "end"

        fun importData(input: List<String> = readFileLines(1, 20212)): MutableMap<String, MutableList<String>> {
            val edges: List<Pair<String, String>> = input.map { it.split("-") }.map { it[0] to it[1] }
            val graph: MutableMap<String, MutableList<String>> = HashMap()
            for ((from, to) in edges) {
                graph.getOrPut(from) { ArrayList() }.add(to)
                graph.getOrPut(to) { ArrayList() }.add(from)
            }
            graph.forEach { (_, toList) -> toList.remove(START) }
            return graph
        }

        private fun Map<String, List<String>>.findPathRec(
            start: String,
            path: List<String>,
            timeForSmall: Boolean
        ): Int {
            var foundPaths = 0
            for (to in this[start]!!) {
                if (to == END) {
                    foundPaths++
                } else if (to.first().isUpperCase() || to !in path || timeForSmall) {
                    val timeForSmallNext = (to.first().isUpperCase() || to !in path) && timeForSmall
                    foundPaths += findPathRec(to, path + to, timeForSmallNext)
                }
            }
            return foundPaths
        }


        fun partOne(graph: Map<String, List<String>>): Int {
            return graph.findPathRec(START, listOf(START), false)
        }

        fun partTwo(graph: Map<String, List<String>>): Int {
            return graph.findPathRec(START, listOf(START), true)
        }

    }
}

fun main() {
    println("PART 1: ${Day12.partOne(Day12.importData())}")
    println("PART 2: ${Day12.partTwo(Day12.importData())}")
}