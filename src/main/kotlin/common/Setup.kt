package common

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

object Setup {


    val sessionCookie: String = TODO("Provide your session key here")

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
        File("./src/main/resources/Y$year/day$day.txt")
            .writeText(content)
    }

    fun writeTestClass(year: Int, day: Int) {
        require(File("./src/test/kotlin/Y$year/").exists())
        val testClassCode = """
            package Y$year

            import org.junit.jupiter.api.Test
            import org.junit.jupiter.api.Assertions.*

            internal class Day${day}Test {

                val testData = ""${'"'}
                
                ""${'"'}.trimIndent().lines()

                @Test
                internal fun partOne() {
                    //assertEquals(, Y$year.Day$day.partOne(testData))
                }
                @Test
                internal fun partTwo() {
                    //assertEquals(,Y$year.Day$day.partTwo(testData))
                }
            }
        """.trimIndent()
        File("./src/test/kotlin/Y$year/Day${day}Test.kt")
            .writeText(testClassCode)
    }

    fun writeClass(year: Int, day: Int) {
        val classCode = """
            package Y$year

            import readFileLines

            object Day$day {
                fun partOne(lines: List<String>): Int = TODO()
                fun partTwo(lines: List<String>): Int = TODO()
            }

            fun main() {
                println(Day${day}.partOne(readFileLines($day)))
                println(Day${day}.partTwo(readFileLines($day)))
            }
        """.trimIndent()
        File("./src/main/kotlin/Y$year/Day${day}.kt")
            .writeText(classCode)
    }

    fun forDate(day: Int, month: Int = Calendar.DECEMBER, year: Int = Calendar.getInstance().get(Calendar.YEAR)) {
        require(year >= 2016 && month == Calendar.DECEMBER) { "There  exists no puzzle for today" }
        val inputData = downloadFileContent(
            URI.create("https://adventofcode.com/$year/day/$day/input")
        )
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
    Setup.forDate(25)
}