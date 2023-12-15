package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {

    val testData = """
    rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(1320, y2023.Day15.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(145L, y2023.Day15.partTwo(testData))
    }
}