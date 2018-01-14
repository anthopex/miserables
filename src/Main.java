import graph.GMLGraphFactory;
import graph.Graph;
import graph.Node;
import graphproperties.GraphProperties;
import pathfinding.DijkstraPathFinder;
import pathfinding.PathFinder;
import pathfinding.PathUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        Graph graph = GMLGraphFactory.createFromGMLFile("lesmis.txt");

//        graph.printGraph();

        PathFinder pathFinder = new DijkstraPathFinder(graph);

//        for (Map.Entry<Integer, Node> currentStart : graph.getNodes().entrySet()) {
//            for (Map.Entry<Integer, Node> currentEnd : graph.getNodes().entrySet()) {
//
//                if (currentEnd.equals(currentStart)) continue;
//
//                System.out.println("From : " + currentStart.getValue().getLabel() + " To : " + currentEnd.getValue().getLabel());
//
//
//                pathFinder.computeShortestPath(currentStart.getValue().getLabel(), currentEnd.getValue().getLabel());
//                PathUtils.printPath(pathFinder.getPath(),  pathFinder.getPathLength());
//
//            }
//        }

        // graph properties and longest path of the metro :
        GraphProperties dijkstraGraphProperties = new GraphProperties(new DijkstraPathFinder(graph), graph);
        dijkstraGraphProperties.computeRadiusAndDiameter();

        System.out.format("Graph radius : %s", dijkstraGraphProperties.getRadius());
        System.out.println();
        System.out.println("Path associated with the graph diameter :");
        PathUtils.printPath(dijkstraGraphProperties.getDiameterPath(), dijkstraGraphProperties.getDiameterPathLength());

//        Graph miserables = buildGraph();
//
//        if (miserables == null) return;
//
//        miserables.printGraph();
    }

    private static Graph buildGraph() {
        FileInputStream fis;
        try{
            fis = new FileInputStream(new File("lesmis.txt"));
            BufferedReader br = new BufferedReader(new FileReader("lesmis.txt"));
            String line;
            ArrayList<String> source = new ArrayList<String>();
            ArrayList<String> target = new ArrayList<String>();
            ArrayList<String> value = new ArrayList<String>();
            String str;

            while ((line = br.readLine()) != null) {  //use case instead
                if (line.contains("source")) {
                    str = line.replaceAll("\\D+","");
                    source.add(str);
                }
                else if (line.contains("target")) {
                    str = line.replaceAll("\\D+","");
                    target.add(str);
                }
                else if (line.contains("value")) {
                    str = line.replaceAll("\\D+","");
                    value.add(str);
                }
            }

            ArrayList<ArrayList<String>> graphe = new ArrayList<>();
            graphe.add(source);
            graphe.add(target);
            graphe.add(value);
            System.out.println("Graphe : " + graphe);
            fis.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
