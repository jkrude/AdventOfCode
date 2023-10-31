package y2020

import common.readFileLines

object Day1 {
    fun partOne(lines: List<String>): Int {
        val numbers = mutableSetOf<Int>()
        lines.forEach { numStr ->
            with(Integer.parseInt(numStr)) {
                numbers.add(this)
                if ((2020 - this) in numbers) {
                    return this * (2020 - this)
                }
            }
        }
        throw IllegalArgumentException("Could not find pairs summing to 2020")
    }

    fun partTwo(lines: List<String>): Int {
        val numbers = lines
            .map { Integer.parseInt(it) }
            .sorted()
        for (i in numbers.indices) {
            for (j in numbers.subList(i + 1, numbers.size).indices) {
                if (numbers[i] + numbers[j] > 2020) break
                for (k in numbers.subList(j + 1, numbers.size).indices) {
                    if (numbers[i] + numbers[j] + numbers[k] > 2020) break
                    if (numbers[i] + numbers[j] + numbers[k] == 2020) {
                        return numbers[i] * numbers[j] * numbers[k]
                    }
                }
            }
        }
        throw IllegalArgumentException("Could not find triple adding up to 2020.")
    }
}

fun main() {
    println(Day1.partOne(readFileLines(1, year = 2020)))
    println(Day1.partTwo(readFileLines(1, 2020)))
}