package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.List;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {

        List<EdgeWithData<Room, Wall>> edges = new ArrayList<>();

        for (Wall wall : walls) {
            Room room1 = wall.getRoom1();
            Room room2 = wall.getRoom2();

            double weight = rand.nextDouble();
            EdgeWithData<Room, Wall> edge = new EdgeWithData<>(room1, room2, weight, wall);
            edges.add(edge);
        }

        MazeGraph graph = new MazeGraph(edges);

        var mst = minimumSpanningTreeFinder.findMinimumSpanningTree(graph);

        Set<Wall> removeWalls = new HashSet<>();
        if (mst.exists()) {
            for (EdgeWithData<Room, Wall> edge: mst.edges()) {
                removeWalls.add(edge.data());
            }
        }
        return removeWalls;
    }
}
