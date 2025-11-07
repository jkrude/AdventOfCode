package y2024

import common.algorithms.Search
import common.extensions.*
import common.extensions.Lists2D.containsIndex
import common.extensions.Lists2D.indices2d
import common.extensions.Lists2D.get
import common.readFileLines

object Day12 {

    private fun partitionInRegions(grid: List2D<Char>): List<Set<Idx2D>> {
        val regions = mutableListOf<Set<Idx2D>>()

        for (ij in grid.indices2d()) {
            if (regions.any { ij in it }) continue
            val newRegion = mutableSetOf<Idx2D>()
            Search.startingFrom(ij)
                .neighbors {
                    it.neighbours()
                        .filter { n -> grid.containsIndex(n) }
                        .filter { n -> grid[n] == grid[ij] }
                }
                .onEachVisit(newRegion::add)
                .executeBfs()
            regions.add(newRegion)
        }
        return regions
    }

    fun partOne(lines: List<String>): Int {
        val grid = lines.toCharList2D()
        return partitionInRegions(grid).sumOf { region ->
            region.sumOf {
                it.neighbours().count { n -> !grid.containsIndex(n) || grid[n] != grid[it] }
            } * region.size
        }
    }

    fun List2D<Char>.outerCornerCount(ij: Idx2D): Int {
        val neighbor = ij.neighbours().filter { n ->
            !this.containsIndex(n) || this[n] != this[ij]
        }
        return when (neighbor.size) {
            0, 1 -> 0
            3 -> 2
            4 -> 4
            else -> {
                // Find out if the neighbours are opposed to each other
                val (n1, n2) = neighbor
                if (n1.first == n2.first || n1.second == n2.second) 0 else 1
            }
        }
    }

    fun List2D<Char>.innerCorner(ij: Idx2D): Int {
        /**
         * Both (0,0) and (2,0) have an inner corner.
         * EE
         * EX
         * EE
         */
        val neighbors = ij.neighbours().toSet()
        val outerNeighbor = ij.allNeighbour().toSet() - neighbors
        // This would be the X in the example
        val potential = outerNeighbor.filter { n -> this.containsIndex(n) && this[n] != this[ij] }
        // Check that the X has two adjacent E overlapping with the neighbors of ij
        return potential.count { p ->
            val overlappingNeighbors = p.neighbours().toSet().intersect(neighbors)
            overlappingNeighbors.size == 2 && overlappingNeighbors.all { n -> this[n] == this[ij] }
        }
    }

    fun partTwo(lines: List<String>): Int {
        val grid = lines.toCharList2D()
        val regions = partitionInRegions(grid)
        // Number of corners are equal to number of edges
        return regions.sumOf { region ->
            region.sumOf { ij -> grid.outerCornerCount(ij) + grid.innerCorner(ij) } * region.size
        }
    }
}

fun main() {
    val input = readFileLines(12, 2024)
    println(Day12.partOne(input))
    println(Day12.partTwo(input))
}