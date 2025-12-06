package common

import org.jgrapht.Graph
import org.jgrapht.Graphs
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph
import org.jgrapht.graph.DirectedPseudograph
import org.jgrapht.nio.IntegerIdProvider
import org.jgrapht.nio.dot.DOTExporter
import org.jgrapht.traverse.BreadthFirstIterator
import java.io.ByteArrayOutputStream
import java.util.function.Function

class LabeledEdge(val label: String) : DefaultEdge() {
    override fun toString(): String {
        return "($source -> $target : $label)"
    }
}

fun <Vertex, Edge> Graph<Vertex, Edge>.addEdgeMissingVertex(source: Vertex, target: Vertex) =
    this.addEdgeMissingVertex(source, target, this.edgeSupplier.get())

fun <Vertex, Edge> Graph<Vertex, Edge>.addEdgeMissingVertex(source: Vertex, target: Vertex, edge: Edge) {
    if (!this.containsVertex(source)) this.addVertex(source)
    if (!this.containsVertex(target)) this.addVertex(target)
    this.addEdge(source, target, edge)
}


fun <Vertex, E : DefaultEdge> Graph<Vertex, E>.toMermaid(
    vertexToString: Vertex.() -> String = { this.toString() }
): String {

    val lookup: Map<String, String> = this.vertexSet().withIndex()
        .associate { it.value.vertexToString() to "s${it.index}" }

    val diagramBuilder = StringBuilder("stateDiagram-v2\n")
    lookup.forEach { (vertex, id) ->
        diagramBuilder.append("state \"$vertex\" as $id\n")
    }

    this.edgeSet().forEach { edge ->
        val source: String = lookup[this.getEdgeSource(edge).vertexToString()]!!
        val target: String = lookup[this.getEdgeTarget(edge).vertexToString()]!!
        diagramBuilder.append(source)
        diagramBuilder.append(" --> ")
        diagramBuilder.append(target)
        diagramBuilder.append("\n")
    }
    return diagramBuilder.toString()
}


fun <Vertex, Edge> Graph<Vertex, Edge>.reachableVerticesFrom(source: Vertex): Set<Vertex> {
    val reached = mutableSetOf<Vertex>()
    BreadthFirstIterator(this, source).forEach(reached::add)
    return reached
}

fun <Vertex, Edge, G : Graph<Vertex, Edge>> G.neighborsOf(vertex: Vertex): Set<Vertex> =
    Graphs.neighborSetOf(this, vertex)

infix fun <Vertex, Edge> Vertex.neighborsIn(graph: Graph<Vertex, Edge>): Set<Vertex> =
    Graphs.neighborSetOf(graph, this)

infix fun <Vertex, Edge> Vertex.successorsIn(graph: Graph<Vertex, Edge>): List<Vertex> =
    Graphs.successorListOf(graph, this)

infix fun <Vertex, Edge> Vertex.predecessorIn(graph: Graph<Vertex, Edge>): List<Vertex> =
    Graphs.predecessorListOf(graph, this)

fun <V, E, G : Graph<V, E>> Map<V, Iterable<V>>.toJGraph(constructor: () -> G): G {
    val graph = constructor()
    this.forEach { (source, neighbours) ->
        neighbours.forEach { target ->
            graph.addEdgeMissingVertex(source, target)
        }
    }
    return graph
}

fun <V> Map<V, Iterable<V>>.toDirectedJGraph(): DefaultDirectedGraph<V, DefaultEdge> =
    this.toJGraph { DefaultDirectedGraph<V, DefaultEdge>(DefaultEdge::class.java) }

fun <V> Map<V, Iterable<V>>.toUnDirectedJGraph(): DefaultUndirectedGraph<V, DefaultEdge> =
    this.toJGraph { DefaultUndirectedGraph<V, DefaultEdge>(DefaultEdge::class.java) }

fun <V> Map<V, Iterable<Pair<String, V>>>.toLabeledGraph(): DirectedPseudograph<V, LabeledEdge> {
    val graph = DirectedPseudograph<V, LabeledEdge>(LabeledEdge::class.java)
    this.forEach { (source, neighbours) ->
        neighbours.forEach { (label, target) ->
            graph.addEdgeMissingVertex(source, target, LabeledEdge(label))
        }
    }
    return graph
}

fun <Vertex, E : DefaultEdge> Graph<Vertex, E>.toDOT(
    vertexToString: Function<Vertex, String> = IntegerIdProvider()
): String {
    val out = ByteArrayOutputStream()
    DOTExporter<Vertex, E>(vertexToString).exportGraph(this, out)
    return out.toString()
}