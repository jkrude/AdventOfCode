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

fun Regex.findGroupValues(string: String, errorMessage: () -> String = { "Could not find $this in $string" }) =
    this.find(string)?.groupValues?.drop(1) ?: throw IllegalArgumentException(errorMessage())

fun Regex.findOverlapping(text: String, startIndex: Int = 0): MutableList<MatchResult> {
    val allMatches = mutableListOf<MatchResult>()
    var i = startIndex
    do {
        val match = find(text, i) ?: break
        i = match.range.first + 1
        allMatches.add(match);
    } while (true);
    return allMatches

}