package y2022

import common.extensions.findGroupValues
import common.readFileLines

object Day21 {

    private sealed interface YellingMonkey {
        //val name: String
        fun compute(): Long
    }

    private class AtomicYellingMonkey(val number: Long) : YellingMonkey {
        override fun compute(): Long = number
    }

    private class LazyComplexYellingMonkey(
        val dependencyLeft: String,
        val dependencyRight: String,
        val operator: Operator,
        private val monkeyMap: Map<String, YellingMonkey>
    ) : YellingMonkey {

        override fun compute(): Long {
            return operator(
                monkeyMap[dependencyLeft]!!.compute(),
                monkeyMap[dependencyRight]!!.compute()
            )
        }

    }

    private fun isAtomic(line: String): Boolean {
        return line.substringAfter(": ").toLongOrNull() != null
    }

    private val complexRightSidePattern = """(\w+) ([+\-*/]) (\w+)""".toRegex()
    private fun decomposeRightSide(rightSide: String): Triple<String, Char, String> {
        val (leftName, opString, rightName) = complexRightSidePattern.findGroupValues(rightSide)
        require(opString.length == 1)
        return Triple(leftName, opString.first(), rightName)
    }

    private enum class Operator(private val f: (Long, Long) -> Long) {
        PLUS({ a: Long, b: Long -> a + b }),
        MINUS({ a: Long, b: Long -> a - b }),
        TIMES({ a: Long, b: Long -> a * b }),
        DIVIDED({ a: Long, b: Long -> a / b });

        operator fun invoke(a: Long, b: Long) = f(a, b)

        fun inverse(): Operator {
            return when (this) {
                PLUS -> MINUS
                MINUS -> PLUS
                TIMES -> DIVIDED
                DIVIDED -> TIMES
            }
        }
    }


    private fun parseOperator(opSymbol: Char): Operator {
        return when (opSymbol) {
            '+' -> Operator.PLUS
            '-' -> Operator.MINUS
            '*' -> Operator.TIMES
            '/' -> Operator.DIVIDED
            else -> throw IllegalArgumentException(opSymbol.toString())
        }
    }

    private fun parseMonkeyMap(lines: List<String>): Map<String, YellingMonkey> {

        val monkeyMap: MutableMap<String, YellingMonkey> = mutableMapOf()

        fun complexMonkeyOf(rightSide: Triple<String, Char, String>): YellingMonkey {
            val (leftName, opChar, rightName) = rightSide
            return LazyComplexYellingMonkey(leftName, rightName, parseOperator(opChar), monkeyMap)
        }
        lines.forEach {
            val (name, rightSide) = it.split(": ")
            if (isAtomic(it)) {
                monkeyMap[name] = AtomicYellingMonkey(rightSide.toLong())
            } else {
                monkeyMap[name] = complexMonkeyOf(decomposeRightSide(rightSide))
            }
        }
        return monkeyMap
    }

    fun partOne(lines: List<String>): Long {
        val monkeyMap = parseMonkeyMap(lines)
        return monkeyMap["root"]?.compute() ?: throw IllegalStateException("root not in monkeyMap")

    }

    private fun humanIsDependencyOf(monkeyName: String, monkeyMap: Map<String, YellingMonkey>): Boolean {
        val monkey = monkeyMap[monkeyName] ?: throw IllegalArgumentException(monkeyName)
        if (monkeyName == "humn") return true
        return if (monkey !is LazyComplexYellingMonkey) false
        else humanIsDependencyOf(monkey.dependencyLeft, monkeyMap)
                || humanIsDependencyOf(monkey.dependencyRight, monkeyMap)

    }

    private fun nextEquals(leftValue: Long, rightValue: Long, xOnLeft: Boolean, operator: Operator): Long {
        // leftValue = a, rightValue = b
        // x * a = b <=> x = b / a <=> a * x = b
        // x + a = b <=> x = b - a <=> a + x = b
        // (x / a = b <=> x = b * a) || (x - a = b <=> x = b + a)
        return if (operator == Operator.PLUS || operator == Operator.TIMES
            || xOnLeft
        ) operator.inverse()(rightValue, leftValue)
        // a / x = b <=> x = a/ b || (a - x = b <=> x = a - b
        else operator(leftValue, rightValue)
    }

    private fun resolve(monkeyName: String, equals: Long, monkeyMap: Map<String, YellingMonkey>): Long {
        // monkeyName shout yell equals
        val monkey = monkeyMap[monkeyName] ?: throw IllegalArgumentException(monkeyName)
        if (monkeyName == "humn") return equals
        if (monkey !is LazyComplexYellingMonkey) throw IllegalArgumentException(monkeyName)
        val (otherName, independentOfHumanName) = if (humanIsDependencyOf(monkey.dependencyLeft, monkeyMap))
            (monkey.dependencyLeft to monkey.dependencyRight)
        else (monkey.dependencyRight to monkey.dependencyLeft)
        val independentValue = monkeyMap[independentOfHumanName]!!.compute()
        val nextEquals =
            nextEquals(independentValue, equals, otherName == monkey.dependencyLeft, monkey.operator)
        return resolve(otherName, nextEquals, monkeyMap)
    }

    fun partTwo(lines: List<String>): Long {
        val monkeyMap: Map<String, YellingMonkey> = parseMonkeyMap(lines)
        val root = monkeyMap["root"]!!
        if (root !is LazyComplexYellingMonkey) throw IllegalArgumentException()
        val (otherName, independentOfHumanName) = if (humanIsDependencyOf(root.dependencyLeft, monkeyMap))
            (root.dependencyLeft to root.dependencyRight) else (root.dependencyRight to root.dependencyLeft)
        return resolve(otherName, monkeyMap[independentOfHumanName]!!.compute(), monkeyMap)
    }
}

fun main() {
    println(Day21.partOne(readFileLines(2, 20221)))
    println(Day21.partTwo(readFileLines(2, 20221)))
}