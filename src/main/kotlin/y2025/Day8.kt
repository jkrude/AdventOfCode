package y2025

import common.addEdgeMissingVertex
import common.algorithms.UnionSet
import common.extensions.productOf
import common.extensions.takeMaxKBy
import common.extensions.takeMinKBy
import common.math.distinctPairs
import common.readFileLines
import org.jgrapht.alg.connectivity.ConnectivityInspector
import org.jgrapht.alg.spanning.PrimMinimumSpanningTree
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph
import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge

typealias Point = List<Long>

object Day8 {
    fun sqDistance(a: Point, b: Point) =
        a.zip(b).sumOf { (x, y) -> (x - y) * (x - y) }

    fun sqDistance(ab: Pair<Point, Point>) = sqDistance(ab.first, ab.second)

    fun boxes(lines: List<String>): List<Pair<Point, Point>> = lines
        .map { it.split(',').map(String::toLong) }
        .distinctPairs()

    fun partOne(lines: List<String>, connections: Int = 1_000): Long {
        val graph = DefaultUndirectedGraph<Point, DefaultEdge>(DefaultEdge::class.java)
        // Searching the minimum k elements instead of sortBy + take(k) is significantly faster.
        // Instead of N log N we only require N log k resulting in a ~16x speedup.
        boxes(lines)
            .map { it to sqDistance(it) } // Avoids recomputing distances
            .takeMinKBy(connections) { it.second }
            .forEach { (ab, _) ->
                graph.addEdgeMissingVertex(ab.first, ab.second)
            }
        return ConnectivityInspector(graph)
            .connectedSets() // compute connected components
            .takeMaxKBy(3) { it.size }
            .productOf { it.size }
            .toLong()
    }

    fun partTwo(lines: List<String>): Long {
        val unionSet = UnionSet<Point>()
        val allPoints = lines.size
        return boxes(lines).sortedBy(::sqDistance)
            .first { (a, b) ->
                unionSet.unionSetAddIfAbsent(a, b)
                // Check whether all boxes are in the same cluster
                unionSet.size == allPoints && unionSet.numSets == 1
            }.let { (a, b) -> a.first() * b.first() }
    }

    /**
     * Find the edge with the greatest weight in the minimum spanning tree.
     * This edge would be last added in the original problem definition.
     * This runs in O(|E| + |V|log(|V|)) instead of E * log E
     * with E = (N*(N-1))/2 and N = V = number of points.
     */
    fun partTwoPrim(lines: List<String>): Long {
        val graph = DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
        boxes(lines).forEach { (from, to) ->
            val edge = graph.edgeSupplier.get()
            graph.addEdgeMissingVertex(from, to, edge)
            graph.setEdgeWeight(edge, sqDistance(from, to).toDouble())
        }
        return PrimMinimumSpanningTree(graph)
            .spanningTree
            .edges
            .maxBy { edge -> graph.getEdgeWeight(edge) }
            .let { graph.getEdgeSource(it)[0] * graph.getEdgeTarget(it)[0] }
    }
}


fun main() {
    val input = readFileLines(8, 2025)
    println(Day8.partOne(input))
    println(Day8.partTwoPrim(input))
}