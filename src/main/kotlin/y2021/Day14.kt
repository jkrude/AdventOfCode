package y2021

import common.readFileText

class Day14 {

    companion object {

        fun parse(text: String = readFileText(14)): Pair<String, Map<String, Char>> {
            val (template, rulesInterim) = text.split("\n\n")
            val rules = rulesInterim.split("\n").map { it.split(" -> ") }.associate { it[0] to it[1][0] }
            return template to rules
        }

        fun partOne(template: String, rules: Map<String, Char>): Int {
            var polymer = template
            repeat(10) {
                polymer = polymer.windowed(2)
                    .joinToString("", postfix = polymer.last().toString())
                    { it[0] + (rules[it] ?: "").toString() }
            }
            return polymer.toSet().maxOf { c -> polymer.count { c == it } } - polymer.toSet()
                .minOf { c -> polymer.count { c == it } }
        }


        fun partTwo(template: String, rules: Map<String, Char>): Long {

            val patternMap: MutableMap<String, Long> =
                template.windowed(2).groupingBy { it }.eachCount()
                    .mapValues { it.value.toLong() }.toMutableMap()

            val occurrenceMap: MutableMap<Char, Long> =
                template.groupingBy { it }.eachCount()
                    .mapValues { it.value.toLong() }.toMutableMap()

            operator fun Char.plus(char: Char): String = this.toString() + char

            repeat(40) {
                for ((pattern, occ) in patternMap.filter { (_, v) -> v > 0 }) {
                    val nextChar = rules[pattern] ?: continue
                    occurrenceMap[nextChar] = (occurrenceMap[nextChar] ?: 0) + occ
                    patternMap[pattern] = patternMap[pattern]?.minus(occ) ?: 0
                    // update both new pattern
                    (pattern[0] + nextChar + pattern[1]).windowed(2)
                        .filter { it in rules } // only pattern with rules are interesting
                        .forEach { patternMap[it] = (patternMap[it] ?: 0) + occ }
                }
            }
            return occurrenceMap.maxOf { it.value } - occurrenceMap.minOf { it.value }
        }
    }
}

fun main() {
    val (template, rules) = Day14.parse()
    println("PART 1: " + (Day14.partOne(template, rules)))
    println("PART 2: " + (Day14.partTwo(template, rules)))
}