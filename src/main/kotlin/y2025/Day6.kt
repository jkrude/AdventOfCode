package y2025

import common.extensions.Lists2D.columns
import common.extensions.product
import common.findAllLong
import common.readFileLines

object Day6 {
    private fun List<List<Long>>.aggr(ops: List<Char>): Long =
        this.zip(ops).sumOf { (numbers, op) ->
            if (op == '+') numbers.sum() else numbers.product()
        }

    fun partOne(lines: List<String>): Long {
        val worksheet = lines.dropLast(1).map { it.findAllLong().toList() }.columns()
        val operators = lines.last().filter { it != ' ' }.toList()
        return worksheet.aggr(operators)
    }

    fun partTwo(lines: List<String>): Long {
        // we don't have to reverse (right to left) as + and * are commutative
        val worksheet: List<List<Char>> = lines.dropLast(1).map { it.toList() }.columns()
        val operators: List<Char> = lines.last().filter { it != ' ' }.toList()
        // Group each block of columns, split by columns of only white space.
        val chunks = mutableListOf(mutableListOf<Long>())
        for (col in worksheet) {
            if (col.all { it == ' ' }) {
                chunks.add(mutableListOf())
            } else {
                chunks.last().add(col.joinToString("").trim().toLong())
            }
        }
        return chunks.aggr(operators)
    }
}

fun main() {
    val input = readFileLines(6, 2025)
    println(Day6.partOne(input))
    println(Day6.partTwo(input))
}