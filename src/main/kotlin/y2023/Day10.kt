package y2023

import common.algorithms.Search
import common.extensions.*
import common.extensions.Lists2D.containsIndex
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.readFileLines
import common.toDirectedJGraph
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import kotlin.math.ceil

object Day10 {

    private fun parse(list2D: List<List<Char>>): Pair<MutableMap<Idx2D, MutableList<Idx2D>>, Idx2D> {
        val graph = mutableMapOf<Idx2D, MutableList<Idx2D>>().withDefault { mutableListOf() }
        var start: Idx2D? = null

        fun Idx2D.connectedTo(other: Idx2D): Boolean {
            val (x, y) = this
            return when (val c = list2D[y][x]) {
                'S' -> true // possibly connected to all four
                '-' -> other == x - 1 to y || other == x + 1 to y
                '|' -> other == x to y - 1 || other == x to y + 1
                'L' -> other == x to y - 1 || other == x + 1 to y
                '7' -> other == x - 1 to y || other == x to y + 1
                'J' -> other == x to y - 1 || other == x - 1 to y
                'F' -> other == x to y + 1 || other == x + 1 to y
                '.' -> false
                else -> error(c)
            }
        }

        for (ji in list2D.indices2d()) {
            val ij = ji.swap()
            val c = list2D[ji]
            if (c == '.') continue
            val neighbors =
                ij.neighbours().filter { list2D containsIndex it.swap() && ij.connectedTo(it) && it.connectedTo(ij) }
            graph.getOrPut(ij, { mutableListOf() }).addAll(neighbors)
            if (c == 'S') start = ij
        }

        return graph to start!!
    }

    // Starting from the neighbours of start, in a depth first matter remove any edge leading backwards.
    // This drastically reduces the number of cycles but would not really be necessary.
    private fun makeOneWay(
        graph: MutableMap<Idx2D, MutableList<Idx2D>>,
        start: Idx2D
    ): DefaultDirectedGraph<Idx2D, DefaultEdge> {
        Search.startingFrom(graph[start]!!)
            .neighbors {
                graph.getValue(it).filter { n -> n != start }
                    .onEach { n -> graph.getValue(n).remove(it) }
            }.executeDfs()
        return graph.toDirectedJGraph()
    }

    private fun getCycle(graph: DefaultDirectedGraph<Pair<Int, Int>, DefaultEdge>): Set<Idx2D> {
        // IMPORTANT: Increase stack size or use TiernanSimpleCycles.
        return SzwarcfiterLauerSimpleCycles(graph).findSimpleCycles().maxBy { it.size }.toSet()
    }

    fun partOne(lines: List<String>): Int {
        val (graphUndirected, start) = parse(lines.toCharList2D())
        // Find the largest cycle. The furthest point away is the size of the cycle / 2 (rounded up).
        return ceil(getCycle(makeOneWay(graphUndirected, start)).size / 2.0).toInt()
    }

    fun partTwo(lines: List<String>): Int {
        val (graphUndirected, start) = parse(lines.toCharList2D())
        val graph = makeOneWay(graphUndirected, start)
        val cycleSet = getCycle(graph)

        fun determineSisBorder(position: Idx2D) =
            listOf(
                listOf(-1 to 0, 1 to 0),//'|'
                listOf(1 to 0, 0 to 1),//'F'
                listOf(-1 to 0, 0 to 1),//'7'
            ).any { letter -> letter.all { position + it in cycleSet } }

        var insideCounter = 0
        // Scanline algorithm: If we encounter a border switch inside and outside.
        for ((j, row) in lines.withIndex()) {
            var inside = false
            for ((i, c) in row.withIndex()) {
                if (i to j in cycleSet) {
                    if ((c == 'S' && determineSisBorder(i to j)) || c == '|' || c == 'F' || c == '7') inside = !inside
                } else if (inside) insideCounter++
            }
        }
        return insideCounter
    }
}

fun main() {
    // -Xss4m
    val input = readFileLines(10, 2023)
    println(Day10.partOne(input))
    println(Day10.partTwo(input))
}