package y2020

import common.readFileText

object Day19 {


    sealed interface Rule {
        fun matchesUntil(rules: List<Rule>, message: String, startIdx: Int): Int?
    }


    data class RuleAnd(val indices: List<Int>) : Rule {
        override fun matchesUntil(rules: List<Rule>, message: String, startIdx: Int): Int? {
            var matchIdx = startIdx
            for (idx in indices) {
                if (matchIdx > message.lastIndex) return null
                matchIdx = rules[idx].matchesUntil(rules, message, matchIdx) ?: return null
            }
            return matchIdx
        }

    }

    data class RuleOr(val left: Rule, val right: Rule) : Rule {
        override fun matchesUntil(rules: List<Rule>, message: String, startIdx: Int): Int? {
            return left.matchesUntil(rules, message, startIdx)
                ?: right.matchesUntil(rules, message, startIdx)
        }
    }

    data class Simple(val symbol: Char) : Rule {
        override fun matchesUntil(rules: List<Rule>, message: String, startIdx: Int): Int? =
            if (message[startIdx] == symbol) startIdx + 1 else null

    }


    fun parseRule(string: String): Pair<Int, Rule> {
        val (idxStr, constraints) = string.split(": ")
        return idxStr.toInt() to (
                if ('|' in constraints) {
                    val (left, right) = constraints.split(" | ")
                        .map { side ->
                            side
                                .split(" ")
                                .map { subRule -> subRule.toInt() }
                        }.map { RuleAnd(it) }
                    RuleOr(left, right)
                } else if ("\"" in constraints) {
                    // 4: "a" -> constraint = "a"
                    // 01234  ->              012
                    Simple(constraints[1])
                } else RuleAnd(constraints.split(" ").map { it.toInt() }))
    }

    fun parse(input: String): Pair<List<Rule>, List<String>> {
        val (rulesStr: List<String>, messages: List<String>) = input.split("\n\n").map { it.lines() }
        val rules: List<Rule> = rulesStr
            .map { rule -> parseRule(rule) }
            .sortedBy { it.first }
            .map { it.second }
        return rules to messages

    }

    fun partOne(input: String): Int {
        val (rules, messages) = parse(input)
        return messages.count { m -> rules[0].matchesUntil(rules, m, 0) == m.length }
    }


    /**
     * 0: 8 11
     * -> 42^n 31^m
     *      with m < n, n > 1, m > 0
     *  42 has to match at least twice once for rule 8 once for rule 11
     *  31 can only match as often as 42 minus once for the rule-8 match
     */
    fun matchesV2(rules: List<Rule>, message: String): Boolean {
        require(rules[0] == RuleAnd(listOf(8, 11)))

        var times42Matched = 0
        var startOfNextMatch = 0
        do {
            // If rule 42 does not match return false
            startOfNextMatch = rules[42].matchesUntil(rules, message, startOfNextMatch) ?: return false
            times42Matched++
            if (times42Matched < 2) continue
            // Try to complete the rest of the string with repetitions of rule 31
            var startOf31Matches: Int = startOfNextMatch
            var times31Matched = 0
            do {
                startOf31Matches = rules[31].matchesUntil(rules, message, startOf31Matches)
                    ?: break // we can't match using rule 31 try more rule 42's
                times31Matched++
                if (startOf31Matches == message.length) return true // we completely matched the string
            } while (times31Matched < times42Matched - 1) // m <= n -1

        } while (startOfNextMatch < message.length)
        return false
    }


    fun partTwo(input: String): Int {
        val (rules, messages) = parse(input)
        return messages.count { m -> matchesV2(rules, m) }
    }
}

fun main() {
    println(Day19.partOne(readFileText(19, 2020)))
    println(Day19.partTwo(readFileText(19, 2020)))
}