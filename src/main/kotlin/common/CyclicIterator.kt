package common

class CyclicIterator<T>(iterable: Iterable<T>) : Iterator<T> {

    private val underlyingData = iterable.toList()
    private var currentIdx = 0

    override fun hasNext(): Boolean = underlyingData.isNotEmpty()

    override fun next(): T {
        return underlyingData[currentIdx].also {
            currentIdx = if (currentIdx == underlyingData.lastIndex) 0 else currentIdx + 1
        }
    }
}

fun <T> cyclicIteratorOf(iterable: Iterable<T>): CyclicIterator<T> {
    return CyclicIterator(iterable)
}