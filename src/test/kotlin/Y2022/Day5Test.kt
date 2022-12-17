package Y2022

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class Day5Test {

    val testData =
        """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2"""

    @Test
    fun partOne() {
        assertEquals("CMZ", Day5.partOne(testData))
    }
}