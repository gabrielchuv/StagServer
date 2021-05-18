package StagServer;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityParser {

    private String entityFileName;
    private Entities entities;

    public EntityParser(String entityFileName) {
        entities = new Entities();
        this.entityFileName = entityFileName;
    }

    public Entities parse() {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entityFileName);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();

            /* Parsing locations */
            for(Graph g : subGraphs){
                ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                for (Graph g1 : subGraphs1){
                    ArrayList<Node> nodesLoc = g1.getNodes(false);
                    Node nLoc = nodesLoc.get(0);
                    /* Extract location's name */
                    String locationName = nLoc.getId().getId();
                    /* Create a new location instance */
                    Location location = new Location();
                    /* Populate location's description */
                    location.setDescription(nLoc.getAttribute("description"));

                    /* Extracting elements inside of location (artefact, furniture...) */
                    ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
                    HashMap<String, HashMap<String,String>> elements = new HashMap<>();
                    for (Graph g2 : subGraphs2) {
                        ArrayList<Node> nodesEnt = g2.getNodes(false);
                        HashMap<String,String> element = new HashMap<>();
                        for (Node nEnt : nodesEnt) {
                            /* Extracting contents of individual elements */
                            element.put(nEnt.getId().getId(),nEnt.getAttribute("description"));
                        }
                        elements.put(g2.getId().getId(), element);
                    }
                    /* Populate location's contents */
                    location.setLocationContents(elements);
                    /* Adding a new location to the Entities class */
                    entities.addLocation(locationName, location);
                }

                /* Parsing paths */
                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges){
                    entities.setPath(e.getSource().getNode().getId().getId(), e.getTarget().getNode().getId().getId());
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (com.alexmerz.graphviz.ParseException pe) {
            System.out.println(pe);
        }
        return(entities);
    }
}

