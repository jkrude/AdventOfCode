package y2023

import common.addEdgeMissingVertex
import common.algorithms.Search
import common.math.lcm
import common.predecessorIn
import common.readFileLines
import common.successorsIn
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge

private typealias Network = Graph<Day20.Module, DefaultEdge>

object Day20 {

    enum class Signal { High, Low }
    data class Pulse(val from: Module, val to: Module, val signal: Signal)

    sealed class Module(val name: String, protected val graph: Network) {
        abstract fun process(pulse: Pulse): List<Pulse>
        fun sendEach(out: Signal) = this.successorsIn(graph).map { Pulse(this, it, out) }
    }

    private class Broadcaster(graph: Network) : Module("broadcaster", graph) {
        override fun process(pulse: Pulse): List<Pulse> = sendEach(pulse.signal)
    }

    private class FlipFlop(name: String, graph: Network) : Module(name, graph) {
        private var signal: Signal = Signal.Low
        override fun process(pulse: Pulse): List<Pulse> {
            if (pulse.signal == Signal.High) return emptyList()
            this.signal = if (this.signal == Signal.High) Signal.Low else Signal.High
            return sendEach(this.signal)
        }
    }

    private class Conjunction(name: String, graph: Network) : Module(name, graph) {
        private lateinit var memory: MutableMap<Module, Signal> // Predecessors are not known on construction.
        override fun process(pulse: Pulse): List<Pulse> {
            if (!this::memory.isInitialized) {
                memory = this.predecessorIn(graph).associateWithTo(HashMap()) { Signal.Low }
            }
            memory[pulse.from] = pulse.signal
            return sendEach(if (memory.values.all { it == Signal.High }) Signal.Low else Signal.High)
        }
    }

    private class End(name: String, graph: Network) : Module(name, graph) {
        override fun process(pulse: Pulse): List<Pulse> = emptyList()
    }

    private fun parseModule(line: String, graph: Network): Pair<Module, List<String>> {
        val (id, outgoing) = line.split(" -> ")
        val module = when (id.first()) {
            '%' -> FlipFlop(id.drop(1), graph)
            '&' -> Conjunction(id.drop(1), graph)
            else -> Broadcaster(graph)
        }
        return module to outgoing.split(", ")
    }

    private fun importGraph(lines: List<String>): Network {
        with(DefaultDirectedGraph<Module, DefaultEdge>(DefaultEdge::class.java)) {
            val modulesByName = lines.map { parseModule(it, this) }.associateBy { it.first.name }
            modulesByName.values.forEach { (module, outgoings) ->
                outgoings.forEach { to: String ->
                    addEdgeMissingVertex(module, modulesByName[to]?.first ?: End(to, this))
                }
            }
            return this
        }
    }

    private fun Network.sendPulse(onEachPulse: (Pulse) -> Unit) {
        val broadcaster = this.vertexSet().first { it is Broadcaster }
        // We process each pulse that was sent in a breadth first manner.
        // The pulse is consumed by its target-module which itself produces outgoing signals.
        Search.startingFrom(Pulse(broadcaster, broadcaster, Signal.Low))
            .onEachVisit(onEachPulse)
            .neighbors { pulse -> pulse.to.process(pulse) }
            .allowRevisit() // It is okay if we process the same Pulse multiple times.
            .executeBfs()
    }

    fun partOne(lines: List<String>): Long {
        val moduleGraph = importGraph(lines)
        var (lows, highs) = 0L to 0L
        // As times is that low we can simply simulate each button push and count the number of signals that were send.
        repeat(1_000) {
            moduleGraph.sendPulse { if (it.signal == Signal.High) highs++ else lows++ }
        }
        return lows * highs
    }

    fun partTwo(lines: List<String>): Long {
        // The online input to "rx" is a single inverter.
        // With a quick inspection using Graph<Vertex, E>.toDOT it can be observed that all inputs
        // to the inverter are on separate cycle. So we can find the first time for each input to be triggered
        // independently and compute the lcm of them.
        val moduleGraph = importGraph(lines)
        val dependencies = moduleGraph.vertexSet().first { it.name == "rx" }
            .predecessorIn(moduleGraph)
            .flatMap { it predecessorIn moduleGraph }
            .associateWithTo(HashMap()) { 0L }
        // Repeat triggering the button until every dependency sent at least one "High" signal.
        for (iteration in generateSequence(1L, Long::inc)) {
            moduleGraph.sendPulse {
                if (it.from in dependencies && it.signal == Signal.High) dependencies[it.from] = iteration
            }
            if (dependencies.all { it.value != 0L }) break
        }
        return dependencies.values.reduce(::lcm) // The first time all dependencies trigger at the same time.
    }
}

fun main() {
    val input = readFileLines(20, 2023)
    println(Day20.partOne(input))
    println(Day20.partTwo(input))
}