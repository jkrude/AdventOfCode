package y2023

import common.CyclicIterator
import common.extensions.findGroupValues
import common.math.lcm
import common.readFileText

object Day8 {

    data class Node(val left: String, val right: String)

    fun parse(input: String): Pair<CyclicIterator<Char>, Map<String, Node>> {
        val (instructions, graphStr) = input.split("\n\n")
        val r = Regex("""([A-Z]+) = \(([A-Z]+), ([A-Z]+)\)""")
        val graph =
            graphStr.lines().associate { val (node, left, right) = r.findGroupValues(it); node to Node(left, right) }
        return CyclicIterator(instructions.toList()) to graph
    }


    fun partOne(input: String): Long {
        val (instructions, graph) = parse(input)
        return steps("AAA", instructions, graph)
    }

    fun steps(start: String, instructions: CyclicIterator<Char>, graph: Map<String, Node>): Long {
        var steps = 0L
        var current = start
        do {
            current = if (instructions.next() == 'R') graph[current]!!.right else graph[current]!!.left
            steps++
        } while (current.last() != 'Z')
        return steps
    }


    fun partTwo(input: String): Long {
        val (instructions, graph) = parse(input)
        val startPoints = graph.keys.filter { it.endsWith("A") }
        // Turns out if s, target = steps(start,instructions,graph) is the first found target after s steps
        // Then s1,t1 = steps(target,instruction,graph) is the same
        // Therefore we have |startPoints| many loops of different length
        return startPoints.map { steps(it, instructions.clone(), graph) }.reduce { x, y -> lcm(x, y) }
    }
}

fun main() {
    val input = readFileText(8, 2023)
    println(Day8.partOne(input))
    println(Day8.partTwo(input))
}