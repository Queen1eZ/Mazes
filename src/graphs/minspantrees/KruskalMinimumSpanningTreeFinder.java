package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();
        List<V> vertices = new ArrayList<>(graph.allVertices());
        // Go through all vertices, initializing each vertex to an independent set
        for (V vertex : vertices) {
            disjointSets.makeSet(vertex);
        }
        // Edges used to store the minimum spanning tree
        List<E> mstEdge = new ArrayList<>();

        // Go through all edges, processing from smallest to largest by weight
        for (E edge : edges) {
            // If the two vertices of an edge are not in the same set, adding the edge does not form a ring
            if (disjointSets.findSet(edge.from()) != disjointSets.findSet(edge.to())) {
                // Add this edge to the minimum spanning tree
                mstEdge.add(edge);
                disjointSets.union(edge.from(), edge.to());
            }
        }

        // Check whether the graph has no vertices
        boolean isGraphEmpty = vertices.isEmpty();
        // If the graph is empty, a successful minimum spanning tree is returned
        if (isGraphEmpty) {
            return new MinimumSpanningTree.Success<>(mstEdge);
        }
        // Check whether the number of edges in the minimum spanning tree is equal to the number of vertices minus one
        boolean isMstValid = mstEdge.size() == vertices.size() - 1;
        if (isMstValid) {
            // If the nature of the spanning tree is satisfied, the minimum successful spanning tree is returned
            return new MinimumSpanningTree.Success<>(mstEdge);
        }
        return new MinimumSpanningTree.Failure<>();
    }
}
