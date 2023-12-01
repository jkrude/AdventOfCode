package y2022

import common.extensions.map
import common.readFileText

class Day13 {
    companion object {

        private fun iterate(list: String): Iterator<String> {
            return object : Iterator<String> {
                var current = 0
                override fun hasNext(): Boolean = current < list.length

                override fun next(): String {
                    require(hasNext())
                    var nestLevel = 0
                    for (idx in current..list.lastIndex) {
                        val char = list[idx]
                        if (char == '[') nestLevel++
                        if (char == ']') nestLevel--
                        if (char == ',' && nestLevel == 0) return list.substring(current, idx)
                            .also { current = idx + 1 }
                    }
                    return list.substring(current, list.length).also { current = list.length }
                }

            }
        }


        private fun atomicCompare(atom1: Int, atom2: Int): ComplexResult {
            if (atom1 < atom2) return finallyRight() // packets in right order no more checking required
            if (atom1 == atom2) return complexResult(rightOrder = true, finalCompare = false) // continue checking
            return wrongOrder()
        }

        private fun figureOutElems(elem1: String, elem2: String): ComplexResult {
            if (elem1.toIntOrNull() != null) {
                if (elem2.toIntOrNull() != null) return atomicCompare(elem1.toInt(), elem2.toInt())
                return listCompare("[$elem1]", elem2)
            }
            if (elem2.toIntOrNull() != null) {
                return listCompare(elem1, "[$elem2]")
            }
            return listCompare(elem1, elem2)
        }

        fun individualElements(line: String): List<String> {
            val tmp = if (line.first() == '[' && line.last() == ']') line.substring(1, line.lastIndex)
            else line
            return iterate(tmp).asSequence().toList()
        }

        private fun listCompare(elem1: String, elem2: String): ComplexResult {
            val asPair = (elem1 to elem2)
            require(
                elem1.first() == '[' && elem1.last() == ']'
                        && elem2.first() == '[' && elem2.last() == ']'
            )
            val listComponents = (asPair).map { it.substring(1, it.lastIndex) }
            if (listComponents.first.isEmpty()) {
                return if (listComponents.second.isEmpty()) complexResult(rightOrder = true, finalCompare = false)
                else finallyRight()
            } // Left list exhausted but right not
            val iterPair = listComponents.map { iterate(it) }
            while (iterPair.first.hasNext()) {
                if (!iterPair.second.hasNext()) return wrongOrder()
                val subElemPair = iterPair.map { it.next() } // Get next element for both iterator
                val result = figureOutElems(subElemPair.first, subElemPair.second)
                if (result.second) return result
                if (!result.first) return result
            }
            return if (iterPair.second.hasNext()) finallyRight() // left exhausted right has more
            else complexResult(true, finalCompare = false) // both exhausted
        }

        private fun compare(elem1: String, elem2: String): Boolean {
            /**
             * Compare element wise
             * if left is Int and right is Int → return left <= right
             * otherwise
             *  if left is Int or right is Int convert to list
             *  return compareLists(left, right)
             */
            return listCompare(elem1, elem2).first
        }

        fun inRightOrder(lines: String): List<IndexedValue<List<String>>> {
            val packages = lines.split("\n\n").map {
                it.lines()
            }
            return packages.withIndex().filter { (_, list) ->
                compare(list.first(), list.last())
            }
        }

        fun partOne(lines: String): Int {
            return inRightOrder(lines)
                .sumOf { (idx, _) -> idx + 1 } // packet indices start with 1
        }

        fun sorted(allPackets: List<String>): List<String> {
            return allPackets.sortedWith(comparator = { a, b ->
                if (compare(a, b)) {
                    if (compare(b, a)) 0
                    else -1
                } else 1
            })
        }


        fun partTwo(packetText: String): Int {
            val (distress1, distress2) = "[[2]]" to "[[6]]"
            val allPackets = packetText.lines().filter { it.isNotBlank() } + listOf(distress1, distress2)
            val sorted = sorted(allPackets)
            return (sorted.indexOf(distress1) + 1) * (sorted.indexOf(distress2) + 1)
        }
    }
}

fun main() {
    println(Day13.partOne(readFileText(1, 20223)))
    println(Day13.partTwo(readFileText(1, 20223)))
}