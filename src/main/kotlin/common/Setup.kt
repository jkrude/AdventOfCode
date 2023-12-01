package common

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

object Setup {


    val sessionCookie: String =
        Setup.javaClass.getResource("../session.env")?.readText() ?: error("Could not read session.env file")

    fun downloadFileContent(fromWhere: URI): String {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(fromWhere)
            .setHeader("Cookie", sessionCookie)
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());

        require(response.statusCode() == 200) { "Bad response : ${response.statusCode()}" }
        return response.body().trimEnd() // remove last empty line
    }

    fun writeInputData(year: Int, day: Int, content: String) {
        File("./src/main/resources/y$year/day$day.txt")
            .writeText(content)
    }

    fun writeTestClass(year: Int, day: Int, readLines: Boolean = true) {
        require(File("./src/test/kotlin/y$year/").exists())
        val testClassCode = """
            package y$year

            import org.junit.jupiter.api.Test
            import org.junit.jupiter.api.Assertions.*

            internal class Day${day}Test {

                val testData = ""${'"'}
                
                ""${'"'}.trimIndent()${if (readLines) ".lines()" else ""}

                @Test
                internal fun partOne() {
                    //assertEquals(L, y$year.Day$day.partOne(testData))
                }
                @Test
                internal fun partTwo() {
                    //assertEquals(L,y$year.Day$day.partTwo(testData))
                }
            }
        """.trimIndent()
        File("./src/test/kotlin/y$year/Day${day}Test.kt")
            .writeText(testClassCode)
    }

    fun writeClass(year: Int, day: Int, readLines: Boolean = true) {
        val argument = if (readLines) "lines: List<String>" else "input: String"
        val readInput = if (readLines) "readFileLines($day,$year)" else "readFileText($day,$year)"
        val import = if (readLines) "readFileLines" else "readFileText"
        val classCode = """
            package y$year

            import common.$import

            object Day$day {
                fun partOne($argument): Long = TODO()
                fun partTwo($argument): Long = TODO()
            }

            fun main() {
                val input = $readInput
                println(Day${day}.partOne(input))
                //println(Day${day}.partTwo(input))
            }
        """.trimIndent()
        val file = File("./src/main/kotlin/y$year/Day${day}.kt")
        file.createNewFile()
        file.writeText(classCode)
    }

    private fun createFoldersIfNotExists(year: Int) {
        listOf(
            "./src/main/kotlin/y$year", "./src/main/resources/y$year",
            "./src/test/kotlin/y$year", "./src/test/resources/y$year"
        ).forEach {
            val file = File(it)
            if (!file.exists()) file.mkdir()
        }
    }

    fun forDate(
        day: Int,
        month: Int = Calendar.DECEMBER,
        year: Int = Calendar.getInstance().get(Calendar.YEAR),
        readLines: Boolean = true
    ) {
        require(year >= 2016 && month == Calendar.DECEMBER) { "There  exists no puzzle for today" }
        val inputData = downloadFileContent(
            URI.create("https://adventofcode.com/$year/day/$day/input")
        )
        createFoldersIfNotExists(year)
        writeInputData(year, day, inputData)
        writeClass(year, day, readLines)
        writeTestClass(year, day, readLines)
    }

    fun forToday(readLines: Boolean = true) = forDate(
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.YEAR),
        readLines
    )

}

fun main() {
    Setup.forToday()
}