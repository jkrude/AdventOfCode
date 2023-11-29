package y2020

import arrow.core.MemoizedDeepRecursiveFunction
import common.reachableVerticesFrom
import common.readFileLines
import common.toDirectedJGraph
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge

object Day7 {

    data class Rule(val bag: String, val containedBags: List<Pair<Int, String>>)

    private val regex = Regex("""(\w+ \w+) bags contain (.*)\.""".trimMargin())
    private val rightSideRegex = Regex("""(\d) (\w+ \w+) bags?""")

    fun parseRule(rule: String): Rule {
        val (left, containedStr) = regex.find(rule)?.groupValues?.drop(1)
            ?: throw IllegalArgumentException("Could not parse $rule")
        if (containedStr == "no other bags") return Rule(left, emptyList())
        val contained: Sequence<List<String>> = rightSideRegex.findAll(containedStr).map { it.groupValues.drop(1) }
        return Rule(left, contained.map { (amtStr, color) -> amtStr.toInt() to color }.toList())
    }

    fun partOne(lines: List<String>): Int {
        val rules = lines.map(::parseRule)
        return reachableGraph(rules)
    }

    fun reachableGraph(rules: List<Rule>): Int {
        val graph: Graph<String, DefaultEdge> =
            rules.associate { (bag, contained) -> bag to contained.map { it.second } }
                .toDirectedJGraph()
        // minus once to exclude shiny gold itself
        return graph.reachableVerticesFrom("shiny gold").size - 1
    }


    fun partTwo(lines: List<String>): Int {
        val rules: Map<String, List<Pair<Int, String>>> =
            lines.map(::parseRule).associate { it.bag to it.containedBags }

        val recursiveAmount = MemoizedDeepRecursiveFunction<String, Int> { bagColor ->
            rules[bagColor]?.let { contained: List<Pair<Int, String>> ->
                contained.sumOf { (amount, color) ->
                    amount + amount * callRecursive(color)
                }
            } ?: 0
        }
        return recursiveAmount("shiny gold")
    }
}

fun main() {

    println(Day7.partOne(readFileLines(7, 2020)))
    println(Day7.partTwo(readFileLines(7, 2020)))
}