package Y2022

import common.CyclicIterator
import common.Point2D
import common.cyclicIteratorOf
import common.x2y
import readFileLines
import kotlin.math.max

object Day17 {

    sealed class Figure(
        highestBottom: Int,
        protected val position: Point2D = 2 x2y (highestBottom + 4)
    ) {
        protected abstract val width: Int
        abstract fun canMoveDown(bottom: Array<Int>): Boolean
        fun moveLeft() {
            position.x = max(position.x - 1, 0)
        }

        fun moveRight() {
            if (position.x + width != 7) position.x++
        }

        abstract fun comeToHalt(bottom: Array<Int>)

        fun moveDownOrComeToHalt(bottom: Array<Int>): Boolean {
            require(position.y > 0)
            val moved = canMoveDown(bottom)
            if (moved) position.y -= 1
            else comeToHalt(bottom)
            return moved
        }
    }

    class Minus(highestBottom: Int) : Figure(highestBottom) {
        override val width: Int = 4
        override fun canMoveDown(bottom: Array<Int>): Boolean {
            for (i in position.x until (position.x + width)) {
                if (bottom[i] == position.y - 1) return false
            }
            return true
        }

        override fun comeToHalt(bottom: Array<Int>) {
            for (i in position.x until (position.x + width)) {
                bottom[i] = position.y
            }
        }
    }

    class Plus(highestBottom: Int) : Figure(highestBottom) {
        override val width: Int = 3
        override fun canMoveDown(bottom: Array<Int>): Boolean {
            return (bottom[position.x] != position.y) &&
                    (bottom[position.x + 1] != position.y - 1) &&
                    (bottom[position.x + 2] != position.y)
        }

        override fun comeToHalt(bottom: Array<Int>) {
            // We know that figure can't fall position.y = bottom.max() + 1
            // with idx of bottom.max()  in position.x until position.x + width
            bottom[position.x] = position.y + 1
            bottom[position.x + 1] = position.y + 2
            bottom[position.x + 2] = position.y + 1
        }
    }

    class LShape(highestBottom: Int) : Figure(highestBottom) {
        override val width: Int = 3
        override fun canMoveDown(bottom: Array<Int>): Boolean {
            for (i in position.x until (position.x + width)) {
                if (bottom[i] == position.y - 1) return false
            }
            return true
        }

        override fun comeToHalt(bottom: Array<Int>) {
            bottom[position.x] = position.y
            bottom[position.x + 1] = position.y
            bottom[position.x + 2] = position.y + 2
        }
    }

    class IShape(highestBottom: Int) : Figure(highestBottom) {
        override val width: Int = 1
        override fun canMoveDown(bottom: Array<Int>): Boolean =
            bottom[position.x] != position.y - 1

        override fun comeToHalt(bottom: Array<Int>) {
            bottom[position.x] = position.y + 3
        }
    }

    class Square(highestBottom: Int) : Figure(highestBottom) {
        override val width: Int = 2
        override fun canMoveDown(bottom: Array<Int>): Boolean =
            (bottom[position.x] != (position.y - 1))
                    || (bottom[position.x + 1] != (position.y - 1))


        override fun comeToHalt(bottom: Array<Int>) {
            bottom[position.x] = position.y + 1
            bottom[position.x + 1] = position.y + 1
        }
    }

    fun figureGenerator(): CyclicIterator<(Int) -> Figure> {
        return cyclicIteratorOf(
            listOf(
                { highestBottom: Int -> Minus(highestBottom) },
                { highestBottom: Int -> Plus(highestBottom) },
                { highestBottom: Int -> LShape(highestBottom) },
                { highestBottom: Int -> IShape(highestBottom) },
                { highestBottom: Int -> Square(highestBottom) },
            )
        )
    }

    fun partOne(gasJetString: String): Int {
        /**
         *
         */
        val bottom = Array(7) { 0 }
        val cyclicGasJets: CyclicIterator<Char> = cyclicIteratorOf(gasJetString.asIterable())
        val cyclicFigureGenerator = figureGenerator()

        fun fallDown() {
            val figureGenerator = cyclicFigureGenerator.next()
            val figure: Figure = figureGenerator(bottom.max())
            do {
                val nextGasJet = cyclicGasJets.next()
                if (nextGasJet == '<') figure.moveLeft()
                else figure.moveRight()
                val movedDown = figure.moveDownOrComeToHalt(bottom)
            } while (movedDown)
        }

        repeat(2022) {
            fallDown()
        }
        return bottom.max()

    }

    fun partTwo(lines: List<String>): Int = TODO()
}

fun main() {
    println(Day17.partOne(readFileLines(17).first()))
    println(Day17.partTwo(readFileLines(17)))
}