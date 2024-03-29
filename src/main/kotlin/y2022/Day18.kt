package y2022

import common.algorithms.Search
import common.readFileLines

private typealias Point3D = Triple<Int, Int, Int>

object Day18 {


    private fun Point3D.adjacentSides(): List<Point3D> {
        val (x, y, z) = this
        return listOf(
            Point3D(x + 1, y, z),
            Point3D(x + -1, y, z),
            Point3D(x, y + 1, z),
            Point3D(x, y - 1, z),
            Point3D(x, y, z + 1),
            Point3D(x, y, z - 1),
        )
    }

    private fun parse(lines: List<String>): List<Point3D> {
        return lines.map {
            val (x, y, z) = it.split(",").map { cubeFace -> cubeFace.toInt() }
            Point3D(x, y, z)
        }
    }

    fun partOne(lines: List<String>): Int {
        val cubes: Set<Point3D> = parse(lines).toSet()

        return cubes.sumOf { cube ->
            cube.adjacentSides().count { neighbouringCube ->
                neighbouringCube !in cubes
            }
        }
    }

    fun partTwo(lines: List<String>): Int {
        val cubes = parse(lines).toSet()
        val (minX, maxX) = cubes.minOf { it.first } to cubes.maxOf { it.first }
        val (minY, maxY) = cubes.minOf { it.second } to cubes.maxOf { it.second }
        val (minZ, maxZ) = cubes.minOf { it.third } to cubes.maxOf { it.third }

        val cache = mutableMapOf<Point3D, Boolean>()
        fun Boolean.andCacheFor(cube: Point3D) = this.also { cache[cube] = this }

        fun isOutSide(cube: Point3D) =
            cache[cube] == true ||
                    (cube.first < minX || cube.first > maxX
                            || cube.second < minY || cube.second > maxY
                            || cube.third < minZ || cube.third > maxZ)


        fun hasPathOutside(cube: Point3D): Boolean {
            if (cube in cache) return cache[cube]!!

            if (cube in cubes) return false.andCacheFor(cube)

            val path = Search.startingFrom(cube).neighbors { it.adjacentSides().filter { n -> n !in cubes } }
                .stopAt(::isOutSide)
                .executeBfs()
            return (path != null).andCacheFor(cube)
        }
        return cubes.sumOf { cube ->
            cube.adjacentSides().count { neighbouringCube ->
                hasPathOutside(neighbouringCube)
            }
        }
    }
}

fun main() {
    println(Day18.partOne(readFileLines(1, 20228)))
    println(Day18.partTwo(readFileLines(1, 20228)))
}