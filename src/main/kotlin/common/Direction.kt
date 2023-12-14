package common

enum class Direction { // Do not change order
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun isVertical() = this == NORTH || this == SOUTH
    fun isHorizontal() = !isVertical()

    fun turnClockwise(): Direction = Direction.entries[(this.ordinal + 1) % 4]

    companion object {

        // Assuming x is increasing to the right and y is increasing to the south
        fun Point2D.moveToward(direction: Direction): Point2D {
            return when (direction) {
                NORTH -> x x2y y - 1
                EAST -> x + 1 x2y y
                SOUTH -> x x2y y + 1
                WEST -> x - 1 x2y y
            }
        }
    }
}


