package y2024

import common.readFileText

object Day9 {

    /**
     * Represents a block in the file system. Blocks are laid out in a linear order resembling a linked list.
     * @param start is the first address of the block.
     * @param end is the last address of the block.
     * @param content indicates weather this file system section is filled with content.
     * @param previous is a reference to the block before this one (null if this is the first blocks).
     * @param next is a reference to the block (null if this is the last block).
     */
    class Block(
        var start: Int,
        var end: Int,
        var content: Long?,
        var previous: Block? = null,
        var next: Block? = null
    ) {

        constructor(start: Int, size: Int) : this(start, start + size - 1, null, null)

        fun size() = 1 + end - start

        fun nextFree(): Block? = generateSequence(this, Block::next).firstOrNull { it.content == null }

        fun previousWithContent(): Block? = if (this.content != null) this else this.previous?.previousWithContent()

        fun nextElseAppend(size: Int = 10): Block {
            if (this.next == null) {
                this.next = Block(this.end + 1, size, content = null, previous = this)
            }
            return this.next!!
        }

        /**
         * Fill the block with content.
         * Filling can result in four distinct cases:
         * 1. The block is already filled -> fill the next free block instead
         * 2. The block has more space than requested -> Split the current block to fit the content and a new empty block.
         * 3. The content perfectly fits the block -> place the content in the current block.
         * 4. The content is greater than the block -> fill the current block and place the rest in the next free one.
         */
        fun fill(content: Long, size: Int): Block {
            if (this.content != null) return this.next!!.fill(content, size)
            if (size < size()) // split
            {
                this.content = content
                val newEnd = start + size - 1
                val newBlock = Block(newEnd + 1, this.end, null, previous = this, next = this.next)
                this.end = newEnd
                this.next = newBlock
                return newBlock
            } else if (size == size()) {
                this.content = content
                return nextFree() ?: throw IllegalStateException()
            } else {
                val newSize = size - this.size()
                this.content = content
                return next!!.fill(content, newSize)
            }
        }
    }

    private fun loadFileSystem(input: String): Pair<Block, Block> {
        val digits = input.map { it.digitToInt() }.toList()
        // id to value
        val firstBlock =
            Block(
                start = 0, size = digits.first()
            ).apply { this.content = 0L }
        var currentBlock: Block = firstBlock
        digits.withIndex().drop(1)
            .forEach { (index, i) ->
                val next = Block(currentBlock.end + 1, i)
                next.previous = currentBlock
                if (index % 2 == 0) {
                    next.content = (index / 2).toLong()
                }
                currentBlock.next = next
                currentBlock = next
            }
        return firstBlock to currentBlock
    }

    fun partOne(input: String): Long {

        val (firstBlock, currentBlock) = loadFileSystem(input)
        var finished = false

        var freeBlock: Block = firstBlock
        var contentBlock: Block = currentBlock
        fun nextFree() {
            while (freeBlock.content != null && !finished) {
                if (freeBlock.next == null || freeBlock.next === contentBlock) finished = true
                else freeBlock = freeBlock.next!!
            }
        }

        fun nextContent() {
            while (contentBlock.content == null && !finished) {
                if (contentBlock.previous == null || contentBlock.previous === freeBlock) finished = true
                else contentBlock = contentBlock.previous!!
            }
        }
        do {
            nextFree()
            nextContent()
            if (finished) break
            if (contentBlock.size() < freeBlock.size()) // split
            {
                freeBlock.content = contentBlock.content
                contentBlock.content = null
                val newEnd = freeBlock.start + contentBlock.size() - 1
                val newBlock = Block(newEnd + 1, freeBlock.end, null, previous = freeBlock, next = freeBlock.next)
                freeBlock.end = newEnd
                freeBlock.next = newBlock
            } else if (contentBlock.size() == freeBlock.size()) {
                freeBlock.content = contentBlock.content
                contentBlock.content = null
            } else {
                val newSize = contentBlock.size() - freeBlock.size()
                freeBlock.content = contentBlock.content
                val newEnd = contentBlock.start + newSize - 1
                val newFreeBlock = Block(
                    newEnd + 1,
                    end = contentBlock.end,
                    content = null,
                    previous = contentBlock,
                    next = contentBlock.next
                )
                contentBlock.next = newFreeBlock
                contentBlock.end = newEnd
            }

        } while (!finished)
        return generateSequence(firstBlock) { it.next }.sumOf { block ->
            (block.start..block.end).sumOf { it * (block.content ?: 0L) }
        }
    }

    fun partTwo(input: String): Long {
        val (firstBlock, currentBlock) = loadFileSystem(input)
        var finished = false

        var leftMostFreeBlock: Block = firstBlock
        var contentBlock: Block = currentBlock
        fun nextFree() {
            while (leftMostFreeBlock.content != null && !finished) {
                if (leftMostFreeBlock.next == null || leftMostFreeBlock.next === contentBlock) finished = true
                else leftMostFreeBlock = leftMostFreeBlock.next!!
            }
        }

        fun nextContent() {
            while (contentBlock.content == null && !finished) {
                if (contentBlock.previous == null || contentBlock.previous === leftMostFreeBlock) finished = true
                else contentBlock = contentBlock.previous!!
            }
        }
        do {
            nextFree()
            nextContent()
            if (finished) break
            var freeBlock = leftMostFreeBlock
            if (contentBlock.size() > freeBlock.size()) {
                freeBlock =
                    generateSequence(leftMostFreeBlock) { it.next }
                        .firstOrNull { it.content == null && it.size() >= contentBlock.size() }
                        ?: freeBlock
            }
            if (freeBlock.start > contentBlock.start) {
                contentBlock = contentBlock.previous ?: break
                continue
            }
            if (contentBlock.size() < freeBlock.size()) // split
            {
                freeBlock.content = contentBlock.content
                contentBlock.content = null
                val newEnd = freeBlock.start + contentBlock.size() - 1
                val newBlock = Block(
                    newEnd + 1,
                    freeBlock.end,
                    null,
                    previous = freeBlock,
                    next = freeBlock.next
                )
                freeBlock.end = newEnd
                freeBlock.next = newBlock
            } else if (contentBlock.size() == freeBlock.size()) {
                freeBlock.content = contentBlock.content
                contentBlock.content = null
            } else {
                contentBlock = contentBlock.previous ?: break
            }
        } while (!finished)
        return generateSequence(firstBlock) { it.next }.sumOf { block ->
            (block.start..block.end).sumOf { it * (block.content ?: 0L) }
        }
    }
}

fun main() {
    val input = readFileText(9, 2024)
    println(Day9.partOne(input))
    println(Day9.partTwo(input))
}