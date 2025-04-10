package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, Double> dist = new HashMap<>();
        Map<V, E> shortestPaths = new HashMap<>();
        ExtrinsicMinPQ<V> minPQ = createMinPQ();

        dist.put(start, 0.0);
        minPQ.add(start, 0.0);

        while (!minPQ.isEmpty()) {
            V current = minPQ.removeMin();

            if (current.equals(end)) {
                break;
            }

            for (E edge : graph.outgoingEdgesFrom(current)) {
                V neighbor = edge.to();
                double weight = edge.weight();
                double newDist = dist.get(current) + weight;
                double oldDist = dist.getOrDefault(neighbor, Double.POSITIVE_INFINITY);

                if (newDist < oldDist) {
                    dist.put(neighbor, newDist);
                    shortestPaths.put(neighbor, edge);
                    if (minPQ.contains(neighbor)) {
                        minPQ.changePriority(neighbor, newDist);
                    }
                    else {
                        minPQ.add(neighbor, newDist);
                    }
                }
            }
        }
        return shortestPaths;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {

        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }

        List<E> pathedges = new ArrayList<>();
        V current = end;

        while (!current.equals(start)) {
            E edge = spt.get(current);
            if (edge == null) {
                return new ShortestPath.Failure<>();
            }
            pathedges.add(edge);
            current = edge.from();
        }
        Collections.reverse(pathedges);
        return new ShortestPath.Success<>(pathedges);
    }
}
