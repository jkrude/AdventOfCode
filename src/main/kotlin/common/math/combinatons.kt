package common.math

import kotlin.math.ceil
import org.apache.commons.numbers.combinatorics.Combinations


fun <T> subsetsOfSize(subsetSize: Int, iterable: List<T>): Sequence<List<T>> {
    return Combinations.of(iterable.size, subsetSize)
        .asSequence()
        .map { it.map { idx -> iterable[idx] } }
}

infix fun <T, R> Iterable<T>.product(other: Iterable<R>): List<Pair<T, R>> =
    this.flatMap { other.map { o -> it to o } }


class PowerSetIterator<T>(
    private val underlyingList: List<T>
) : Iterator<List<T>> {

    private var binaryCounter: Int = 0
    private val lastIndex = 1.shl(underlyingList.size) - 1 // 2^n -1
    override fun hasNext(): Boolean = binaryCounter <= lastIndex

    override fun next(): List<T> {
        check(hasNext(), { "Next was called without an available next element." })
        // A 1 indicated take the element of the underlying list
        val inBinary = binaryCounter.toString(2).padStart(underlyingList.size, '0')

        binaryCounter++

        // Keep all elements that have a 1 in the string
        return underlyingList.filterIndexed { index, _ -> inBinary[index] == '1' }
    }
}


/**
 * `InplaceCounter` is an implementation of an `Iterator` that generates arrays
 * of integers, incrementing their values in a counter-like manner for
 * a given base. It produces all possible combinations of digits up to a specified
 * limit or number of digits in the given base.
 * For example in base 2 counting to 3 the counter would produce:
 * [0,0,0]; [0,0,1];, [0,1,0]; [0,1,1];
 *
 * The iterator uses a simple in-place counter logic to calculate the next combination
 * without creating additional objects. This makes the class efficient in scenarios where
 * memory usage needs to be minimized.
 *
 * @constructor Initializes the counter with a maximum value or a specified number of digits and base.
 * @param max The maximum value for combinations (used in conjunction with base).
 * @param base The base for the counter (e.g., base 10 for decimal representation).
 * @param digits The number of digits for the counter (alternative constructor parameter instead of max).
 */
class InplaceCounter : Iterator<IntArray> {
    val base: Int
    private val counter: IntArray
    private var firstInvocation = true

    constructor(max: Long, base: Int) {
        this.base = base
        this.counter = IntArray(ceil(max / base.toFloat()).toInt())
    }

    constructor(digits: Int, base: Int) {
        this.base = base
        this.counter = IntArray(digits)
    }

    override fun hasNext(): Boolean =
        !counter.all { it == base - 1 }


    override fun next(): IntArray {
        if (!hasNext()) throw NoSuchElementException()
        if (!firstInvocation) this.computeNext()
        else firstInvocation = false
        return counter
    }

    fun computeNext() {
        var idx = counter.lastIndex
        while (idx >= 0) {
            counter[idx] += 1
            if (counter[idx] == base) {
                counter[idx] = 0
                idx--
            } else return
        }
        throw IllegalStateException()
    }
}