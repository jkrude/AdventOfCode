package y2022

import com.microsoft.z3.*
import common.*
import common.extensions.findGroupValues
import common.extensions.productOf
import java.util.*

object Day19 {

    private enum class Resource {
        Ore, Clay, Obsidian, Geode;
    }

    private fun <T> resourceMapOf(defaults: (Resource) -> T) = EnumMap(Resource.entries.associateWith { defaults(it) })
    private fun resourceMapOf(vararg pairs: Pair<Resource, Int>) =
        resourceMapOf { 0 }.apply {
            pairs.forEach { (resource, amount) -> this[resource] = amount }
        }

    private fun <K : Enum<K>, V> EnumMap<K, V>.at(key: K): V = this[key]!!

    private class BluePrint(description: String) {
        // Resource of robot -> how many resources are required
        val requirements: EnumMap<Resource, EnumMap<Resource, Int>>
        val id: Int
        fun requiresFor(robot: Resource): EnumMap<Resource, Int> = requirements.at(robot)

        private val bluePrintRegex =
            """Blueprint (\d+)+: Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

        init {
            val values = bluePrintRegex.findGroupValues(description).map { it.toInt() }
            require(values.size == 7)
            id = values.first()
            requirements = resourceMapOf { resource ->
                when (resource) {
                    Resource.Ore -> resourceMapOf(Resource.Ore to values[1])
                    Resource.Clay -> resourceMapOf(Resource.Ore to values[2])
                    Resource.Obsidian -> resourceMapOf(Resource.Ore to values[3], Resource.Clay to values[4])
                    Resource.Geode -> resourceMapOf(Resource.Ore to values[5], Resource.Obsidian to values[6])
                }
            }
        }
    }

    private class GeodeMaximizer(val bluePrint: BluePrint, val turns: Int) {

        private val optimizer: Optimize
        private val resourcesAtEndOfTurn: List<EnumMap<Resource, IntExpr>>
        private val robotsAtStartOfTurn: List<EnumMap<Resource, IntExpr>>
        private val robotBought: List<EnumMap<Resource, IntExpr>>
        val result: Long

        init {
            with(Kontext()) {
                optimizer = mkOptimize()
                resourcesAtEndOfTurn =
                    List(turns + 1) {
                        resourceMapOf { res ->
                            mkIntConst("Resource $res-$it").also { symb ->
                                optimizer.add(symb ge 0)
                            }
                        }
                    }
                robotsAtStartOfTurn =
                    List(turns + 1) {
                        resourceMapOf { res ->
                            mkIntConst("Robot $res-$it").also { symb ->
                                optimizer.add(symb ge 0)
                            }
                        }
                    }
                robotBought =
                    List(turns + 1) {
                        resourceMapOf { resource ->
                            mkIntConstInRange("robot_${resource}_bought_at_$it", 0..1, optimizer::add)
                        }
                    }
                // We cant buy robots initially.
                robotBought[0].forEach { (_, initiallyBought) -> optimizer.add(initiallyBought eq 0) }
                // We have no resources at the beginning.
                resourcesAtEndOfTurn[0].forEach { (_, initialValue) -> optimizer.add(initialValue eq 0) }
                // We start with one ore robot.
                robotsAtStartOfTurn[0].forEach { (robotType, initialRobots) ->
                    optimizer.add(if (robotType != Resource.Ore) initialRobots eq 0 else initialRobots eq 1)
                }
                result = solve()
            }
        }

        private fun Kontext.solve(): Long {
            for (turn in (1..turns)) {
                resourceMatch(turn)
                robotsMatch(turn)
            }
            val handle = optimizer.maximize(resourcesAtEndOfTurn.last().at(Resource.Geode))
            val result = optimizer.check()
            require(result == Status.SATISFIABLE)
            return handle.value.toLong()
        }

        private fun Kontext.resourceMatch(thisTurn: Int) {
            // Yield = what the robots produce this turn
            val yieldThisTurn = resourceMapOf { robotsAtStartOfTurn[thisTurn].at(it) }

            // The sum of costs of each robot we bought this turn
            val resourcesSpendThisTurn: EnumMap<Resource, Expr<IntSort>> = resourceMapOf { resource ->
                robotBought[thisTurn].map { (robotType, wasRobotBought) ->
                    wasRobotBought * bluePrint.requiresFor(robotType).at(resource)
                }.sum()
            }

            Resource.entries.forEach { resource ->
                optimizer.add(
                    resourcesAtEndOfTurn[thisTurn].at(resource) eq
                            (resourcesAtEndOfTurn[thisTurn - 1].at(resource)
                                    + yieldThisTurn.at(resource)
                                    - resourcesSpendThisTurn.at(resource))
                )
            }
        }

        private fun Kontext.robotsMatch(thisTurn: Int) {
            // This turn robots are last turn robots plus the newly bought one
            val lastTurn = thisTurn - 1
            Resource.entries.forEach { resource ->
                optimizer.add(
                    robotsAtStartOfTurn[thisTurn].at(resource) eq
                            (robotsAtStartOfTurn[lastTurn].at(resource) + robotBought[lastTurn].at(
                                resource
                            ))
                )
            }

            // Decide whether to buy a robot this turn
            fun hasNotEnoughResources(robotType: Resource): BoolExpr =
                bluePrint.requiresFor(robotType).entries
                    .any { (reqResource, requiredAmount) -> resourcesAtEndOfTurn[lastTurn].at(reqResource) lt requiredAmount }

            fun isOtherRobotBought(resource: Resource): BoolExpr =
                robotBought[thisTurn].entries
                    .filter { it.key != resource } // only consider other
                    .any { (_, bought: IntExpr) -> bought eq 1 }

            Resource.entries.forEach { resource ->
                // We cant build robots if we don't have enough resources or already bought another one.
                optimizer.add(
                    (isOtherRobotBought(resource) or hasNotEnoughResources(resource))
                            implies (robotBought[thisTurn].at(resource) eq 0)
                )
            }
        }

    }

    private fun maximizeGeodes(bluePrint: BluePrint, turns: Int) = GeodeMaximizer(bluePrint, turns).result

    // maximize the number of opened geodes after 24 minutes
    fun partOne(lines: List<String>): Long =
        lines.map { BluePrint(it) }.sumOf { it.id.toLong() * maximizeGeodes(it, 24) }

    fun partTwo(lines: List<String>): Long =
        lines.take(3).map { BluePrint(it) }.productOf { maximizeGeodes(it, 32) }
}


fun main() {
    println(Day19.partOne(readFileLines(19, 2022)))
    println(Day19.partTwo(readFileLines(19, 2022)))
}