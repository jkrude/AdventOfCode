package common

enum class Direction { // Do not change order
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun isVertical() = this == NORTH || this == SOUTH
    fun isHorizontal() = !isVertical()

    fun opposite() = Direction.entries[(this.ordinal + 2) % 4]

    fun turnClockwise(): Direction = Direction.entries[(this.ordinal + 1) % 4]
    fun turnCounterClockwise(): Direction = Direction.entries[(this.ordinal - 1).mod(4)]

    fun visualize(): Char {
        return when (this) {
            WEST -> '<'
            EAST -> '>'
            NORTH -> '^'
            SOUTH -> 'v'
        }
    }

    companion object {

        // Assuming x is increasing to the right and y is increasing to the south
        fun Point2D.moveToward(direction: Direction, amount: Int = 1): Point2D =
            when (direction) {
                NORTH -> x x2y y - amount
                EAST -> x + amount x2y y
                SOUTH -> x x2y y + amount
                WEST -> x - amount x2y y
            }

        fun ofArrow(char: Char) =
            when (char) {
                '<' -> WEST
                '>' -> EAST
                '^' -> NORTH
                'v' -> SOUTH
                else -> throw IllegalArgumentException("Not an arrow: $char")
            }
    }
}


