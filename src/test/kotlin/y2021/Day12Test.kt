package y2021

import org.junit.jupiter.api.Assertions.assertEquals

class Day12Test {

    val testGraph1 = Day12.importData(
        """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
        """.trimIndent().split("\n")
    )
    val testGraph2 = Day12.importData(
        """
        dc-end
        HN-start
        start-kj
        dc-start
        dc-HN
        LN-dc
        HN-end
        kj-sa
        kj-HN
        kj-dc
    """.trimIndent().split("\n")
    )


    fun testPartOne() {

        assertEquals(10, Day12.partOne(testGraph1))
        assertEquals(19, Day12.partOne(testGraph2))
    }

    fun testPartTwo() {
        assertEquals(36, Day12.partTwo(testGraph1))
        assertEquals(103, Day12.partTwo(testGraph2))
    }
}