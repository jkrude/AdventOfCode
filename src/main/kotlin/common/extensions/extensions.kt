package common.extensions


inline fun Boolean.runIfTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
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

fun String.substringBetween(leftDelimiter: String, rightDelimiter: String): String =
    this.substringAfter(leftDelimiter).substringBefore(rightDelimiter)