package graph;



import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GMLGraphFactory {
    public static Graph createFromGMLFile(String filepath) throws IOException {
        // Ouvre le GML.
        String text = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);

        // Cré un objet GML dont on peut récupérer les données
        List<List<Map<String, Object>>> gml = extractGml(text);
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
        // Cré un objet GML qui contient la liste des stations + un itérateur pour les parcourir
        List<Map<String, Object>> characters = gml.get(0);

        // Cré un objet GML qui contient les données sur les lignes + un itérateur pour les parcourir
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
            // Cré un objet GML avec les données de la station
//            GMLObject station = stations.getGMLObject(iteratorStation.next().toString());


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
            // Crée une liste contenant la correspondance (deux ou trois arrêts)
            Map<String, Object> relation = relations.get(i);


            int from = Integer.parseInt(relation.get("source").toString());
            int to = Integer.parseInt(relation.get("target").toString());
            int weight = Integer.parseInt(relation.get("value").toString());
            graph.addUndirectedEdgeBetweenNodes(from, to, weight);
        }
    }
}