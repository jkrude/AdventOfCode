package common.algorithms

import java.util.*

object Search {


    fun <Vertex> genericSearch(
        start: Vertex,
        neighbours: (Vertex) -> Iterable<Vertex>,
        add: (Deque<Vertex>, Vertex) -> Unit,
        isTarget: (node: Vertex) -> Boolean = { _ -> false },
        callOnEachVisited: (Vertex) -> Unit = {}
    ) {
        val visited = mutableSetOf<Vertex>()
        val queue: Deque<Vertex> = ArrayDeque()

        queue.push(start)
        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()
            if (visited.contains(currentNode)) {
                continue
            }
            visited.add(currentNode)

            callOnEachVisited(currentNode)

            if (isTarget(currentNode)) return

            neighbours(currentNode).forEach { node ->
                add(queue, node)
            }
        }
    }

    fun <Vertex> genericSearchTraversalList(
        edges: Map<Vertex, List<Vertex>>,
        start: Vertex,
        add: (Deque<Vertex>, Vertex) -> Unit,
        isTarget: (node: Vertex) -> Boolean = { _ -> false },
    ): MutableList<Vertex> {
        val traversalList = mutableListOf<Vertex>()
        genericSearch(
            start,
            neighbours = { node: Vertex -> edges[node] ?: emptyList() },
            add,
            isTarget
        ) {
            traversalList.add(it)
        }
        return traversalList
    }

    fun <Vertex> depthFirst(queue: Deque<Vertex>, node: Vertex) {
        queue.addFirst(node)
    }

    fun <Vertex> breadthFirst(queue: Deque<Vertex>, node: Vertex) {
        queue.addLast(node)
    }

}