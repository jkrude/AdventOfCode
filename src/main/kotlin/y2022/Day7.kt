package y2022

import common.readFileLines

class Day7 {

    class File(
        val name: String,
        var size: Int = 0,
        val children: MutableList<File> = mutableListOf()
    ) {
        fun computeSize(): Int {
            if (size == 0) size = children.sumOf { it.computeSize() }
            return size
        }

        fun flattenChildren(): List<File> {
            return if (children.isEmpty()) listOf(this)
            else listOf(this) + children.flatMap { it.flattenChildren() }
        }

        fun isDir() = children.isNotEmpty()

        override fun toString() = name
    }

    companion object {
        /**
         * begin with $ are commands you executed
         * cd means change directory
         * total size of a directory is the sum of the sizes of the files it contains
         *
         * find all of the directories with a total size of at most 100000
         * sum of their total sizes
         */

        fun buildDir(lines: List<String>): File {
            val visitStack = mutableListOf(File("/"))
            val toBeParsed = lines.drop(1)
            val filePattern = """(\d+) (\w+.?\w*)""".toRegex()

            fun parseCommand(line: String) {
                require(line.startsWith("$ cd "))
                if (line.startsWith("$ cd ..")) {
                    visitStack.removeLast()
                } else {
                    val name = line.substringAfter("$ cd ")
                    val nextDir = visitStack.last().children.first { it.name == name }
                    visitStack.add(nextDir)
                }
            }

            fun addToCurrentDir(dirContent: List<String>) {
                // Assumes content of dir is never listed twice
                for (line in dirContent) {
                    if (line.startsWith("$")) return
                    val file = if (line.startsWith("dir")) {
                        File(line.substringAfter("dir "))
                    } else {
                        val (size, name) = filePattern.find(line)!!.groupValues.drop(1)
                        File(name, size.toInt())
                    }
                    visitStack.last().children.add(file)
                }
            }

            // Parse every line one by one
            for ((idx, line) in toBeParsed.withIndex()) {
                if (line.startsWith("$ cd")) parseCommand(line)
                if (line.startsWith("$ ls")) {
                    addToCurrentDir(toBeParsed.subList(idx + 1, toBeParsed.size))
                }
                // skipp other lines (handled by addToCurrentDir
            }

            val root = visitStack[0]
            root.computeSize()

            return root
        }

        fun partOne(root: File) = root.flattenChildren()
            .filter { it.isDir() }
            .filter { it.size <= 100_000 }
            .sortedBy { it.size }
            .sumOf { it.size }

        /**
         * total disk space available to the filesystem is 70_000_000
         * need unused space of at least 30_000_000
         * Find node with
         *  isDir()
         *  size >= requiredSpace
         * choose the one with smallest size
         */
        fun partTwo(root: File, updateSpace: Int = 30_000_000, totalSpace: Int = 70_000_000): Int {
            val requiredExtraSpace = updateSpace - (totalSpace - root.size)
            return root.flattenChildren()
                .filter { it.isDir() }
                .filter { it.size >= requiredExtraSpace }
                .minByOrNull { it.size }
                ?.size ?: throw IllegalArgumentException()
        }

    }
}

fun main() {
    val tree: Day7.File = Day7.buildDir(readFileLines(7))
    println(Day7.partOne(tree))
    val testData = """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent().lines()
    val testTree = Day7.buildDir(testData)
    println(Day7.partTwo(tree))
}