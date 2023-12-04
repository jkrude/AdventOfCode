package y2023

import common.extensions.allNeighbour
import common.extensions.product
import common.neighborsOf
import common.readFileLines
import common.toDirectedJGraph
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge

private fun String.isNum(): Boolean {
    return this.toIntOrNull() != null
}

object Day3 {

    data class Node(val symbol: String, val indices: List<Pair<Int, Int>>) {
        fun isNumber() = symbol.isNum()

        fun adjacentIn(lookupMap: Map<Pair<Int, Int>, Node>): List<Node> =
            indices.flatMap { it.allNeighbour() }
                .toSet()
                .mapNotNull { ij -> lookupMap[ij] }
    }


    private val regex = Regex("""(\d+|[^\.])""")
    private fun parseLine(rowIdx: Int, line: String, lookupMap: MutableMap<Pair<Int, Int>, Node>) {
        val symbolsToIndices: Sequence<Pair<String, IntRange>> = regex.findAll(line).map { it.value to it.range }
        val nodes: Sequence<Node> = symbolsToIndices.map { Node(it.first, it.second.map { j -> rowIdx to j }) }
        nodes.forEach { node -> node.indices.forEach { lookupMap[it] = node } }
    }


    private fun toGraph(lines: List<String>): DefaultDirectedGraph<Node, DefaultEdge> {
        val idx2dToNode = buildMap {
            lines.forEachIndexed { index, line -> parseLine(index, line, this) }
        }
        return idx2dToNode.values.associateWith { node -> node.adjacentIn(idx2dToNode) }.toDirectedJGraph()
    }


    fun partOne(lines: List<String>): Long {
        val graph = toGraph(lines)
        return graph.vertexSet()
            .filter { it.symbol.isNum() && graph.neighborsOf(it).any { neighbor -> !neighbor.symbol.isNum() } }
            .sumOf { it.symbol.toLong() }
    }

    fun partTwo(lines: List<String>): Long {
        val graph = toGraph(lines)
        return graph.vertexSet()
            .filter { it.symbol == "*" && graph.neighborsOf(it).count { n -> n.isNumber() } == 2 }
            .sumOf { graph.neighborsOf(it).map { neighbor -> neighbor.symbol.toLong() }.product() }
    }
}

fun main() {
    val input = readFileLines(3, 2023)
    println(Day3.partOne(input))
    println(Day3.partTwo(input))
}