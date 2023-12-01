package common

import org.jgrapht.Graph
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedPseudograph
import org.jgrapht.traverse.BreadthFirstIterator

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

fun <V> Map<V, Iterable<V>>.toDirectedJGraph(): DefaultDirectedGraph<V, DefaultEdge> {
    val graph = DefaultDirectedGraph<V, DefaultEdge>(DefaultEdge::class.java)
    this.forEach { (source, neighbours) ->
        neighbours.forEach { target ->
            graph.addEdgeMissingVertex(source, target)
        }
    }
    return graph
}

fun <V> Map<V, Iterable<Pair<String, V>>>.toLabeledGraph() {
    val graph = DirectedPseudograph<V, LabeledEdge>(LabeledEdge::class.java)
    this.forEach { (source, neighbours) ->
        neighbours.forEach { (label, target) ->
            graph.addEdgeMissingVertex(source, target, LabeledEdge(label))
        }
    }
}