import graph.GMLGraphFactory;
import graph.Graph;
import graph.Node;
import graphproperties.GraphProperties;
import pathfinding.DijkstraPathFinder;
import pathfinding.PathFinder;
import pathfinding.PathUtils;

import java.io.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        Graph graph = GMLGraphFactory.createFromGMLFile("lesmis.gml");

        graph.printGraph();

        PathFinder pathFinder = new DijkstraPathFinder(graph);

        for (Map.Entry<Integer, Node> currentStart : graph.getNodes().entrySet()) {
            for (Map.Entry<Integer, Node> currentEnd : graph.getNodes().entrySet()) {

                if (currentEnd.equals(currentStart)) continue;

                System.out.println("From : " + currentStart.getValue().getLabel() + " To : " + currentEnd.getValue().getLabel());


                pathFinder.computeShortestPath(currentStart.getValue().getLabel(), currentEnd.getValue().getLabel());
                PathUtils.printPath(pathFinder.getPath(),  pathFinder.getPathLength());

            }
        }

        // graph properties :
        GraphProperties dijkstraGraphProperties = new GraphProperties(new DijkstraPathFinder(graph), graph);
        dijkstraGraphProperties.computeRadiusAndDiameter();

        System.out.format("Graph radius : %s", dijkstraGraphProperties.getRadius());
        System.out.println();
        System.out.println("Path associated with the graph diameter :");
        PathUtils.printPath(dijkstraGraphProperties.getDiameterPath(), dijkstraGraphProperties.getDiameterPathLength());

    }
}
