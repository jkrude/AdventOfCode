package y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {

    val testData = """
    Register A: 729
    Register B: 0
    Register C: 0

    Program: 0,1,5,4,3,0
    """.trimIndent()

    @Test
    internal fun bst() {
        val cmp = Day17.Computer(0, 0, regC = 9)
        cmp.applyInstruction(2, 6)
        assertEquals(1, cmp.regB)
    }


    @Test
    internal fun outA_program_outputs_0_1_2() {
        val cmp = Day17.Computer(regA = 10, regB = 0, regC = 0)
        cmp.applyInstruction(5, 0)
        cmp.applyInstruction(5, 1)
        cmp.applyInstruction(5, 4)
        assertEquals("0,1,2", cmp.output)
    }

    @Test
    internal fun adv_loop_outputs_expected_and_leaves_A_zero() {
        val cmp = Day17.Computer(regA = 2024, regB = 0, regC = 0)
        // program: 0,1,5,4,3,0  repeated until regA becomes 0
        val instructions = listOf(0, 1, 5, 4, 3, 0)
        assertEquals(cmp.exec(instructions), "4,2,5,6,7,7,7,7,3,1,0")
        assertEquals(0, cmp.regA)
    }

    @Test
    internal fun bxl_xor_with_7() {
        val cmp = Day17.Computer(regA = 0, regB = 29, regC = 0)
        cmp.applyInstruction(1, 7)
        assertEquals(26, cmp.regB)
    }

    @Test
    internal fun bxc_xor_B_with_C() {
        val cmp = Day17.Computer(regA = 0, regB = 2024, regC = 43690)
        cmp.applyInstruction(4, 0)
        assertEquals(44354, cmp.regB)
    }


    @Test
    internal fun partOne() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", y2024.Day17.partOne(testData))
    }

}