package y2025

import common.readFileLines
import common.toLong

object Day1 {
    fun partOne(lines: List<String>): Long {
        var counter = 50
        return lines.count { line ->
            val num = line.substring(1).toInt()
            counter += if (line[0] == 'L') -num else num
            counter = Math.floorMod(counter, 100) // deals with negatives
            return@count counter == 0
        }.toLong()
    }

    fun partTwo(lines: List<String>): Long {
        var counter = 50
        return lines.sumOf { instruction -> // instruction is something like L90 or R630
            val num = instruction.substring(1).toInt()
            // We increase the counter in three cases:
            // 1. All 100 steps we move over 0 anyway.
            // 2. If we move into negatives -1..99 or over 99.
            //      Except, if we were on zero and move into -1..-99 it doesn't count (was counted in 3.).
            // 3. If we just land on 0.
            val rest = num % 100 // num is allways positive
            val wasZero = counter == 0
            counter += if (instruction[0] == 'L') -rest else rest
            // Case 2 and 3.
            val overOnOrZero = ((!wasZero && counter < 0) || counter > 99 || counter == 0)
            // floorMod to handle negatives correctly. The default mod does: -1 % 10 = -1
            counter = Math.floorMod(counter, 100)
            // Add the Case 1 turns using integer division to round down.
            return@sumOf (num / 100) + overOnOrZero.toLong()
        }
    }
}

fun main() {
    val input = readFileLines(1, 2025)
    println(Day1.partOne(input))
    println(Day1.partTwo(input))
}