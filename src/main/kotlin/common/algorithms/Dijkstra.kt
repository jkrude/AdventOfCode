package common.algorithms

import java.util.*

object Dijkstra {

    private fun <Vertex> internalDijkstra(
        startPoints: Iterable<Vertex>,
        neighbors: (Vertex) -> Iterable<Pair<Int, Vertex>>,
        isTarget: (Vertex) -> Boolean,
    ): Pair<MutableMap<Vertex, Vertex>, MutableMap<Vertex, Int>>? {
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
            if (isTarget(current)) continue
            neighbors(current).forEach { (weight, next) ->
                val alternative = currentDistance + weight
                if (alternative < distances.getValue(next)) {
                    distances[next] = alternative
                    predecessors[next] = current
                    if (!priorityQueue.contains(next)) priorityQueue.add(next)
                }
            }
        }
        if (predecessors.keys.none { isTarget(it) } || distances.keys.none { isTarget(it) }) return null
        return predecessors to distances
    }

    /**
     * Compute the set of vertices that are on an optimal path to the target.
     * There can be multiple targets with the same optimal distance.
     * @return The set of vertices on an optimal path to a target (including start and target vertices)
     *  as well as the distance of every visited node to a start vertex.
     */
    fun <Vertex> allShortestDijkstra(
        startPoints: Iterable<Vertex>,
        neighbors: (Vertex) -> Iterable<Pair<Int, Vertex>>,
        isTarget: (Vertex) -> Boolean,
    ): Pair<Set<Vertex>, Map<Vertex, Int>>? {
        val distances = mutableMapOf<Vertex, Int>().withDefault { Int.MAX_VALUE }
        startPoints.forEach { distances[it] = 0 }
        val allBestPredecessors = mutableMapOf<Vertex, MutableList<Vertex>>()
        val priorityQueue = PriorityQueue<Vertex> { a, b ->
            (distances[a] ?: Int.MAX_VALUE) - (distances[b] ?: Int.MAX_VALUE)
        }
        var targetValue: Int? = null
        val targetVertices: MutableList<Vertex> = mutableListOf()

        priorityQueue.addAll(startPoints)
        while (priorityQueue.isNotEmpty()) {
            val current = priorityQueue.poll()
            val currentDistance = distances.getValue(current)
            if (isTarget(current)) {
                targetValue = currentDistance
                targetVertices.add(current)
            }
            neighbors(current).forEach { (weight, next) ->
                val alternative = currentDistance + weight
                if (targetValue != null && alternative > targetValue) return@forEach
                val previousDistance = distances.getValue(next)
                if (alternative < previousDistance) {
                    distances[next] = alternative
                    allBestPredecessors[next]?.clear()
                    if (!priorityQueue.contains(next)) priorityQueue.add(next)
                }
                if (alternative <= previousDistance) {
                    allBestPredecessors.getOrPut(next, { mutableListOf() }).add(current)
                }
            }
        }
        if (targetVertices.isEmpty()) return null
        val optimalVertices = mutableSetOf<Vertex>()
        // Go back from the targets and collect all optimal predecessors.
        Search.SearchBuilder(targetVertices)
            .neighbors { allBestPredecessors[it] ?: emptyList() }
            .onEachVisit { optimalVertices.add(it) }
            .executeBfs()
        return optimalVertices to distances
    }

    fun <Vertex> dijkstraNoWeights(
        edges: Map<Vertex, List<Vertex>>,
        start: Vertex,
        end: Vertex,
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
        end: Vertex,
    ) = dijkstra(listOf(start), edges) { it == end }

    fun <Vertex> dijkstra(
        startPoints: List<Vertex>,
        edges: Map<Vertex, List<Pair<Int, Vertex>>>,
        isTarget: (Vertex) -> Boolean,
    ): Int? = dijkstra(startPoints, { edges[it] ?: emptyList() }, isTarget)

    fun <Vertex> dijkstra(
        startPoints: List<Vertex>,
        neighbors: (Vertex) -> Iterable<Pair<Int, Vertex>>,
        isTarget: (Vertex) -> Boolean,
    ): Int? {
        val distances = internalDijkstra(startPoints, neighbors, isTarget)?.second ?: return null
        return distances.entries.first { isTarget(it.key) }.value
    }

    fun <Vertex> dijkstraPath(
        startPoints: Iterable<Vertex>,
        neighbors: (Vertex) -> Iterable<Pair<Int, Vertex>>,
        isTarget: (Vertex) -> Boolean,
    ): List<Vertex>? {
        val (predecessors, distances) = internalDijkstra(startPoints, neighbors, isTarget) ?: return null
        val end = distances.keys.first { isTarget(it) }
        var n = end
        val path = mutableListOf(n)
        while (n in predecessors) {
            n = predecessors[n]!!
            path.add(n)
        }
        return path.reversed()
    }
}