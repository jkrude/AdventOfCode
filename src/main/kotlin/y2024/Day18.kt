package y2024

import common.algorithms.Dijkstra
import common.algorithms.UnionSet
import common.extensions.Idx2D
import common.extensions.Lists2D.containsIndex
import common.extensions.Lists2D.get
import common.extensions.Lists2D.getOrNull
import common.extensions.Lists2D.set
import common.extensions.allNeighbour
import common.extensions.neighbours
import common.extensions.takePair
import common.readFileLines

object Day18 {

    class MemorySpace(input: List<String>, val gridSize: Pair<Int, Int> = 70 to 70) {
        val grid: MutableList<MutableList<Boolean>>
        val fallingBytes: List<Pair<Int, Int>>

        init {
            val (w, h) = gridSize
            this.grid = MutableList(w + 1) { MutableList(h + 1) { true } }
            this.fallingBytes = input
                .map { it.split(",").map { c -> c.toInt() }.takePair() }
        }

        fun findPath() = Dijkstra.dijkstra(
            startPoints = listOf(0 to 0),
            neighbors = { current: Idx2D ->
                current.neighbours().filter { n -> grid.getOrNull(n) == true }.map { 1 to it }
            },
            isTarget = { it.first == gridSize.first && it.second == gridSize.second }
        )

    }

    fun partOne(input: List<String>, fallingBytes: Int = 1024, gridDim: Int = 70): Long {
        val memorySpace = MemorySpace(input, gridDim to gridDim)
        for (idx2d in memorySpace.fallingBytes.take(fallingBytes)) {
            memorySpace.grid[idx2d] = false
        }
        return memorySpace.findPath()!!.toLong()
    }

    fun partTwoNaive(input: List<String>, gridSize: Pair<Int, Int> = 70 to 70): String =
        with(MemorySpace(input, gridSize)) {
            return fallingBytes.first { byte ->
                grid[byte] = false
                findPath() == null
            }.let { (x, y) -> "$x,$y" }
        }

    fun partTwo(input: List<String>, gridDim: Int = 70): String {
        /*
        We can formulate the connectivity question as an online connected components' problem.
        Instead of tracking the shortest paths from the start to the goal we track whether there is
        a connection between the edges of the memory space. For example, if there is a continuous line
        of fallen bytes from the left edge to the right edge, the goal can't be reached anymore.
        Using the union set/find algorithm, we can efficiently check this condition.
        To this end we treat adjacent fallen bytes (8-neighborhood) as connected nodes and check whether
        the edges are part of the same connected component. Note that not all edge-edge combinations are problematic.
        For example, the top edge and the right edge might be connected through (0 to 70) which is fine.
         */
        val memorySpace = MemorySpace(input, gridDim to gridDim)
        val connected = UnionSet<Idx2D>()
        // Create and connect the edges around the memory space.
        for (i in 0 until gridDim) {
            connected.unionSetAddIfAbsent(-1 to i, -1 to (i + 1)) // top edge
            connected.unionSetAddIfAbsent(i to -1, (i + 1) to -1) // left edge
            connected.unionSetAddIfAbsent(gridDim+1 to i, gridDim+1 to (i + 1)) // bottom
            connected.unionSetAddIfAbsent(i to gridDim+1, (i + 1) to gridDim+1) // right
        }
        val edges = listOf(
            (-1 to 0), //top
            (0 to -1), // left
            (gridDim+1 to 0), // bottom
            (0 to gridDim+1), // right
        )
        with(memorySpace) {
            return fallingBytes.first { byteIdx2d ->
                grid[byteIdx2d] = false // register as fallen byte
                // If any of the surrounding cells contains a fallen byte -> create a connection
                byteIdx2d.allNeighbour().forEach { neighbor ->
                    if (!grid.containsIndex(neighbor) || !grid[neighbor]) {
                        connected.unionSetAddIfAbsent(byteIdx2d, neighbor)
                    }
                }
                val parents = edges.map { connected.findSet(it) }
                // is there a connection between top-left || top-bottom || bottom-right || left-right
                return@first (parents[0] == parents[1]) || (parents[0] == parents[2])
                        || (parents[2] == parents[3] || parents[1] == parents[3])
            }.let { (x, y) -> "$x,$y" }
        }
    }
}

fun main() {
    val input = readFileLines(18, 2024)
    println(Day18.partOne(input))
    println(Day18.partTwo(input))
}