package common.algorithms

class UnionSet<T>() {

    private val parents = mutableMapOf<T, T>()
    var numSets: Int = 0
        private set
    private val sizes = mutableMapOf<T, Int>()

    val elements: Set<T> get() = parents.keys
    val size: Int get() = parents.size

    constructor(initialElements: Iterable<T>) : this() {
        initialElements.forEach { add(it) }
    }

    fun add(element: T) {
        parents[element] = element
        sizes[element] = 1
        numSets++
    }

    fun contains(element: T): Boolean = element in parents

    fun unionSet(a: T, b: T): Boolean {
        require(a in parents && b in parents)
        val rootA = findSet(a)
        val rootB = findSet(b)
        if (rootA == rootB) return false

        // Union by size: attach smaller tree to larger tree
        val sizeA = sizes[rootA]!!
        val sizeB = sizes[rootB]!!

        if (sizeA < sizeB) {
            parents[rootA] = rootB
            sizes[rootB] = sizeB + sizeA
        } else {
            parents[rootB] = rootA
            sizes[rootA] = sizeA + sizeB
        }

        numSets--
        return true
    }

    fun unionSetAddIfAbsent(a: T, b: T): Boolean {
        if (a !in parents) add(a)
        if (b !in parents) add(b)
        return unionSet(a, b)
    }

    fun findSet(element: T): T {
        val p = parents[element]!!
        if (p == element) {
            return element
        }
        // Path Compression: Point directly to the root
        val root = findSet(p)
        parents[element] = root
        return root
    }
}