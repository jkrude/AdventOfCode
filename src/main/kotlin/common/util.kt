package common

import java.io.File


fun readFileLines(day: Int, year: Int): List<String> = readFile(day, year).readLines()
fun readFile(day: Int, year: Int): File =
    File("src/main/resources/Y$year/day$day.txt")

fun readFileText(day: Int, year: Int): String = readFile(day, year).readText()

fun Regex.findOverlapping(text: String, startIndex: Int = 0): MutableList<String> {
    val allMatches = mutableListOf<String>()
    var i = startIndex
    do {
        val match = find(text, i) ?: break
        i = match.range.first + 1
        allMatches.add(match.value);
    } while (true);
    return allMatches

}