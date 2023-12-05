package y2023

import arrow.core.Either
import common.extensions.forBoth
import common.extensions.intersect
import common.extensions.subtract
import common.readFileText

object Day5 {

    data class RangeMapping(val range: LongRange, val destinationDiff: Long)

    class Mapping(val from: String, val to: String, rangesLists: List<List<Long>>) {

        val ranges: List<RangeMapping>

        init {
            ranges = rangesLists.map { (toStart, sourceStart, length) ->
                RangeMapping(sourceStart until sourceStart + length, toStart - sourceStart)
            }
        }

        operator fun get(fromValue: Long): Long {
            val r = ranges.firstOrNull { rangeMapping -> rangeMapping.range.contains(fromValue) } ?: return fromValue
            return fromValue + r.destinationDiff
        }

    }

    //destination range start, the source range start, and the range length
    fun parse(input: String): Pair<List<Long>, List<Mapping>> {
        val mapsStr = input.split("\n\n")
        val (seedsStr, rest) = mapsStr.first() to mapsStr.drop(1)
        val mappings = rest.map { mapStr ->
            val (headerStr, body) = mapStr.split(":\n")
            val header = headerStr.substringBefore(" map")
            val (from, to) = header.split("-to-")
            val ranges = body.lines().map { bodyLine -> bodyLine.split(" ").map { it.toLong() } }
            Mapping(from, to, ranges)
        }
        return seedsStr.substringAfter(": ").split(" ").map(String::toLong) to mappings
    }

    private fun List<Mapping>.seedToLocation(seed: Long): Long =
            this.fold(seed) { acc, mapping -> mapping[acc] }


    //find the lowest location number that corresponds to any of the initial seeds
    fun partOne(input: String): Long {
        val (seeds, mappings) = parse(input)
        return seeds.minOf { mappings.seedToLocation(it) }
    }

    private fun split(seedRange: LongRange, mappings: List<Mapping>): List<LongRange> {

        val splicedRange = mutableListOf<LongRange>()

        fun splitRec(range: LongRange, index: Int) {
            var currRange = range
            if (index == mappings.size) {
                splicedRange.add(currRange)
                return
            }
            for ((sourceRange, diff) in mappings[index].ranges) {
                val inter = currRange intersect sourceRange
                if (inter == LongRange.EMPTY) continue
                splitRec(inter.first + diff..inter.last + diff, index + 1)
                when (val diffRange = currRange subtract inter) {
                    is Either.Left -> currRange = diffRange.value
                    is Either.Right -> {
                        diffRange.value.forBoth { splitRec(it, index) }
                        return
                    }
                }
            }
            if (currRange != LongRange.EMPTY) splitRec(currRange, index + 1)
        }

        splitRec(seedRange, 0)
        return splicedRange

    }


    fun partTwo(input: String): Long {
        val (seeds, mappings) = parse(input)
        val seedRanges: List<LongRange> = seeds.chunked(2).map { it.first() until it.first() + it.last() }

        return seedRanges.flatMap { split(it, mappings).map { subrange -> subrange.first } }.min()
    }
}

fun main() {
    val input = readFileText(5, 2023)
    println(Day5.partOne(input))
    println(Day5.partTwo(input))
}