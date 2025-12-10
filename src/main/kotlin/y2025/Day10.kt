package y2025

import com.microsoft.z3.*
import common.*
import common.extensions.substringBetween

object Day10 {

    data class Machine(val pattern: String, val buttons: List<List<Int>>, val joltageReq: List<Int>)

    val switchesR = Regex("""\(([\d,]+)\)""")

    /**
     * Parse all the machines.
     * A machine is structured something like [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
     */
    fun parseMachines(lines: List<String>): List<Machine> {
        return lines.map { line ->
            val pattern = line.substringBetween("[", "]")
            val switches: List<List<Int>> =
                switchesR.findAll(line)
                    .map { it.groupValues[1].split(",").map(String::toInt) }
                    .toList()
            val joltageReg = line.substringBetween("{", "}").split(",").map(String::toInt)
            Machine(pattern, switches, joltageReg)
        }
    }

    fun Optimize.minimizeAndResult(objective: Expr<IntSort>): Long {
        val handle = minimize(objective)
        require(check() == Status.SATISFIABLE)
        return handle.value.toLong()
    }

    /**
     * Creates a mapping from each light to the list of buttons that affect it.
     * For example if button with index 0 effects (1,3),
     * then we add 0 to light 1 and light 3.
     * But instead of the buttonIndex we store the variable for the button-presses.
     */
    fun <T> Kontext.mapButtonsToLights(
        lightCount: Int,
        buttons: List<List<Int>>,
        buttonPresses: List<T>
    ): List<List<T>> =
        List(lightCount) { lightIndex ->
            buttons.withIndex()
                .mapNotNull { (buttonIndex, effectedLights) -> if (lightIndex in effectedLights) buttonIndex else null }
                .map { buttonIndex -> buttonPresses[buttonIndex] }
        }

    fun partOne(lines: List<String>): Long =
        parseMachines(lines).sumOf { (pattern, buttons, _) ->
            with(Kontext()) {
                val optimizer = mkOptimize()
                val lightShouldBeOn: List<Boolean> = pattern.map { it == '#' }
                // A button is either presses or not (it doesn't make sense to press a button twice)
                val buttonIsPressed = List(buttons.size) { mkBoolConst("b$it") }
                val buttonPerLight = mapButtonsToLights(pattern.length, buttons, buttonIsPressed)
                buttonPerLight.zip(lightShouldBeOn).forEach { (buttonPressForLight, expected) ->
                    // Whether the light is activates is equal to the buttonPresses % 2 == 0
                    // Using booleans we can use xor to compute this property.
                    val isLightOn = buttonPressForLight.reduce { acc, other -> mkXor(acc, other) }
                    optimizer.add(isLightOn eq mkBool(expected))
                }
                // Convert our list of bools to integer -> minimize the total number of true bits
                val buttonPressesInt: List<Expr<IntSort>> = buttonIsPressed.map { mkITE(it, mkInt(1), mkInt(0)) }
                return@sumOf optimizer.minimizeAndResult(buttonPressesInt.sum())
            }
        }

    fun partTwo(lines: List<String>): Long =
        parseMachines(lines).sumOf { (_, buttons, joltageReq) ->
            with(Kontext()) {
                val optimizer = mkOptimize()
                // Variables for how often a button is pressed.
                val buttonPresses = List(buttons.size) { mkIntConst("b$it") }
                val expectedJoltage: List<IntNum> = joltageReq.map { mkInt(it) }
                val buttonPerLight = mapButtonsToLights(joltageReq.size, buttons, buttonPresses)
                // For each light: how often the lights gets activates matches the expected joltage
                buttonPerLight.zip(expectedJoltage).map { (buttonPressForLight, expected) ->
                    // Sum up the button presses that effect this light
                    val constraint = buttonPressForLight.sum() eq expected
                    optimizer.add(constraint)
                }
                // We can't press a button negative times
                buttonPresses.forEach { optimizer.add(it ge 0) }
                val totalButtonPresses = buttonPresses.sum()
                // Minimize total button presses
                return@sumOf optimizer.minimizeAndResult(totalButtonPresses)
            }
        }
}

fun main() {
    val input = readFileLines(10, 2025)
    println(Day10.partOne(input))
    println(Day10.partTwo(input))
}