package y2024

import common.algorithms.Search
import common.readFileText
import java.math.BigInteger

object Day17 {
    class Computer(
        var regA: Int,
        var regB: Int,
        var regC: Int,
    ) {
        var instructionPointer = 0
        val outputBand = mutableListOf<Int>()
        val output: String get() = outputBand.joinToString(",")

        fun comboValue(operand: Int): Int = when (operand) {
            0, 1, 2, 3 -> operand
            4 -> regA
            5 -> regB
            6 -> regC
            7 -> error("7 should not appear in a valid program")
            else -> error("unknown operand $operand")
        }

        fun applyInstruction(opcode: Int, operand: Int) {
            require(opcode in 0..7 && operand in 0..7)
            val func = when (opcode) {
                0 -> ::adv
                1 -> ::bxl
                2 -> ::bst
                3 -> ::jnz
                4 -> ::bxc
                5 -> ::out
                6 -> ::bdv
                7 -> ::cdv
                else -> error("unknown opcode $opcode")
            }
            if (opcode == 3) {
                if (!jnz(operand)) instructionPointer += 2
            } else {
                func(operand)
                incInst()
            }
        }

        fun exec(instructions: List<Int>): String {
            while (this.instructionPointer < instructions.lastIndex) {
                val opCode = instructions[this.instructionPointer]
                val operand = instructions[this.instructionPointer + 1]
                this.applyInstruction(opCode, operand)
            }
            return this.output
        }

        fun incInst() {
            instructionPointer += 2
        }

        fun adv(operand: Int) {
            regA = regA shr comboValue(operand)
        }

        fun bxl(operand: Int) {
            regB = regB xor operand
        }

        fun bst(operand: Int) {
            regB = comboValue(operand) % 8
        }

        fun jnz(operand: Int): Boolean {
            if (regA == 0) return false
            instructionPointer = operand
            return true
        }

        // operand is included for legacy reasons
        fun bxc(operand: Int) {
            regB = regB xor regC
        }

        fun out(operand: Int) {
            outputBand.add(comboValue(operand) % 8)
        }

        fun bdv(operand: Int) {
            regB = regA shr comboValue(operand)
        }

        fun cdv(operand: Int) {
            regC = regA shr comboValue(operand)
        }
    }

    fun parse(input: String): Pair<Computer, List<Int>> {
        val (initialRegister, instructionsStr) = input.split("\n\n")
        val (regA, regB, regC) = initialRegister.split("\n").map { it.split(": ")[1].toInt() }
        val instructions = instructionsStr
            .removePrefix("Program: ")
            .split(",")
            .map { it.toInt() }
        return Computer(regA, regB, regC) to instructions
    }

    fun partOne(input: String): String {
        val (computer, instructions) = parse(input)
        return computer.exec(instructions)
    }

    class SearchNode(
        val regABitList: List<Char>, // the bits representing register A.
        val regAPointer: Int = 0, // The point from which the current cycle reads.
        val instructionPointer: Int = 0, // The instruction where we are at.
    )

    class SmallestRegisterSearch(val instructions: List<Int>) {
        val successfulRegisterValues = mutableListOf<List<Char>>()

