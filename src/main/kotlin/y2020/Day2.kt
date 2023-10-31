package y2020

import common.readFileLines

data class PasswordLine(
    val from: Int,
    val to: Int,
    val character: Char,
    val password: String
) {
    fun frequencyValid() = password.count { it == character } in from..to

    fun positionValid() =
        (password[from - 1] == character) xor (password[to - 1] == character)

}

fun parseLine(line: String): PasswordLine {
    val (start, password) = line.split(": ")
    val (fromTo, char) = start.split(" ")
    val (from, to) = fromTo.split("-")
    return PasswordLine(from.toInt(), to.toInt(), char.first(), password)
}

object Day2 {
    fun partOne(lines: List<String>): Int {
        return lines.map { parseLine(it) }.count { it.frequencyValid() }
    }

    fun partTwo(lines: List<String>) = lines.map { parseLine(it) }.count { it.positionValid() }
}

fun main() {
    println(Day2.partOne(readFileLines(2, 2020)))
    println(Day2.partTwo(readFileLines(2, 2020)))
}