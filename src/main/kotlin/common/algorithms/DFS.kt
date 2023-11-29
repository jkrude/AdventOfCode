package common.algorithms

object DFS {

    fun <T> genericDFSTrace(
        start: T,
        neighbours: T.() -> Collection<T>,
        isTarget: (T) -> Boolean
    ): List<T>? {
        val visited = mutableSetOf<T>()
        return fullyGenericDFSTrace(
            start, neighbours, isTarget,
            wasVisited = { it in visited },
            update = { visited.add(it) }
        )
    }

    inline fun <T> fullyGenericDFSTrace(
        start: T,
        neighbours: T.() -> Collection<T>,
        isTarget: (T) -> Boolean,
        wasVisited: (T) -> Boolean,
        update: (T) -> Unit = {}
    ): List<T>? {

        val candidates: ArrayDeque<T> = ArrayDeque<T>().apply { add(start) }
        val parents: MutableMap<T, T?> = mutableMapOf(start to null)

        while (candidates.isNotEmpty()) {
            val current = candidates.removeLast()
            if (wasVisited(current)) continue
            if (isTarget(current)) return reconstruct(current, parents)
            update(current)
            current.neighbours().forEach {
                candidates.addLast(it)
                parents[it] = current
            }
        }
        return null
    }

    fun <T> reconstruct(last: T, parents: Map<T, T?>): List<T> {
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