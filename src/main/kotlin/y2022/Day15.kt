package y2022

import common.Point2D
import common.extensions.findGroupValues
import common.readFileLines
import common.x2y
import java.util.concurrent.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

object Day15 {

    private fun parseLines(lines: List<String>): List<Pair<Point2D, Point2D>> {
        val pattern = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)"""
            .toRegex()
        return lines.map { line ->
            val (x1, y1, x2, y2) = pattern.findGroupValues(line).map(String::toInt)
            (x1 x2y y1) to (x2 x2y y2)
        }
    }

    fun getCoveredArea(sensor: Point2D, beacon: Point2D): (Point2D) -> Boolean {
        val distance = sensor.manhattanDistanceTo(beacon)
        return { point ->
            sensor.manhattanDistanceTo(point) <= distance
        }
    }

    fun partOne(lines: List<String>, yLevel: Int = 2_000_000): Int {
        val sensorSignalPairs = parseLines(lines)

        val covered = mutableSetOf<Int>() // x coordinates in y covered by signals
        for ((signal, beacon) in sensorSignalPairs) {
            val saveRadius = signal.manhattanDistanceTo(beacon)
            val remaining = saveRadius - abs(signal.y - yLevel)
            if (remaining >= 0) {
                covered.add(signal.x)
                for (i in 1..remaining) {
                    covered.add(signal.x + i)
                    covered.add(signal.x - i)
                }
            }
        }
        val beaconsAtRowLevel = sensorSignalPairs
            .filter { it.second.y == yLevel }
            .map { (_, b) -> b.x }.toSet()
        return (covered - beaconsAtRowLevel).size

    }


    fun visualize(
        topLeft: Point2D, bottomRight: Point2D,
        points: List<Point2D>,
        sprite: (Point2D) -> String = Any::toString
    ) {
        val width = (bottomRight.x - topLeft.x) + 1
        val height = (bottomRight.y - topLeft.y) + 1
        val map = Array(height) { Array(width) { "." } }
        val offsetX = abs(topLeft.x)
        val offsetY = abs(topLeft.y)
        points.forEachIndexed { idx, point ->
            val (x, y) = point
            if (x in topLeft.x..bottomRight.x && y in topLeft.y..bottomRight.y) {
                map[offsetY + y][offsetX + x] = sprite(point)
            }
        }
        println("   " + (topLeft.x..bottomRight.x).joinToString(" ") { (Math.floorMod(it, 10).toString()) })
        println(map.withIndex().joinToString("\n") {
            "${it.index}" + (if (it.index < 10) " " else "") + ": ${
                it.value.joinToString(" ")
            }"
        })
    }


    class SearchWorker(
        private val sensors: List<Point2D>,
        private val radiusPerSignal: List<Int>,
        private val startY: Int,
        private val endY: Int,
        private val maxX: Int
    ) : Callable<Point2D?> {
        override fun call(): Point2D? {
            for (y in startY..endY) {
                var x = 0
                while (x <= maxX) {
                    // Move x as far to the right as possible
                    val remaining = maxMove(x, y) ?: return x x2y y
                    x = (remaining + 1) // Go to first unreachable position
                }
            }
            return null
        }

        private fun distance(x1: Int, y1: Int, other: Point2D): Int {
            return abs(x1 - other.x) + abs(y1 - other.y)
        }

        private fun maxMove(x: Int, y: Int): Int? {
            var maxX = -1
            for (i in sensors.indices) {
                val signal = sensors[i]
                val radius = radiusPerSignal[i]
                if (distance(x, y, signal) <= radius) {
                    val rem = radius - abs(y - signal.y)
                    maxX = max(maxX, signal.x + rem)
                }
            }
            return if (maxX == -1) null else maxX
            // This would be the nice kotlin variant
            //(y2021.map of signal to radius).filter { (point, radius) ->
            //    point2D.manhattanDistanceTo(point) <= radius
            //}.maxOfOrNull { (signal, radius) ->
            //    val rem = radius - abs(point2D.y - signal.y)
            //    signal.x + rem
            //}
        }
    }

    fun partTwo(lines: List<String>, searchSpace: Int = 4_000_000): Long {
        val sensorSignalPairs = parseLines(lines)
        val radiusPerSignal: List<Int> =
            sensorSignalPairs.map { (signal, beacon) -> signal.manhattanDistanceTo(beacon) }
        val signals = sensorSignalPairs.map { it.first }
        val threads = Runtime.getRuntime().availableProcessors()
        val sizePerJob = threads * 100
        val numberOfJobs: Int = ceil(searchSpace / sizePerJob.toDouble()).roundToInt()
        val searchSpaces = (0 until numberOfJobs).map {
            (it * sizePerJob) to ((it + 1) * sizePerJob)
        }
        val executor = ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, LinkedBlockingDeque())
        val completionService = ExecutorCompletionService<Point2D?>(executor)

        for (job in searchSpaces) {
            completionService.submit(
                SearchWorker(
                    signals,
                    radiusPerSignal,
                    job.first,
                    job.second,
                    searchSpace
                )
            )
        }
        for (waitingFor in numberOfJobs downTo 1) {
            val distressSignal = completionService.take().get() ?: continue
            executor.shutdownNow()
            return (distressSignal.x.toLong() * 4_000_000L) + distressSignal.y.toLong()
        }
        throw IllegalStateException("No job found distress signal")
    }
}

fun main() {
//    println(y2021.Day15.y2021.partOne(y2021.readFileLines(1,20225)))
    println(Day15.partTwo(readFileLines(1, 20225)))
}