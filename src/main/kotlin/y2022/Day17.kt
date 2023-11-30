package y2022

import common.*
import kotlin.math.abs
import kotlin.math.max

object Day17 {

    sealed class Figure(highestRock: Int, val pointOffsets: List<Point2D>) {
        var position: Point2D = (2 x2y initialY(highestRock))

        private fun initialY(highestRock: Int): Int =
            highestRock + 3 + pointOffsets.maxOf { abs(it.y) }

        fun moveLeft() = position.x--
        fun moveRight() = position.x++
        fun moveDown() = position.y--
        fun moveUp() = position.y++

        fun points() = pointOffsets.map { position + it }
        inline fun any(predicate: (Point2D) -> Boolean) = pointOffsets.any { predicate(position + it) }
    }

    class Minus(highestRock: Int) : Figure(
        highestRock,
        listOf(
            0 x2y 0, 1 x2y 0, 2 x2y 0, 3 x2y 0
        )
    )

    class Plus(highestRock: Int) : Figure(
        highestRock, listOf(
            1 x2y 0,
            0 x2y -1, 1 x2y -1, 2 x2y -1,
            1 x2y -2
        )
    )

    class LShape(highestRock: Int) : Figure(
        highestRock, listOf(
            2 x2y 0,
            2 x2y -1,
            0 x2y -2, 1 x2y -2, 2 x2y -2,
        )
    )

    class IShape(highestRock: Int) : Figure(
        highestRock, listOf(
            0 x2y 0,
            0 x2y -1,
            0 x2y -2,
            0 x2y -3
        )
    )

    class Square(highestRock: Int) : Figure(
        highestRock, listOf(
            0 x2y 0, 1 x2y 0,
            0 x2y -1, 1 x2y -1
        )
    )

    private fun MutableSet<Point2D>.fallDown(
        figure: Figure,
        cyclicGasJets: CyclicIterator<Char>,
        highestRock: Int
    ): Int {
        while (true) {
            val nextMove = cyclicGasJets.next()
            if (nextMove == '<') figure.moveLeft() else figure.moveRight()
            if (figure.any { it.x < 0 || it.x > 6 || it in this }) {
                // Undo operation
                if (nextMove == '<') figure.moveRight() else figure.moveLeft()
            }
            figure.moveDown()
            if (figure.any { it.y == -1 || it in this }) {
                figure.moveUp()
                break
            }
        }
        val finalPositions = figure.points()
        this.addAll(finalPositions)
        return max(highestRock, finalPositions.maxOf { it.y })
    }

    private data class Situation(val ceiling: List<Int>, val gasIdx: Int)
    private data class Loop(val lastTurn: Long, val lastHeight: Int)

    private fun handleLoop(
        loop: Loop,
        currTurn: Long,
        highestRock: Int,
        totalTurns: Long
    ): Pair<Long, Long> {
        val loopLength = currTurn - loop.lastTurn
        val loopHeight = highestRock - loop.lastHeight // how much height added by the loop
        val nowRemaining = totalTurns - currTurn
        val loopMult: Long = nowRemaining / loopLength
        val nextTurn: Long = currTurn + loopLength * loopMult
        return nextTurn to loopHeight * loopMult
    }

    fun simulate(
        gasJetString: String,
        totalTurns: Long
    ): Long {
        val map = mutableSetOf<Point2D>()

        val cyclicGasJets: CyclicIterator<Char> = cyclicIteratorOf(gasJetString.asIterable())
        val cyclicFigureGenerator = cyclicIteratorOf(listOf(::Minus, ::Plus, ::LShape, ::IShape, ::Square))
        var highestRock: Int = -1

        val seenSituations = mutableMapOf<Situation, Loop>()
        var loopHeightAddition = 0L
        var looped = false
        var currTurn = 0L
        while (currTurn < totalTurns) {
            val figure: Figure = cyclicFigureGenerator.next()(highestRock + 1)
            if (figure is Minus) {
                val situation = Situation(map.ceilingPattern(), cyclicGasJets.currentIdx)
                if (situation in seenSituations && !looped) {
                    val (nextTurn, addedHeight) = handleLoop(
                        seenSituations.getValue(situation),
                        currTurn,
                        highestRock,
                        totalTurns
                    )
                    currTurn = nextTurn
                    loopHeightAddition = addedHeight
                    looped = true
                } else seenSituations[situation] = Loop(currTurn, highestRock)
            }
            highestRock = map.fallDown(figure, cyclicGasJets, highestRock)
            currTurn++
        }

        return highestRock + 1 + loopHeightAddition
    }

    private fun MutableSet<Point2D>.ceilingPattern(): List<Int> {
        val bottom = (0 until 7).map { x ->
            filter { it.x == x }.maxOfOrNull { it.y } ?: 0
        }
        val lowestPoint = bottom.minOf { it }
        return bottom.map { it - lowestPoint } // normalize to zero
    }

    fun partOne(gasJetString: String) = simulate(gasJetString, 2022)

    fun partTwo(gasJetString: String): Long = simulate(gasJetString, 1000000000000L)
}

fun main() {
    println(Day17.partOne(readFileText(17, 2022)))
    println(Day17.partTwo(readFileText(17, 2022)))
}