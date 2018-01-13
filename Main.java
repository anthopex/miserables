import graph.Graph;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Graph miserables = buildGraph();

        if (miserables == null) return;

        miserables.printGraph();
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
