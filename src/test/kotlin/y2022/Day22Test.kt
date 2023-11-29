package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    val testData =
        """        ...#    
        .#..    
        #...    
        ....    
...#.......#    
........#...    
..#....#....    
..........#.    
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5"""

    @Test
    internal fun partOne() {
        assertEquals(6032, y2022.Day22.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        //assertEquals(,Y2022.Day22.partTwo(testData))
    }
}