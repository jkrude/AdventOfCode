package common.algorithms

import java.util.*

object Search {

    class SearchBuilder<V>(private val starts: Iterable<V>) {

        private lateinit var neighborsOf: (V) -> Iterable<V>
        private lateinit var target: (V) -> Boolean
        private var visitor: (V) -> Unit = {}
        private var revisitNodes: Boolean = false
        fun neighbors(supplier: (V) -> Iterable<V>): SearchBuilder<V> {
            this.neighborsOf = supplier
            return this
        }

        fun neighbors(map: Map<V, Iterable<V>>): SearchBuilder<V> {
            this.neighborsOf = { map[it] ?: emptyList() }
            return this
        }

        fun stopAt(predicate: (V) -> Boolean): SearchBuilder<V> {
            this.target = predicate
            return this
        }

        fun onEachVisit(consumer: (V) -> Unit): SearchBuilder<V> {
            this.visitor = consumer
            return this
        }

        fun allowRevisit(): SearchBuilder<V> {
            revisitNodes = true
            return this
        }

        fun executeDfs() = start(Deque<V>::addFirst)

        fun executeBfs() = start(Deque<V>::addLast)

        fun dFSWithTrace(): List<V>? {
            require(this::neighborsOf.isInitialized)
            require(this::target.isInitialized, { "In order to build a trace a target predicate is required." })
            val parents: MutableMap<V, V?> = mutableMapOf()
            starts.forEach { parents[it] = null }
            val addWithNeighbor: (V) -> Iterable<V> = {
                neighborsOf(it).onEach { n -> parents[n] = it }
            }
            require(this::neighborsOf.isInitialized)
            val target = genericSearch(
                this.starts,
                neighbours = addWithNeighbor,
                add = Deque<V>::addFirst,
                isTarget = target,
                callOnEachVisited = visitor
            ) ?: return null
            return reconstruct(target, parents)

        }

        private fun start(addTo: Deque<V>.(V) -> Unit): V? {
            require(this::neighborsOf.isInitialized)
            if (!this::target.isInitialized) {
                target = { false }
            }
            return genericSearch(
                this.starts,
                neighbours = neighborsOf,
                add = addTo,
                isTarget = target,
                callOnEachVisited = visitor,
                revisitNodes = revisitNodes
            )
        }
    }

    fun <V> startingFrom(start: V) =
        SearchBuilder(listOf(start))

    fun <V> startingFrom(startVertices: Iterable<V>): SearchBuilder<V> =
        SearchBuilder(startVertices)


    private fun <Vertex> genericSearch(
        start: Iterable<Vertex>,
        neighbours: (Vertex) -> Iterable<Vertex>,
        add: (Deque<Vertex>, Vertex) -> Unit,
        isTarget: (node: Vertex) -> Boolean = { _ -> false },
        callOnEachVisited: (Vertex) -> Unit = {},
        revisitNodes: Boolean = false
    ): Vertex? {
        val visited = mutableSetOf<Vertex>()
        val queue: Deque<Vertex> = ArrayDeque()

        start.forEach { queue.push(it) }
        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()
            if (!revisitNodes && visited.contains(currentNode)) {
                continue
            }
            if (!revisitNodes) visited.add(currentNode)

            callOnEachVisited(currentNode)

            if (isTarget(currentNode)) return currentNode

            neighbours(currentNode).forEach { node ->
                add(queue, node)
            }
        }
        return null
    }

    private fun <T> reconstruct(last: T, parents: Map<T, T?>): MutableList<T> {
        val path = mutableListOf<T>()
        var curr: T? = last
        while (curr != null) {
            path.add(curr)
            curr = parents[curr]
        }
        path.reverse()
        return path
    }
}