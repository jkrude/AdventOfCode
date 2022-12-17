package common.algorithms

import java.util.*

object Search {


    private fun <Vertex> genericSearch(
        edges: Map<Vertex, List<Vertex>>,
        start: Vertex,
        add: (Deque<Vertex>, Vertex) -> Unit
    ): MutableList<Vertex> {
        val visited = mutableSetOf<Vertex>()
        val queue: Deque<Vertex> = ArrayDeque()

        queue.push(start)
        val traversalList = mutableListOf<Vertex>()
        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()
            if (!visited.contains(currentNode)) {
                traversalList.add(currentNode)
                visited.add(currentNode)
                edges[currentNode]?.forEach { node ->
                    add(queue, node)
                }
            }
        }
        return traversalList
    }

    fun <Vertex> depthFirst(
        edges: Map<Vertex, List<Vertex>>,
        start: Vertex,
    ): List<Vertex> {
        return genericSearch(edges, start) { q, v -> q.addFirst(v) }
    }

    fun <Vertex> breadthFirst(
        edges: Map<Vertex, List<Vertex>>,
        start: Vertex,
    ): List<Vertex> {
        return genericSearch(edges, start) { q, v -> q.addLast(v) }
    }

}