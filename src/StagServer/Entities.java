package StagServer;

import java.util.*;

public class Entities {

    private HashMap<String,String> paths;
    /* MIGHT WANT TO MAKE THE ARRAYLIST A TUPLE BUT NEED TO USE NEW LIBRARY */
    //private TreeMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;

    private LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;

    public Entities() {
        locations = new LinkedHashMap<>();
        paths = new HashMap<>();
    }

    public void setLocation(ArrayList<String> location, HashMap<String, HashMap<String,String>> elements) {
        locations.put(location, elements);
    }

    public void setPath(String startLocation, String endLocation) {
        paths.put(startLocation, endLocation);
    }

    /* FOR TESTING */
    public LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String, String>>> getLocations() {
        return locations;
    }

    /* FOR TESTING */
    public HashMap<String, String> getPaths() {
        return paths;
    }

}
