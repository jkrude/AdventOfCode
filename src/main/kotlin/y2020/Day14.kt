package y2020

import common.readFileLines

object Day14 {

    private fun parseMaskLine(line: String) = line.removePrefix("mask = ")

    fun ULong.applyBitmask(line: String): ULong {
        val mask = parseMaskLine(line)
        val bitString = StringBuilder(this.toString(2).padStart(36, '0'))
        for ((index, x) in mask.withIndex()) {
            when (x) {
                'X' -> continue
                else -> bitString.setCharAt(index, x)
            }
        }
        return bitString.toString().toULong(2)
    }


    fun partOne(lines: List<String>): ULong {
        var bitMask: String = lines.first()
        val instructions = lines.drop(1)
        val mem: MutableMap<ULong, ULong> = mutableMapOf()
        val r = Regex("""mem\[(\d+)\] = (\d+)""")

        for (op in instructions) {
            if (op.startsWith("mask")) {
                bitMask = op
                continue
            }
            val (address, nbr) = r.find(op)?.groupValues?.drop(1)?.map { it.toULong() }
                ?: throw IllegalArgumentException(op)
            mem[address] = nbr.applyBitmask(bitMask)
        }
        return mem.values.sum()
    }

    // Resolve the possible addresses by replacing X's both by '0' and '1'
    fun partTwo(lines: List<String>): ULong {
        var bitMask: String = parseMaskLine(lines.first())
        val instructions = lines.drop(1)
        val mem: MutableMap<ULong, ULong> = mutableMapOf()
        val r = Regex("""mem\[(\d+)\] = (\d+)""")

        for (op in instructions) {
            if (op.startsWith("mask")) {
                bitMask = parseMaskLine(op)
                continue
            }
            val (address, nbr) = r.find(op)?.groupValues?.drop(1)?.map { it.toULong() }
                ?: throw IllegalArgumentException(op)
            storeWithBitMask(mem, address, nbr, bitMask)
        }
        return mem.values.sum()
    }

    // Store nbr at all addresses that the applied bitMask to the address can be resolved to.
    private fun storeWithBitMask(
        mem: MutableMap<ULong, ULong>,
        address: ULong,
        nbr: ULong,
        bitMask: String
    ) {
        val allAddresses: MutableList<ULong> = mutableListOf()
        val addressStr = StringBuilder(address.toString(2).padStart(36, '0'))
        for ((index, x) in bitMask.withIndex()) {
            if (x == '1') addressStr.setCharAt(index, '1')
        }
        // Construct all possible addresses by resolving X's in bitMask[start,lastIndex]
        fun resolve(start: Int, address: String) {
            val first = bitMask.indexOf('X', start)
            if (first == -1) { // There is no more unknown -> fully deterministic address
                allAddresses.add(address.toULong(2))
                return
            }
            val a = StringBuilder(address)
            a.setCharAt(first, '0')
            resolve(first + 1, a.toString())
            a.setCharAt(first, '1')
            resolve(first + 1, a.toString())
        }

        resolve(0, addressStr.toString())
        for (addr in allAddresses) {
            mem[addr] = nbr
        }
    }
}

fun main() {
    println(Day14.partOne(readFileLines(14, 2020)))
    println(Day14.partTwo(readFileLines(14, 2020)))
}