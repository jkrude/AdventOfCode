package y2024

import common.extensions.*
import common.extensions.Lists2D.containsIndex
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.math.product
import common.readFileLines

object Day8 {

    private fun parse(lines: List<String>): Pair<List<List<Char>>, Map<Char, List<Idx2D>>> {
        val grid = lines.toCharList2D()
        val antennas: Map<Char, List<Idx2D>> = grid.indices2d()
            .groupBy { grid[it] }
            .filterKeys { it != '.' }
        return grid to antennas
    }

    fun partOne(lines: List<String>): Int {
        val (grid, antennas) = parse(lines)
        val antiNodes = mutableSetOf<Idx2D>()
        antennas.values.forEach { values ->
            for ((x, y) in values product values) {
                if (x == y) continue
                val diff = x - y
                val o1 = x + diff
                val o2 = y - diff
                if (grid.containsIndex(o1)) antiNodes.add(o1)
                if (grid.containsIndex(o2)) antiNodes.add(o2)
            }
        }
        return antiNodes.size
    }

    private fun List2D<Char>.print(antiNodes: Set<Idx2D>) {
        val g = this.map { it.toMutableList() }.toMutableList()
        for (a in antiNodes) {
            g[a.first][a.second] = '#'
        }
        println(g.joinToString(separator = "\n") { it.joinToString(separator = "") })
    }

    private fun List2D<Char>.antiNodes(x: Idx2D, y: Idx2D): Set<Idx2D> {
        if (x == y) return setOf(x)
        val positions = mutableSetOf<Idx2D>()
        val diff = x - y
        var o1 = x + diff
        var o2 = y - diff
        while (this.containsIndex(o1)) {
            positions.add(o1)
            o1 += diff
        }
        while (this.containsIndex(o2)) {
            positions.add(o2)
            o2 -= diff
        }
        return positions
    }

    fun partTwo(lines: List<String>): Int {
        val (grid, antennas) = parse(lines)
        val antiNodes = mutableSetOf<Idx2D>()
        antennas.values.forEach { values ->
            for ((x, y) in values product values) {
                antiNodes.addAll(grid.antiNodes(x, y))
            }
        }
        // Use for debugging: grid.print(antiNodes)
        return antiNodes.size
    }
}

fun main() {
    val input = readFileLines(8, 2024)
    println(Day8.partOne(input))
    println(Day8.partTwo(input))
}