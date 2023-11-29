package y2022

import common.readFileLines
import kotlin.math.max
import kotlin.math.pow

object Day25 {

    @JvmInline
    value class Snafu(val value: String) {
        init {
            require(value.all { it == '2' || it == '1' || it == '0' || it == '-' || it == '=' })
        }

        companion object {
            private fun atomicDecode(snafuChar: Char): Int {
                return when (snafuChar) {
                    '2' -> 2
                    '1' -> 1
                    '0' -> 0
                    '-' -> -1
                    '=' -> -2
                    else -> throw IllegalArgumentException(snafuChar.toString())
                }
            }

            /**
             * Add to Snafus of length 1
             * @return the result (first) and a possible carry (second)
             * -4 = '-1'?
             * 1=
             * 1=
             * ----
             * 11
             *
             * 1=0
             * 1=0
             * ----
             * 110
             *
             * -3= '-2'
             *  2=
             *  2-
             * ----
             * 1=2
             */
            private fun atomicAdd(left: Char, right: Char): Pair<Char, Char> {
                return when (atomicDecode(left) + atomicDecode(right)) {
                    -4 -> '1' to '-'
                    -3 -> '2' to '-'
                    -2 -> '=' to '0'
                    -1 -> '-' to '0'
                    0 -> '0' to '0'
                    1 -> '1' to '0'
                    2 -> '2' to '0'
                    3 -> '=' to '1'
                    4 -> '-' to '1'
                    else -> throw IllegalArgumentException("$left+$right")
                }
            }
        }

        fun toDecimal(): Long =
            value.withIndex().sumOf {
                (5.0).pow(value.length - it.index) * atomicDecode(it.value)
            }.toLong()


        operator fun plus(other: Snafu): Snafu {
            val maxLength = max(this.value.length, other.value.length)
            val a = this.value.padStart(maxLength, '0')
            val b = other.value.padStart(maxLength, '0')
            var carry: Char = '0'
            val result = StringBuilder()
            for (i in (maxLength - 1) downTo 0) {
                var (atomicResult, nextCarry) = atomicAdd(a[i], b[i])
                if (carry != '0') { // previous result might have a carry
                    val (carryResult, intermediateCarry) = atomicAdd(atomicResult, carry)
                    nextCarry = atomicAdd(nextCarry, intermediateCarry).first
                    /**Adding the carry might introduce an intermediate carry which cancels out the nextCarry
                     *e.g.
                     * 2=
                     * 1=
                     *----
                     * -1 -> carry of '-'
                     * 20
                     * 10 -> 2 + 1 has nextCarry of '1' and atomicResult of '='
                     *    -> atomicResult + carry =  '=' + '-' = '2 -> intermediateCarry = '-'
                     *    -> nextCarry + intermediateCarry = '0' -> Cancel each other out
                     *----
                     * 21
                     */
                    require(atomicAdd(nextCarry, intermediateCarry).second == '0')
                    atomicResult = carryResult
                }
                result.append(atomicResult)
                carry = nextCarry
            }
            // Our variable might y2021.get longer
            if (carry != '0') {
                require(carry == '1' || carry == '2') { "Result is negative: ${this.value} + ${other.value}" }
                result.append(carry)
            }
            return Snafu(result.toString().reversed())
        }
    }

    val String.snafu: Snafu
        get() = Snafu(this)

    fun partOne(lines: List<String>): String {
        return lines.map {
            Snafu(it)
        }.reduce { a: Snafu, b: Snafu ->
            a + b
        }.value
    }
}

fun main() {
    println(Day25.partOne(readFileLines(2, 20225)))
}