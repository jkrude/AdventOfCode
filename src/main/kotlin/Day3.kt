import java.io.FileNotFoundException
import kotlin.math.pow

class Day3 {
    fun partOne(lines: List<String>): Int {
        lines.first().indices
            .map { idx -> lines.groupBy { it[idx] }.maxByOrNull { (_, v) -> v.size }?.key ?: '1' }
            .joinToString(separator = "")
            .toInt(radix = 2)
            .let { return it * ((2f.pow(lines.first().length) - 1).toInt() - it) }
    }

    // Life support = oxygen * co2 rating
    fun partTwo(input: List<String>): Int {

        fun filter(data: List<String>, byMaj: Boolean): Int {
            var filtered = data
            for (i in data.first().indices) {
                val (ones, zeros) = filtered.partition { it[i] == '1' }
                filtered = when {
                    ones.size == zeros.size -> if (byMaj) ones else zeros
                    (ones.size < zeros.size) xor byMaj -> ones // conditional inversion
                    else -> zeros
                }
                if (filtered.size == 1) break
            }
            return filtered.first().toInt(radix = 2)
        }

        return filter(input, true) * filter(input, false)
    }
}

fun main() {

    println(Day3().partTwo(readReport(3)))
}