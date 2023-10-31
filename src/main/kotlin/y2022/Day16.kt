package y2022

import common.algorithms.permutations
import common.readFileLines
import kotlin.math.max

object Day16 {

    fun parse(lines: List<String>): Pair<Map<String, Int>, Map<String, List<String>>> {
        val pattern =
            """Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? ((?:[A-Z]{2}(?:, )?)+)""".toRegex()
        val valveFlow = mutableMapOf<String, Int>()
        val edges = mutableMapOf<String, List<String>>()
        lines.forEach {
            val groups = pattern.find(it)?.groupValues ?: throw IllegalArgumentException(it)
            val (valve, flow, next) = groups.drop(1)
            valveFlow[valve] = flow.toInt()
            edges[valve] = next.split(", ")
        }

        return valveFlow to edges
    }


    fun partOneBrute(lines: List<String>): Int {
        val (flowMap, edgeMap) = parse(lines)
        val valvesIdxMap = flowMap.keys.toList().withIndex().associate { (idx, valve) -> valve to idx }
        fun idxOf(valve: String) = valvesIdxMap[valve]!!

        val flows: List<Int> = flowMap.entries
            .sortedBy { idxOf(it.key) }.map { it.value }

        val start = "AA"
        require(start in flowMap.keys)

        // Int.MAX_VALUE means no edge exists
        val adjacencyMatrix = Array(valvesIdxMap.size) { IntArray(valvesIdxMap.size) { Int.MAX_VALUE } }
        for ((valve, neighbours) in edgeMap) {
            for (neighbourValve in neighbours) {
                adjacencyMatrix[idxOf(valve)][idxOf(neighbourValve)] = 1
                adjacencyMatrix[idxOf(valve)][idxOf(valve)] = 0 // Distance to self is 0
            }
        }
        val allDistances = computeAllPairShortestDistance(adjacencyMatrix)


        // index = oldIndex
        val positiveFlows: List<IndexedValue<Int>> = flows
            .withIndex()
            .filter { (_, flow) -> flow > 0 }

        return permutations(positiveFlows.size)
            .maxOf {
                // it = config = list of size positive flow with config[i] = idx
                // s.t. idx in 0..config.size and idx is the idx of the valve that is opened i-th
                valueOfOpened(idxOf(start), positiveFlows, it, allDistances) ?: 0
            }
    }

    fun computeAllPairShortestDistance(adjacencyMatrix: Array<IntArray>): Array<IntArray> {
        val copy: Array<IntArray> = adjacencyMatrix.map { it.copyOf() }.toTypedArray()
        common.algorithms.allPairsShortestPath.floydWarshall(copy)
        return copy
    }


    /**
     * @param flows at position i is the flow of valve i
     * @param config where the i-th entry is the index of the valve that opened i-th
     * @param distances matrix with the shortest path from i-th valve to j-th valve -1 for no route possible
     */
    fun valueOfOpened(
        startValveIdx: Int,
        flows: List<IndexedValue<Int>>, // flows[i] = (idx,flow) where idx is the row/colum in distances that corresponds to valve i
        config: List<Int>,
        distances: Array<IntArray>
    ): Int? {
        fun distanceOf(i: Int, j: Int) = distances[flows[i].index][flows[j].index]
        var time = 30 - distances[startValveIdx][flows[config.first()].index] // Move to first valve from start
        time-- // Open the valve
        if (time < 0) return null
        var value = flows[config.first()].value * time // Open the first valve
        for (i in config.indices) { // kotlin style would be windowed(2)
            if (i == config.lastIndex) continue
            val valve = config[i]
            val nextValve = config[i + 1]
            val dist = distanceOf(valve, nextValve) // it no edge exists distance = INT.MAX_VALUE
            time -= dist // move to nextValve
            time-- // Open the valve
            if (time < 0) return null
            value += flows[nextValve].value * time
        }

        return value
    }


    fun solve(position: String, flows: Map<String, Int>, map: Map<String, List<String>>): Int {

        val cache = mutableMapOf<Triple<String, Int, Set<String>>, Int>()
        fun cachedRecursive(position: String, time: Int, opened: Set<String>): Int {

            val args = Triple(position, time, opened)

            if (args in cache) return cache[args]!!
            if (time == 0) {
                cache[args] = 0
                return 0
            }
            var score = map.getValue(position).maxOfOrNull {
                cachedRecursive(it, time - 1, opened)
            } ?: 0
            val currentFlow = flows.getValue(position)
            if (currentFlow > 0 && position !in opened) {
                score = max(
                    score,
                    (time - 1) * currentFlow + cachedRecursive(position, time - 1, opened + setOf(position))
                )
            }
            cache[args] = score
            return score
        }
        return cachedRecursive(
            position,
            30,
            emptySet()
        )
    }

    fun partOne(lines: List<String>): Int {
        val (flow, map) = parse(lines)
        return solve("AA", flow, map)
    }

    fun partTwo(lines: List<String>): Int = TODO()
}

fun main() {
//    Day16.reduce(y2021.readFileLines(1,20226))/
    println(Day16.partOne(readFileLines(1, 20226)))
//    println(Day16.y2021.partTwo(y2021.readFileLines(1,20226)))
}