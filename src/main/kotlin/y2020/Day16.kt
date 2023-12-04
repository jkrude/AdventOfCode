package y2020

import com.microsoft.z3.IntExpr
import com.microsoft.z3.Status
import common.Kontext
import common.extensions.findGroupValues
import common.extensions.productOf
import common.readFileText
import common.toInt


object Day16 {

    data class Rule(val name: String, val ranges: List<IntRange>) {
        fun isValid(nbr: Int) = ranges.any { nbr in it }

    }

    fun parseRule(ruleStr: String): Rule {
        val r = Regex("""(\w+ ?\w+?): (\d+)-(\d+) or (\d+)-(\d+)""")
        val (name, r1, r2, r3, r4) = r.findGroupValues(ruleStr)
        return Rule(name, listOf(r1.toInt()..r2.toInt(), r3.toInt()..r4.toInt()))
    }

    fun parse(text: String): Triple<List<Rule>, List<Int>, List<List<Int>>> {
        val (rules, ticket, otherTickets) = text.split("\n\n")
        return Triple(
            rules.lines().map(::parseRule),
            ticket.lines()[1].split(",").map(String::toInt),
            otherTickets.lines().drop(1).map { it.split(",").map { nbr -> nbr.toInt() } }
        )

    }

    fun partOne(input: String): Int {
        val (rules, _, others) = parse(input)
        return others.flatten().filter { rules.none { r -> r.isValid(it) } }.sum()
    }

    /**
     * map every rule to a unique position (in 0 until rules.size) s.t
     * for every other ticket o
     *      * for every (position,ticketField) in o:
     *      *     a rule that holds for ticketField is assigned this position
     *
     */
    fun findRuleAssignments(rules: List<Rule>, others: List<List<Int>>): List<Int> {

        with(Kontext()) {
            val solver = mkSolver()
            // Assign each rule one position 0 until rules.size
            val rulePositions: List<IntExpr> = List(rules.size) { mkIntConst("r$it") }
            solver.assertUniqueNumbers(rulePositions)

            // For each other ticket
            others.forEach { otherTicket ->
                otherTicket.forEachIndexed { position, ticketField ->
                    val indicesOfValidRules = rules.mapIndexedNotNull { ruleIndex, rule ->
                        if (rule.isValid(ticketField)) ruleIndex else null
                    }
                    // one of the rules that is true for that position has to be assigned the current position
                    solver.add(indicesOfValidRules.any { ruleIndex -> rulePositions[ruleIndex] eq position })
                }
            }
            val result = solver.check()
            if (result != Status.SATISFIABLE) error(solver.toString())
            return rulePositions.map { solver.model.evaluate(it, false).toInt() }
        }

    }

    fun partTwo(input: String): Long {
        val (rules, ticket, others) = parse(input)
        // All other tickets that are valid
        val othersValid = others.filter { other -> other.all { rules.any { r -> r.isValid(it) } } }

        // (idx,x) means rules[idx] has position x
        val rulePositions = findRuleAssignments(rules, othersValid)
        val reverseMap: Map<Int, Int> = rulePositions.mapIndexed { index, i -> i to index }.associate { it }
        require(othersValid.all { other ->
            other.withIndex().all { (idx, i) -> rules[reverseMap[idx]!!].isValid(i) }
        })


        val relevantPositions = rules.map { it.name }
            .withIndex()
            .filter { it.value.startsWith("departure") }
            .map { rulePositions[it.index] }
        return relevantPositions.productOf { ticket[it].toLong() }
    }
}


fun main() {
    println(Day16.partOne(readFileText(16, 2020)))
    println(Day16.partTwo(readFileText(16, 2020)))
}