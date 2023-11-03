package common

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

object Setup {


    val sessionCookie: String =
        "session=YOUR COOKIE HERE"

    fun downloadFileContent(fromWhere: URI): String {
        val client = HttpClient.newBuilder().build();
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

    fun writeTestClass(year: Int, day: Int) {
        require(File("./src/test/kotlin/y$year/").exists())
        val testClassCode = """
            package y$year

            import org.junit.jupiter.api.Test
            import org.junit.jupiter.api.Assertions.*

            internal class Day${day}Test {

                val getTestData = ""${'"'}
                
                ""${'"'}.trimIndent().lines()

                @Test
                internal fun partOne() {
                    //assertEquals(, y$year.Day$day.partOne(getTestData))
                }
                @Test
                internal fun partTwo() {
                    //assertEquals(,y$year.Day$day.partTwo(getTestData))
                }
            }
        """.trimIndent()
        File("./src/test/kotlin/y$year/Day${day}Test.kt")
            .writeText(testClassCode)
    }

    fun writeClass(year: Int, day: Int) {
        val classCode = """
            package y$year

            import common.readFileLines

            object Day$day {
                fun partOne(lines: List<String>): Int = TODO()
                fun partTwo(lines: List<String>): Int = TODO()
            }

            fun main() {
                println(Day${day}.partOne(readFileLines($day,$year)))
                //println(Day${day}.partTwo(readFileLines($day,$year)))
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

    fun forDate(day: Int, month: Int = Calendar.DECEMBER, year: Int = Calendar.getInstance().get(Calendar.YEAR)) {
        require(year >= 2016 && month == Calendar.DECEMBER) { "There  exists no puzzle for today" }
        val inputData = downloadFileContent(
            URI.create("https://adventofcode.com/$year/day/$day/input")
        )
        createFoldersIfNotExists(year)
        writeInputData(year, day, inputData)
        writeClass(year, day)
        writeTestClass(year, day)
    }

    fun forToday() = forDate(
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.YEAR)
    )

}

fun main() {
    Setup.forDate(7, year = 2020)
}