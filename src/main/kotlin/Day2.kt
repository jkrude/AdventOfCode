fun readReport(): List<String> = object {}.javaClass.getResource("day2.txt")?.readText()?.split("\n")!!

fun partOne(): Int {
    val lines: List<String> = readReport()
    val lengthOfString = lines.first().length
    val gamma: MutableList<Boolean> = ArrayList(lengthOfString)
    for (idx in 0 until lengthOfString) {
        gamma.add(lines.count { line -> line[idx] == '1' } > lines.size / 2)
    }
    fun List<Boolean>.asInt(negated: Boolean = false) =
        Integer.parseInt(
            this.joinToString(separator = "") { if (it xor negated) "1" else "0" }, 2
        )
    return gamma.asInt() * gamma.asInt(true)
}

// Life support = oxygen * co2 rating
fun partTwo(input: List<String>): Int {
    return filter(byMajority = true, input) * filter(byMajority = false, input)
}

fun filter(byMajority: Boolean, list: List<String>): Int {
    var idx = 0
    var filteredList = list
    while (filteredList.size > 1) {
        val oneCount = filteredList.count { it[idx] == '1' }
        val filterCriteria =
            if (byMajority) {
                if (oneCount >= filteredList.size / 2f) '1' else '0'
            } else {
                if (oneCount < filteredList.size / 2f) '1' else '0'
            }
        filteredList = filteredList.filter { it[idx] == filterCriteria }
        idx++
    }
    return Integer.parseInt(filteredList.first(), 2)
}


fun main() {
    val testData = """
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010""".trimIndent().split("\n")
    val data = readReport()
    println(partTwo(data))
}