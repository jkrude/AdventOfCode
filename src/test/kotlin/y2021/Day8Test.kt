package y2021

import Day8
import junit.framework.TestCase

class Day8Test : TestCase() {

    private val testData = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent().split("\n")

    fun testPartOne() {
        assertEquals(26, Day8.partOne(Day8.readReport(testData)))
    }

    fun testClassification() {

        val classificationData = listOf(8394, 9781, 1197, 9361, 4873, 8418, 4548, 1625, 8717, 4315)

        for ((idx, line) in testData.withIndex())
            assertEquals(classificationData[idx], Day8.partTwo(Day8.readReport(listOf(line))))
    }


    fun testPartTwo() {
        val testDataSmall =
            listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")
        assertEquals(5353, Day8.partTwo(Day8.readReport(testDataSmall)))


        assertEquals(61229, Day8.partTwo(Day8.readReport(testData)))

    }
}