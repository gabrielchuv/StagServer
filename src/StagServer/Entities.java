package StagServer;

import java.util.*;

public class Entities {

    private HashMap<String,String> paths;
    /* MIGHT WANT TO MAKE THE ARRAYLIST A TUPLE BUT NEED TO USE NEW LIBRARY */
    //private TreeMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;

    private LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;

    public Entities() {
        locations = new LinkedHashMap<>();
    }

    public void setLocations(ArrayList<String> location, HashMap<String, HashMap<String,String>> elements) {
        locations.put(location, elements);
    }

    public LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String, String>>> getLocations() {
        return locations;
    }

}
