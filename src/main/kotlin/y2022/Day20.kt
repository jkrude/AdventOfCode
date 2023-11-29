package y2022

import common.readFileLines

object Day20 {
    fun moveLong(idx: Int, workingList: MutableList<Int>, baseList: List<Long>) {
        val moveBy = baseList[workingList[idx]]
        if (moveBy == 0L) return
        val elementAtIdx = workingList.removeAt(idx)
        val movingTo: Int = if (moveBy > 0) {
            ((idx + moveBy) % workingList.size).toInt()
        } else {
            if (idx + moveBy == 0L) workingList.size else Math.floorMod(idx + moveBy, workingList.size)
        }
        workingList.add(movingTo, elementAtIdx)
    }

    private fun mix(baseList: List<Long>, workingList: MutableList<Int>) {
        for (i in workingList.indices) {
            val idx = workingList.indexOf(i)
            moveLong(idx, workingList, baseList)
        }
    }

    private fun extract(baseList: List<Long>, workingList: MutableList<Int>): Long {
        val idxZeroBase = baseList.indexOf(0)
        val idxZeroWorking = workingList.indexOf(idxZeroBase)
        val thousand = (idxZeroWorking + 1000) % baseList.size
        val twoThousand = (idxZeroWorking + 2000) % baseList.size
        val threeThousand = (idxZeroWorking + 3000) % baseList.size
        return baseList[workingList[thousand]] + baseList[workingList[twoThousand]] + baseList[workingList[threeThousand]]
    }

    fun partOne(lines: List<String>): Long {
        val baseList = lines.map { it.toLong() }
        val workingList = baseList.indices.toMutableList()
        mix(baseList, workingList)
        return extract(baseList, workingList)
    }

    fun partTwo(lines: List<String>): Long {
        val decryptionKey = 811589153L
        val baseList: List<Long> = lines.map { it.toLong() * decryptionKey }
        val workingList = baseList.indices.toMutableList()
        repeat(10) {
            mix(baseList, workingList)
        }
        return extract(baseList, workingList)
    }
}

fun main() {
    println(Day20.partOne(readFileLines(2, 20220)))
    println(Day20.partTwo(readFileLines(2, 20220)))
}