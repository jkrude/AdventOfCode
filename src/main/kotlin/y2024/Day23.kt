package y2024

import common.addEdgeMissingVertex
import common.readFileLines
import org.jgrapht.alg.clique.BronKerboschCliqueFinder
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph

object Day23 {

    fun <T> Map<T, List<T>>.triangles(node: T): List<Set<T>> {
        val triangleList = mutableListOf<Set<T>>()
        val neighbors: List<T> = this[node] ?: error("Node not part of graph $node")
        for (n1: T in neighbors) {
            for (n2: T in this[n1] ?: emptyList()) {
                val isTriangle: Boolean = this[n2]?.any { it == node } ?: false
                if (isTriangle) triangleList.add(setOf(node, n1, n2))
            }
        }
        return triangleList
    }

    fun partOne(lines: List<String>): Long {
        val connections: Map<String, MutableList<String>> = buildMap<String, MutableList<String>> {
            lines.forEach {
                val (from, to) = it.split(("-"))
                getOrPut(from) { mutableListOf() }.add(to)
                getOrPut(to) { mutableListOf() }.add(from)
            }
        }

        val triangles: Set<Set<String>> = connections.keys
            .filter { it.startsWith("t") }
            .flatMapTo(mutableSetOf()) { connections.triangles(it) }
        return triangles.size.toLong()
    }

    fun partTwo(lines: List<String>): String {
        val graph = DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        lines.forEach {
            val (from, to) = it.split("-")
            graph.addEdgeMissingVertex(from, to)
        }
        // If there is no maximum clique, our parsing code is wrong.
        val maxClique: Set<String> = BronKerboschCliqueFinder(graph).maximumIterator().next()
        // Sort lexicographically and return as joined-string.
        return maxClique
            .sorted()
            .joinToString(",")
    }
}

fun main() {
    val input = readFileLines(23, 2024)
    println(Day23.partOne(input))
    println(Day23.partTwo(input))
}