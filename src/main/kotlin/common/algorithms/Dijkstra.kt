package common.algorithms

import java.util.*

object Dijkstra {

    fun <Vertex> dijkstraNoWeights(
        edges: Map<Vertex, List<Vertex>>,
        start: Vertex,
        end: Vertex
    ): Int? {
        return dijkstra(
            edges.mapValues { (_, list) -> list.map { 1 to it } },
            start,
            end
        )
    }

    fun <Vertex> dijkstra(
        edges: Map<Vertex, List<Pair<Int, Vertex>>>,
        start: Vertex,
        end: Vertex
    ) = dijkstra(edges, listOf(start), end)

    fun <Vertex> dijkstra(
        edges: Map<Vertex, List<Pair<Int, Vertex>>>,
        startPoints: List<Vertex>,
        end: Vertex
    ): Int? {
        val distances = mutableMapOf<Vertex, Int>().withDefault { Int.MAX_VALUE }
        startPoints.forEach { distances[it] = 0 }
        val predecessors = mutableMapOf<Vertex, Vertex>()
        val priorityQueue = PriorityQueue<Vertex> { a, b ->
            (distances[a] ?: Int.MAX_VALUE) - (distances[b] ?: Int.MAX_VALUE)
        }
        priorityQueue.addAll(startPoints)
        while (priorityQueue.isNotEmpty()) {
            val current = priorityQueue.poll()
            val currentDistance = distances.getValue(current)
            if (current == end) continue
            edges[current]?.forEach { (weight, next) ->
                val alternative = currentDistance + weight
                if (alternative < distances.getValue(next)) {
                    distances[next] = alternative
                    predecessors[next] = current
                    if (!priorityQueue.contains(next)) priorityQueue.add(next)
                }
            }
        }
        if (end !in predecessors || end !in distances) return null
        return distances[end]!!
    }
}