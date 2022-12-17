package Y2022

import readFileLines

/**
 * two large compartments
 * failed to follow this rule for exactly one item type per rucksack
 * rucksack always has the same number of items in each of its two compartments
 * priority:
 *  Lowercase item types a through z have priorities 1 through 26.
 *  Uppercase item types A through Z have priorities 27 through 52
 */

fun priorityOfDuplicate(line: String): Int {
    line.length
    val first = line.substring(0, line.length / 2).toSet()
    val second = line.substring(line.length / 2).toSet()

    return first.intersect(second).sumOf { priority(it) }
}

fun partOne(lines: List<String>): Int = lines.sumOf { priorityOfDuplicate(it) }

fun priority(char: Char) =
    if (char.isUpperCase()) char - '@' + 26
    else char - '`'

/**
 * badge is item type B, then all three Elves will have item type B somewhere in their rucksack,
 * and at most two of the Elves will be carrying any other item type
 */
fun partTwo(lines: List<String>): Int {
    return lines.chunked(3) // group in size of 3
        .map { group ->
            group.map { it.toSet() } // map string to set of chars
                .reduce(operation = Set<Char>::intersect) // fold by intersect
        }.sumOf {
            priority(it.first())
        }
}

fun main() {
    println(partOne(readFileLines(3)))
    println(partTwo(readFileLines(3)))
}