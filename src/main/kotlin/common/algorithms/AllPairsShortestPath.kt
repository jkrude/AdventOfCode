package common.algorithms

object AllPairsShortestPath {

    /** Floyd Warshall algorithm to find the shortest distance between all nodes.
     * @param distances The matrix representing the distance between i-th and j-th element
     * @param nonExistingEdgeValue The value at distances[i][j] if there is no current route between i and j
     *
     */
    fun floydWarshall(distances: Array<IntArray>, nonExistingEdgeValue: Int = Int.MAX_VALUE) {
        // The outer loop iterates through each node in the graph, which will be used as an intermediate
        // node in the shortest path calculation
        for (k in distances.indices) {
            // The inner loops iterate through all pairs of nodes
            for (i in distances.indices) {
                for (j in distances.indices) {
                    // Check if a route between i -> k -> j exists
                    if (distances[i][k] == nonExistingEdgeValue || distances[k][j] == nonExistingEdgeValue) continue

                    // If the distance from node i to node j using intermediate node k is shorter than the
                    // current best known distance, update the distance matrix to reflect this new shorter distance
                    if (distances[i][j] == nonExistingEdgeValue ||
                        distances[i][j] > distances[i][k] + distances[k][j]
                    ) {
                        distances[i][j] = distances[i][k] + distances[k][j]
                    }
                }
            }
        }
    }
}