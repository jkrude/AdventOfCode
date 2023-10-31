package common.algorithms

class UnionSet<T>() {

    private val parents = mutableMapOf<T, T>()

    constructor(initialElements: Iterable<T>) : this() {
        initialElements.forEach { add(it) }
    }

    fun add(element: T) {
        parents[element] = element
    }

    fun unionSet(a: T, b: T) {
        require(a in parents && b in parents)
        val parentA = findSet(a)
        val parentB = findSet(b)
        if (parentA == parentB) return
        parents[parentB] = parents[parentA]!! // Could be optimized
    }

    fun findSet(a: T): T {
        var curr = a
        val visited = mutableListOf(a)
        while (parents[curr] != curr) {
            curr = parents[curr]!!
            visited.add(curr)
        }
        for (node in visited) { // Path compression
            parents[node] = curr
        }
        return curr
    }


}