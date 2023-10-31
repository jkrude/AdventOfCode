package y2022

import common.algorithms.Dijkstra
import common.get
import common.indices2d
import common.neighbours
import common.readFileLines

class Day12 {
    companion object {

        private fun parse(lines: List<String>): Triple<Pair<Int, Int>, Pair<Int, Int>, List<List<Int>>> {
            var start = 0 to 0
            var end = 0 to 0
            val heightMap: List<List<Int>> = lines.mapIndexed { row, line ->
                line.mapIndexed { col, c ->
                    if (c.isLowerCase()) c - '`'
                    else if (c == 'S') 1.also { start = row to col }
                    else 26.also { end = row to col }
                }
            }
            return Triple(start, end, heightMap)
        }

        private fun asGraph(heightMap: List<List<Int>>): Map<Pair<Int, Int>, MutableList<Pair<Int, Int>>> {
            val rows = heightMap.size
            val columns = heightMap[0].size
            val vertices = heightMap.indices2d()
            val edges: Map<Pair<Int, Int>, MutableList<Pair<Int, Int>>> =
                vertices.associateWith { mutableListOf() }
            for (vertex in vertices) {
                for ((i, j) in vertex.neighbours()) {
                    if (i !in 0 until rows || j !in 0 until columns) continue
                    if (heightMap[vertex] >= heightMap[i][j] - 1) {
                        edges[vertex]!!.add(i to j)
                    }
                }
            }
            return edges
        }

        fun partOne(lines: List<String>): Int {
            val (start, end, heightMap) = parse(lines)
            val edges = asGraph(heightMap)
            return Dijkstra.dijkstraNoWeights(edges, start, end)!!
        }

        fun partTwo(lines: List<String>): Int {
            val (start, end, heightMap) = parse(lines)
            val edges = asGraph(heightMap)
            return heightMap
                .indices2d()
                .filter { heightMap[it] == 1 } // All starting positions with min height
                .mapNotNull {
                    Dijkstra.dijkstraNoWeights(edges, it, end)
                }.minOrNull()!!
        }
    }
}

fun main() {
    println(Day12.partOne(readFileLines(12)))
    println(Day12.partTwo(readFileLines(12)))
}