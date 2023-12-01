package common.extensions


inline fun Boolean.runIfTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

infix fun Int.autoRange(to: Int) = if (this < to) this..to else this downTo to

fun IntRange.extendedBy(symmetric: Int): IntRange {
    require(symmetric > 0)
    return (start - symmetric)..(endInclusive + symmetric)
}

fun IntRange.extendedBy(lower: Int, upper: Int): IntRange {
    require(lower > 0 && upper > 0)
    return start - lower..endInclusive + upper
}

