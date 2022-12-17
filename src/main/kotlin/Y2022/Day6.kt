package Y2022

import readFileText

class Day6 {
    companion object {
        /**
         * Add a subroutine to the device that detects a start-of-packet marker
         * Indicated by a sequence of four characters that are all different
         */

        fun startOfPacket(packetData: String, length: Int) =
            packetData
                .windowed(length)
                .indexOfFirst { it.toSet().size == length } + length

        fun partOne(fileText: String) = startOfPacket(fileText, 4)
        fun partTwo(fileText: String) = startOfPacket(fileText, 14)
    }
}

fun main() {
    println(Day6.partTwo(readFileText(6)))
}