package y2023

import common.extensions.mapSecond
import common.extensions.takePair
import common.readFileText

object Day15 {

    private fun String.asciHash(): Int = this.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

    fun partOne(input: String): Int = input.split(",").sumOf { it.asciHash() }

    fun partTwo(input: String): Long {
        val boxes = List(256) { LinkedHashMap<String, Int>() } // LinkedHashMap preserves insertion order
        for (instruction in input.split(",")) {
            if ("=" in instruction) {
                val (label, focalLength) = instruction.split("=").takePair().mapSecond { it.toInt() }
                val hash = label.asciHash()
                if (boxes[hash].contains(label)) boxes[hash].replace(label, focalLength)
                else boxes[hash][label] = focalLength
            } else {
                val label = instruction.dropLast(1)
                boxes[label.asciHash()].remove(label)
            }
        }

        return boxes.withIndex().sumOf { (i, boxMap) ->
            boxMap.values.withIndex().sumOf { (slot, focalLength) ->
                (1 + i) * (slot + 1) * focalLength.toLong()
            }
        }

    }
}

fun main() {
    val input = readFileText(15, 2023)
    println(Day15.partOne(input))
    println(Day15.partTwo(input))
}