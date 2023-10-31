package y2022

import common.readFileLines
import java.util.*

object Day19 {

    private enum class Resource {
        Ore, Clay, Obsidian, Geode;

        companion object {
            fun <T> asEnumMap(default: T) = EnumMap(Resource.values().associateWith { default })
        }
    }

    private fun <K : Enum<K>, V> EnumMap<K, V>.at(key: K): V = this[key]!!

    private class BluePrint(
        description: String
    ) {
        // Resource of robot -> how many resources are required
        val requirements: EnumMap<Resource, Map<Resource, Int>>
        val id: Int

        private val bluePrintRegex = """Blueprint (\d+)+:
        | Each ore robot costs (\d+) ore.
        | Each clay robot costs (\d+) ore.
        | Each obsidian robot costs (\d+) ore and (\d+) clay.
        | Each geode robot costs (\d+) ore and (\d+) obsidian.""".trimMargin().toRegex()

        init {
            val groupValues = bluePrintRegex.find(description)?.groupValues?.drop(1)?.map { it.toInt() }
                ?: throw IllegalArgumentException(description)
            require(groupValues.size == 7) // [0] = all groups
            id = groupValues[0]
            requirements = EnumMap(
                mapOf(
                    Resource.Ore to mapOf(Resource.Ore to groupValues[1]),
                    Resource.Clay to mapOf(Resource.Ore to groupValues[2]),
                    Resource.Obsidian to mapOf(Resource.Ore to groupValues[3], Resource.Clay to groupValues[4]),
                    Resource.Geode to mapOf(Resource.Ore to groupValues[5], Resource.Obsidian to groupValues[6])
                )
            )
        }

        val reqOreForOreRobot = requirements[Resource.Ore]!![Resource.Ore]!!
        val reqOreForClayRobot = requirements[Resource.Clay]!![Resource.Ore]!!
        val reqOreForObsidianRobot = requirements[Resource.Obsidian]!![Resource.Ore]!!
        val reqClayForObsidianRobot = requirements[Resource.Obsidian]!![Resource.Clay]!!
        val reqOreForGeodeRobot = requirements[Resource.Geode]!![Resource.Ore]!!
        val reqObsidianForGeodeRobot = requirements[Resource.Geode]!![Resource.Obsidian]!!
    }

    private class Factory(
        val bluePrint: BluePrint
    ) {
        val resources: EnumMap<Resource, Int> = Resource.asEnumMap(0)

        val robots: EnumMap<Resource, Int> = Resource.asEnumMap(0).apply {
            this[Resource.Ore] = 1
        }

        fun next(buildStrategy: (Factory) -> Map<Resource, Int>) {
            for ((resourceType, robots) in robots) {
                resources[resourceType] = resources.getValue(resourceType) + robots
            }
            val robotsToBuild = buildStrategy(this)
            for ((resource, numberOfNewRobots) in robotsToBuild) {
                require(bluePrint.requirements[resource]!!
                    .all { (reqResource: Resource, amount) -> resources.getValue(reqResource) >= (amount * numberOfNewRobots) }) {
                    "Not enough resources to execute building plan"
                }
                bluePrint.requirements[resource]!!.forEach { (reqResource, amount) ->
                    resources[reqResource] =
                        resources.getValue(reqResource) - (amount * numberOfNewRobots) // remove resources
                    robots[resource] = robots.getValue(resource) + numberOfNewRobots // build the robots
                }
            }
        }
    }

    // maximize the number of opened geodes after 24 minutes
    fun partOne(lines: List<String>): Int = TODO()

    fun partTwo(lines: List<String>): Int = TODO()
}

fun main() {
    println(Day19.partOne(readFileLines(19)))
    println(Day19.partTwo(readFileLines(19)))
}