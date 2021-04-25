package StagServer;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityParser {

    /* This class will receive a .dot file as its argument and parse it
    * It will then populate an entity class with the parsed values */

    private String entityFileName;
    private Entities entities;

    public EntityParser(String entityFileName) {
        entities = new Entities();
        this.entityFileName = entityFileName;
    }

    public void execute() {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entityFileName);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
            for(Graph g : subGraphs){
                System.out.printf("id = %s\n",g.getId().getId());

                ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                for (Graph g1 : subGraphs1){
                    ArrayList<Node> nodesLoc = g1.getNodes(false);
                    Node nLoc = nodesLoc.get(0);
                    ArrayList<String> location = new ArrayList<>();
                    location.add(nLoc.getId().getId());
                    location.add(nLoc.getAttribute("description"));

                    System.out.printf("\tid = %s, name = %s\n",g1.getId().getId(), nLoc.getId().getId());
                    System.out.println("location desc: " + nLoc.getAttribute("description"));

                    ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
                    HashMap<String, HashMap<String,String>> elements = new HashMap<>();
                    for (Graph g2 : subGraphs2) {

                        System.out.printf("\t\tid = %s\n", g2.getId().getId());
                        ArrayList<Node> nodesEnt = g2.getNodes(false);
                        HashMap<String,String> element = new HashMap<>();
                        for (Node nEnt : nodesEnt) {
                            System.out.println("1");
                            System.out.printf("\t\t\tid = %s, description = %s\n", nEnt.getId().getId(), nEnt.getAttribute("description"));

                            System.out.println("2");
                            element.put(nEnt.getId().getId(),nEnt.getAttribute("description"));
                            System.out.println("element: " + element);

                            System.out.println("3");
                            System.out.println("elements1: " + elements);

                        }
                        elements.put(g2.getId().getId(), element);
                        System.out.println("elements2: " + elements);


                        //System.out.println(location);
                        //System.out.println(elements);

                    }
                    entities.setLocations(location, elements);
                }

                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges){
                    System.out.printf("Path from %s to %s\n", e.getSource().getNode().getId().getId(), e.getTarget().getNode().getId().getId());
                }
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (com.alexmerz.graphviz.ParseException pe) {
            System.out.println(pe);
        }
    }

    public void printing() {
        System.out.println("PRINTING");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(entities.getLocations());
    }
}

