package y2022

import common.Point2D
import common.readFileText
import common.x2y

object Day22 {
    enum class Direction {
        EAST,
        SOUTH,
        WEST,
        NORTH
    }

    private fun Point2D.moveToward(direction: Direction): Point2D {
        return when (direction) {
            Direction.EAST -> x x2y y + 1
            Direction.SOUTH -> x + 1 x2y y
            Direction.WEST -> x x2y y - 1
            Direction.NORTH -> x - 1 x2y y
        }
    }

    fun partOne(lines: String): Long {
        val (mapString, instructions) = lines.split("\n\n")
        val maxWidth = mapString.lines().maxOf { it.length }
        val map = mapString.lines().map {
            if (it.length == maxWidth) it
            else it.padEnd(maxWidth)
        }
        val startIndex = map.first().withIndex().first { it.value == '.' }.index
        var position = 0 x2y startIndex
        var facing: Direction = Direction.EAST


        fun move(amount: Int): Point2D {
            var next: Point2D = position
            repeat(amount) {
                do {
                    next = next.moveToward(facing)
                    next.x = Math.floorMod(next.x, map.size)
                    next.y = Math.floorMod(next.y, map.first().length)
                } while (map[next.x][next.y] == ' ')
                if (map[next.x][next.y] == '#') return position
                position = next
            }
            return position
        }

        var idxInstruction = 0
        fun nextInstruction() {
            if (instructions[idxInstruction] == 'R') {
                facing = Direction.values()[(facing.ordinal + 1) % 4]
                idxInstruction++
            } else if (instructions[idxInstruction] == 'L') {
                facing = Direction.values()[Math.floorMod(facing.ordinal - 1, 4)]
                idxInstruction++
            } else {
                var idxOfNextDir = idxInstruction
                while (idxOfNextDir <= instructions.lastIndex && instructions[idxOfNextDir].isDigit()) idxOfNextDir++
                val amount = instructions.substring(idxInstruction, idxOfNextDir).toInt()
                move(amount)
                idxInstruction = idxOfNextDir
            }
        }

        while (idxInstruction < instructions.lastIndex) {
            nextInstruction()
        }
        return 1000L * (position.x + 1) + 4L * (position.y + 1) + facing.ordinal

    }

    private fun getCubeFace(point2D: Point2D, map: List<String>): Int? {
        return TODO()
    }

    private fun Point2D.moveTowardsInCube(direction: Direction, map: List<String>) {
        val currentCubeFace = getCubeFace(this, map) ?: throw IllegalArgumentException()
        val nextStep = this.moveToward(direction)
        if (getCubeFace(nextStep, map) == currentCubeFace) nextStep to direction


    }

    fun partTwo(lines: String): Int = TODO()
}

fun main() {
    println(Day22.partOne(readFileText(22)))
    //println(Day22.y2021.partTwo(y2021.readFileText(22)))
}