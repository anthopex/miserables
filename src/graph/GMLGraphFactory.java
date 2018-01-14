package graph;



import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GMLGraphFactory {
    public static Graph createFromGMLFile(String filepath) throws IOException {
        // Ouvre le JSON.
        String text = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);

        // Cré un objet JSON dont on peut récupérer les données
        List<List<Map<String, Object>>> gml = extractGml(text);

//        for (List<Map<String, Object>> currentList : gml) {
//            System.out.println("New ObjectList");
//            for (Map<String, Object> currentObject : currentList) {
//                System.out.println("Object");
//                for (Map.Entry<String, Object> currentAttribute : currentObject.entrySet()) {
//                    System.out.println(currentAttribute.getKey() + " - " + currentAttribute.getValue());
//
//                }
//            }
//
//        }

//        return null;
        return createFromGMLObject(gml);
    }

    public static List<List<Map<String, Object>>> extractGml(String text) {
        List<List<Map<String, Object>>> gml = new ArrayList<>();


        int indentationLevel = 0;
        List<Map<String, Object>> currentList = new ArrayList<>();
        Map<String, Object> currentObject = new HashMap<>();
        String lastObjectType = "  node";
        for (String currentLine : text.split("\n")) {
            if ("  [".equals(currentLine) || "[".equals(currentLine)) {
                indentationLevel++;
            } else if ("  ]".equals(currentLine) && indentationLevel == 2) {
                currentList.add(currentObject);
                indentationLevel--;
            } else if ("]".equals(currentLine) && indentationLevel == 1) {
                gml.add(currentList);
                indentationLevel--;
            } else if ("  node".equals(currentLine) || "  edge".equals(currentLine)) {
                currentObject = new HashMap<>();

                if (!lastObjectType.equals(currentLine)) {
                    gml.add(currentList);
                    currentList = new ArrayList<>();
                }


                lastObjectType = currentLine;
            } else {
                String[] currentSplittedLine = currentLine.split(" ");
                if (currentSplittedLine.length < 2) continue;
                currentObject.put(currentSplittedLine[currentSplittedLine.length - 2], currentSplittedLine[currentSplittedLine.length - 1].replaceAll("\"", ""));
            }


        }

        return gml;
    }


    private static Graph createFromGMLObject(List<List<Map<String, Object>>> gml) {
        // Cré un objet JSON qui contient la liste des stations + un itérateur pour les parcourir
        List<Map<String, Object>> characters = gml.get(0);

        // Cré un objet JSON qui contient les données sur les lignes + un itérateur pour les parcourir
        List<Map<String, Object>> relations = gml.get(1);


        Graph graph = new Graph();

        addStationsToGraph(graph, characters);

        addEdgesBetweenStations(graph, relations);

        return graph;
    }

    private static void addStationsToGraph(Graph graph,  List<Map<String, Object>> characters) {
        Map<Integer, Node> nodes = graph.getNodes();
//        Iterator iteratorStation = stations.keys();

        // Parcourt les stations et les ajoute au graph.
        for (Map<String, Object> currentChar : characters) {
            // Cré un objet JSON avec les données de la station
//            JSONObject station = stations.getJSONObject(iteratorStation.next().toString());


            // Creates the station and add it to the graph :
            Integer id = Integer.parseInt(currentChar.get("id").toString());
            nodes.put(id,
                    new Node(
                            currentChar.get("label").toString()
                    )
            );

        }
    }


    private static void addEdgesBetweenStations(Graph graph, List<Map<String, Object>> relations) {
        for (int i = 0; i < relations.size(); i++) {
            // Cré une liste contenant la correspondance (deux ou trois arrêts)
            Map<String, Object> relation = relations.get(i);


            int from = Integer.parseInt(relation.get("source").toString());
            int to = Integer.parseInt(relation.get("target").toString());
            int weight = Integer.parseInt(relation.get("value").toString());
            graph.addUndirectedEdgeBetweenNodes(from, to, weight);




//
//            // We only need to handle metro routes (we don't care about other means of transportations,
//            // and connections are handled using the fact that a station has several edges from/to itself)
//            JSONArray stops = route.getJSONArray("arrets");
//            String lineName = route.getString("ligne");
//
//            // Iterate over the stops [0, n - 1] to add edges between them
//            // We add a single, directed edge because "routes" in the JSON contains routes for each
//            // destination of the metro lines :
//            for (int j = 0; j < stops.length() - 1; j++) {
//                int nodeFromIndex = Integer.parseInt(stops.getString(j));
//                int nodeToIndex = Integer.parseInt(stops.getString(j + 1));
//
//                if (!edgeExists(graph, nodeFromIndex, nodeToIndex, lineName, EdgeType.METRO_LINE)) {
//                    graph.addEdgeBetweenNodes(nodeFromIndex, nodeToIndex, lineName, EdgeType.METRO_LINE);
//                }
//            }

        }
    }






