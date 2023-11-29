package common.math

import org.apache.commons.numbers.combinatorics.Combinations

object Combinations {
    fun <T> subsetsOfSize(subsetSize: Int, iterable: List<T>): Sequence<List<T>> {
        return Combinations.of(iterable.size, subsetSize)
            .asSequence()
            .map { it.map { idx -> iterable[idx] } }
    }


    class PowerSetIterator<T>(
        private val underlyingList: List<T>
    ) : Iterator<List<T>> {

        private var binaryCounter: Int = 0
        private val lastIndex = 1.shl(underlyingList.size) - 1 // 2^n -1
        override fun hasNext(): Boolean = binaryCounter != lastIndex

        override fun next(): List<T> {
            check(hasNext(), { "Next was called without an available next element." })
            // A 1 indicated take the element of the underlying list
            val inBinary = binaryCounter.toString(2).padStart(underlyingList.size, '0')

            binaryCounter++

            // Keep all elements that have a 1 in the string
            return underlyingList.filterIndexed { index, _ -> inBinary[index] == '1' }
        }
    }
}
