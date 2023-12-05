package common.extensions

import arrow.core.Either

infix fun Int.autoRange(to: Int) = if (this < to) this..to else this downTo to
fun IntRange.extendedBy(symmetric: Int): IntRange {
    require(symmetric > 0)
    return (start - symmetric)..(endInclusive + symmetric)
}

fun IntRange.extendedBy(lower: Int, upper: Int): IntRange {
    require(lower > 0 && upper > 0)
    return start - lower..endInclusive + upper
}

/**
 * @return a range containing all elements that are in this and the other range.
 */
infix fun LongRange.intersect(other: LongRange): LongRange {
    return when {
        first in other && last in other -> this
        first in other && last !in other -> first..other.last
        first !in other && last in other -> other.first..last
        first !in other && last !in other -> {
            if (other.first !in this && other.last !in this) LongRange.EMPTY
            else other
        }

        else -> error("Could not find intersection of $this and $other")
    }
}

/** Create a range with all elements of other removed.
 * If other is fully contained in this return two ranges: left and right of other
 * @return this without elements of other
 */
infix fun LongRange.subtract(other: LongRange): Either<LongRange, Pair<LongRange, LongRange>> {
    return when {

        first !in other && last !in other -> {
            if (other.first in this && other.last in this) Either.Right(first until other.first to other.last + 1..this.last)
            else Either.Left(this)
        }

        first !in other && last in other -> Either.Left(first until other.first)
        first in other && last !in other -> Either.Left(other.last + 1..last)
        first in other && last in other -> Either.Left(LongRange.EMPTY)
        else -> error("Could not find subtraction of $this and $other")
    }
}

/**
 * The same as intersect for long.
 */
infix fun IntRange.intersect(other: IntRange): IntRange {
    return when {
        first in other && last in other -> this
        first in other && last !in other -> first..other.last
        first !in other && last in other -> other.first..last
        first !in other && last !in other -> {
            if (other.first !in this && other.last !in this) IntRange.EMPTY
            else other
        }

        else -> error("Could not find intersection of $this and $other")
    }
}

/**
 * The same as subtract for long.
 */
infix fun IntRange.subtract(other: IntRange): Either<IntRange, Pair<IntRange, IntRange>> {
    return when {

        first !in other && last !in other -> {
            if (other.first in this && other.last in this) Either.Right(first until other.first to other.last + 1..this.last)
            else Either.Left(this)
        }

        first !in other && last in other -> Either.Left(first until other.first)
        first in other && last !in other -> Either.Left(other.last + 1..last)
        first in other && last in other -> Either.Left(IntRange.EMPTY)
        else -> error("Could not find subtraction of $this and $other")
    }
}