//
//    private static void addFootConnectionsToStations(Graph graph, JSONArray correspondances) {
//        // Iterate over the list of foot connections :
//        for (int i = 0; i < correspondances.length(); i++) {
//            JSONArray correspondance = correspondances.getJSONArray(i);
//
//            Map<Integer, Node> nodes = graph.getNodes();
//
//            List<Integer> nodesToLink = new ArrayList<>();
//
//            // Only keep stations we know about (aka metro stations) :
//            for (int j = 0; j < correspondance.length(); j ++) {
//                try {
//                    String correspondanceLine = correspondance.getString(j);
//                    int nodeIndex = Integer.parseInt(correspondanceLine);
//                    Node node = nodes.get(nodeIndex);
//                    if (node != null) nodesToLink.add(nodeIndex);
//                } catch (Exception e) {
//                    // No-op ! This station was not recognised, we go on to the next one
//                }
//            }
//
//
//            if (nodesToLink.size() == 2) {
//                graph.addUndirectedEdgeBetweenNodes(nodesToLink.get(0), nodesToLink.get(1), "Correspondance à pied", EdgeType.CONNECTION);
//            }
//
//            if (nodesToLink.size() == 3) {
//                graph.addUndirectedEdgeBetweenNodes(nodesToLink.get(0), nodesToLink.get(1), "Correspondance à pied", EdgeType.CONNECTION);
//                graph.addUndirectedEdgeBetweenNodes(nodesToLink.get(0), nodesToLink.get(2), "Correspondance à pied", EdgeType.CONNECTION);
//                graph.addUndirectedEdgeBetweenNodes(nodesToLink.get(1), nodesToLink.get(2), "Correspondance à pied", EdgeType.CONNECTION);
//            }
//
//            if (nodesToLink.size() > 3) {
//                throw new RuntimeException("Tu m'as oublié, Jack");
//            }
//        }
//    }
//
//
//    private static boolean edgeExists(Graph graph, int nodeFromIndex, int nodeToIndex, String lineName, EdgeType edgeType) {
//        Node edgeNodeTo = graph.getNodes().get(nodeToIndex);
//        Node edgeNodeFrom = graph.getNodes().get(nodeFromIndex);
//
//        for (Edge e: graph.getEdges()) {
//            Node nodeTo = e.getNodeTo();
//            Node nodeFrom = e.getNodeFrom();
//
//            if (nodeTo == edgeNodeTo
//                    && nodeFrom == edgeNodeFrom
//                    && lineName.equals(e.getLine())
//                    && edgeType == e.getType()) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//
//    private static void computeNeighborsAndWeights(Graph graph) {
//        Map<Integer, Node> nodes = graph.getNodes();
//
//        nodes.forEach((Integer key, Node node) -> JSONGraphFactory.computeNodeNeighborsAndWeights(node));
//    }
//
//    private static void computeNodeNeighborsAndWeights(Node node) {
//        List<Edge> edges = node.getEdges();
//
//        edges.forEach(edge ->  {
//            // Computing edges weights using geocalc to calculate the distance between two (lat, lng) pairs :
//            Coordinate latStation1 = new DegreeCoordinate(edge.getNodeFrom().getLat());
//            Coordinate lngStation1 = new DegreeCoordinate(edge.getNodeFrom().getLng());
//            Point station1 = new Point(latStation1, lngStation1);
//
//            Coordinate latStation2 = new DegreeCoordinate(edge.getNodeTo().getLat());
//            Coordinate lngStation2 = new DegreeCoordinate(edge.getNodeTo().getLng());
//            Point station2 = new Point(latStation2, lngStation2);
//
//            // distance in meters :
//            Double distance = EarthCalc.getHarvesineDistance(station1, station2);
//            Long dL = Math.round(distance);
//            edge.setWeight(dL.intValue());
//        });
//    }
}