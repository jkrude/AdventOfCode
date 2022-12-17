import kotlin.math.min

class Day17 {

    companion object {

        class Gy(private val vy0: Int) {
            operator fun invoke(z: Int) = vy0 * (z + 1) - (z * (z + 1) / 2)
        }

        class Gx(private val vx0: Int) {
            operator fun invoke(z: Int) = (0..z).sumOf { if (vx0 > it) vx0 - it else 0 }
        }

        private fun existsZ(it: Pair<Int, Int>, xRange: IntProgression, yRange: IntProgression): Boolean {
            val (gx, gy) = Gx(it.first) to Gy(it.second)
            return (0..200).any { gx(it) in xRange && gy(it) in yRange }
        }

        fun highestY(initialVelocity: Int): Int = Gy(initialVelocity)(initialVelocity)

        fun partOne(xRange: IntProgression, yRange: IntProgression): Pair<Int, Int> {

            val searchSpace = (0 to 0)..(200 to 200)
            var currBest = searchSpace.first()
            for (initialVel in searchSpace) {
                if (highestY(currBest.second) < highestY(initialVel.second)) {
                    if (existsZ(initialVel, xRange, yRange)) currBest = initialVel
                }
            }
            return currBest
        }

        fun partTwo(xRange: IntProgression, yRange: IntProgression): Int =
            ((0 to min(0, yRange.minOf { it }))..(200 to 200)).count { existsZ(it, xRange, yRange) }
    }
}

fun main() {
    val xRange = 144..178
    val yRange = -100..-76
    val (x, y) = Day17.partOne(xRange, yRange)
    println("Best: $x,$y with highest: ${Day17.highestY(y)}")
    println("There are ${Day17.partTwo(xRange, yRange)} many start velocities")
}