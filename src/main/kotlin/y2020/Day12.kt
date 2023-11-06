package y2020

import common.Point2D
import common.readFileLines
import common.x2y

object Day12 {

    // x position is west (negative) or east (positive)
    // y position is north (negative) or south (positive)
    // turning means x becomes y and y gets inverted and becomes x
    fun turnClockwise(direction: Point2D) {
        val x = direction.x
        direction.x = direction.y * -1
        direction.y = x
    }

    private fun turn(direction: Point2D, right: Boolean, amount: Int) {
        val ninetyTurns: Int = if (right) amount / 90 else (360 - amount) / 90
        repeat(ninetyTurns) {
            turnClockwise(direction)
        }
    }

    /**
     * Action N means to move north by the given value.
     * Action S means to move south by the given value.
     * Action E means to move east by the given value.
     * Action W means to move west by the given value.
     * Action L means to turn left the given number of degrees.
     * Action R means to turn right the given number of degrees.
     * Action F means to move forward by the given value in the direction the ship is currently facing.
     */
    private fun changeDirection(action: Char, amount: Int) =
        when (action) {
            'N' -> (0 x2y -1) * amount
            'S' -> (0 x2y 1) * amount
            'E' -> (1 x2y 0) * amount
            'W' -> (-1 x2y 0) * amount
            else -> throw IllegalArgumentException(action.toString())
        }

    private fun applyInstruction(instruction: String, direction: Point2D, position: Point2D) {
        val action = instruction.first()
        val amount = instruction.drop(1).toInt()
        when (action) {
            'N', 'S', 'E', 'W' -> position += changeDirection(action, amount)
            'L', 'R' -> turn(direction, action == 'R', amount)
            'F' -> position += (direction * amount)
            else -> throw IllegalArgumentException()
        }
    }

    fun partOne(lines: List<String>): Int {
        val direction: Point2D = 1 x2y 0
        val position: Point2D = 0 x2y 0
        lines.forEach { applyInstruction(it, direction, position) }
        return (0 x2y 0).manhattanDistanceTo(position)
    }

    private fun applyWaypointInstruction(instruction: String, waypoint: Point2D, position: Point2D) {
        val action = instruction.first()
        val amount = instruction.drop(1).toInt()
        when (action) {
            'N', 'S', 'E', 'W' -> waypoint += changeDirection(action, amount)
            'L', 'R' -> turn(waypoint, action == 'R', amount)
            'F' -> position += waypoint * amount
            else -> throw IllegalArgumentException()
        }
    }

    fun partTwo(lines: List<String>): Int {
        val waypoint = 10 x2y -1
        val position = 0 x2y 0
        lines.forEach { applyWaypointInstruction(it, waypoint, position) }
        return position.manhattanDistanceTo(0 x2y 0)
    }
}

fun main() {
    println(Day12.partOne(readFileLines(12, 2020)))
    println(Day12.partTwo(readFileLines(12, 2020)))
}