        fun checkRegisterAInitialization(searchNode: SearchNode): List<SearchNode> {
            require(searchNode.instructionPointer in instructions.indices)

            // X if no condition was placed on this bit, otherwise, the bit value (0 or 1).
            // Bits for register A as a_31,...,a_2.,a_1,a_0, dropping the last three every instruction loop.
            val regABitList: MutableList<Char> = searchNode.regABitList.toMutableList()
            var rightPointer = searchNode.regAPointer

            var instructionPointer = searchNode.instructionPointer
            // Get the bit-index of register A starting from the least significant position (right)
            fun idxFromRight(idx: Int) = rightPointer - idx
            fun regABitFromRight(idx: Int) = regABitList[idxFromRight(idx)]
            fun registerABits() = regABitList.map { if (it == 'X') '0' else it }
            fun bitListToInt(bitList: List<Char>): Int {
                require(bitList.size <= 32) // can only create Int
                return bitList.joinToString("").toInt(radix = 2)
            }

            while (instructionPointer < instructions.size) {
                val instruction = instructions[instructionPointer]
                // a0, a1, a2
                val lastThreeBits: List<Char> = (0..2).map(::regABitFromRight)
                for ((i, bit) in lastThreeBits.withIndex()) {
                    // We have encountered a bit for which no condition was previously registered.
                    if (bit == 'X') {
                        return listOf(
                            SearchNode(
                                regABitList.toMutableList().apply { set(idxFromRight(i), element = '0') },
                                rightPointer,
                                instructionPointer
                            ),
                            SearchNode(
                                regABitList.toMutableList().apply { set(idxFromRight(i), element = '1') },
                                rightPointer,
                                instructionPointer
                            ),
                        )
                    }
                }
                // a2, a1, a0
                val regAMod8Bits = lastThreeBits.reversed()
                val shiftOffset: Int = bitListToInt(regAMod8Bits) xor 2
                if (2 + shiftOffset > rightPointer) return emptyList()

                // The three bits we have to produce
                val (x2, x1, x0) = instruction.toString(radix = 2).padStart(length = 3, padChar = '0')
                    .map { it.digitToInt() }
                // Our current set of the last three bits of the A-register.
                val (a2, a1, a0) = regAMod8Bits.map { it.digitToInt() }

                // Conditions for bit a_{2+ shiftOffset}, a_{1+shiftOffset} and a_{0+shiftOffset}
                val c2 = x2 xor (1 - a2) // not a2
                val c1 = x1 xor a1
                val c0 = x0 xor (1 - a0) // not a0
                val conditions = listOf(c0, c1, c2).map { it.digitToChar() }

                for ((i, bitCondition) in conditions.withIndex()) {
                    val prevCondition: Char = regABitFromRight(i + shiftOffset)
                    // Check if for this bit there exists a condition and it differs from the new one
                    if (prevCondition != 'X' && bitCondition != prevCondition) return emptyList()
                    regABitList[idxFromRight(i + shiftOffset)] = bitCondition
                }

                // Sanitycheck that we actually produce the correct output.
                // We only need to include a_{2 + max-shiftOffset}, .., a_2,a_1,a_0 and
                // shiftOffset can be at most 7.
                val relevantRegisterAValue =
                    bitListToInt(registerABits().subList(rightPointer - 9, rightPointer + 1))
                val cmp = Computer(relevantRegisterAValue, 0, 0)
                val out = cmp.exec(instructions.dropLast(2)) // skip the jump instruction
                require(out == instruction.toString()) { "Expected to produce $instruction but got $out" }
                // Shift registerA by 3 to the right (e.g. divide by 2^3
                rightPointer -= 3
                require(rightPointer >= 3)

                // increase the instruction pointer
                instructionPointer += 1
            }
            // We successfully generated all instructions.
            // Return the fully computed registerA value.
            this.successfulRegisterValues.add(registerABits())
            return emptyList()
        }
    }

    fun partTwo(input: String): BigInteger {
        // I manually checked the logic for my puzzle input and found the following
        // 1. There is only one jump instruction at the end of each cycle
        // 2. Each cycle reads the last three digits of registerA and discards them at the end of the loop.
        // 3. You can reverse engineer other bit positions to produce a specific output.
        // This solution starts with no assumptions about the bits of register A.
        // Whenever some bit value is used but was not previously involved we creating a branching point for the search.
        // For the length of the desired output a maximum of 128 bits is required.
        val (_, instructions) = parse(input)
        val search = SmallestRegisterSearch(instructions)
        Search.SearchBuilder(listOf(SearchNode(List(128) { 'X' }, 127, 0)))
            .neighbors(search::checkRegisterAInitialization)
            .executeDfs()
        return search.successfulRegisterValues.minOfOrNull { it.joinToString("").toBigInteger(2) }!!
    }

}

fun main() {
    val input = readFileText(17, 2024)
    println(Day17.partOne(input))
    println(Day17.partTwo(input))
}