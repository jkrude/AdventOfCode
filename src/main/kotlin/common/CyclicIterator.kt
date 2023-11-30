package common

class CyclicIterator<T>(iterable: Iterable<T>) : Iterator<T> {

    private val underlyingData = iterable.toList()
    private var _currentIdx = 0
    val currentIdx get() = _currentIdx

    override fun hasNext(): Boolean = underlyingData.isNotEmpty()

    override fun next(): T {
        return underlyingData[_currentIdx].also {
            _currentIdx = if (_currentIdx == underlyingData.lastIndex) 0 else _currentIdx + 1
        }
    }
}

fun <T> cyclicIteratorOf(iterable: Iterable<T>): CyclicIterator<T> {
    return CyclicIterator(iterable)
}