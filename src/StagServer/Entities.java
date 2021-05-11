package StagServer;

import java.lang.reflect.Array;
import java.util.*;

public class Entities {

    private HashMap<String,String> paths;
    private HashMap<String, Player> players;
    private LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;
    /* MIGHT WANT TO MAKE THE ARRAYLIST A TUPLE BUT NEED TO USE NEW LIBRARY */
    private LinkedHashMap<String, Location> newLocations;


    //private TreeMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;


    public Entities() {
        locations = new LinkedHashMap<>();
        paths = new HashMap<>();
        players = new HashMap<>();
        newLocations = new LinkedHashMap<>();
    }

    public void setLocation(ArrayList<String> location, HashMap<String, HashMap<String,String>> elements) {
        locations.put(location, elements);
    }

    public void setNewLocation(String locationName, Location location) {
        newLocations.put(locationName, location);
    }

    public void setPath(String startLocation, String endLocation) {
        paths.put(startLocation, endLocation);
    }


    /* FOR TESTING */
    public LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String, String>>> getLocations() {
        return locations;
    }

    public LinkedHashMap<String, Location> getNewLocations() {
        for(int i = 0; i < newLocations.size(); i++) {
            System.out.println("START");
            System.out.println(newLocations.get("start").getDescription());
            System.out.println(newLocations.get("start").getLocationContents());
            System.out.println("FOREST");
            System.out.println(newLocations.get("forest").getDescription());
            System.out.println(newLocations.get("forest").getLocationContents());
            System.out.println("CELLAR");
            System.out.println(newLocations.get("cellar").getDescription());
            System.out.println(newLocations.get("cellar").getLocationContents());
            System.out.println("UNPLACED");
            System.out.println(newLocations.get("unplaced").getDescription());
            System.out.println(newLocations.get("unplaced").getLocationContents());
        }
        return newLocations;
    }



    /* FOR TESTING */
    public HashMap<String, String> getPaths() {
        return paths;
    }

    public Boolean playerExists(String playerName) {
        if(players.containsKey(playerName)) {
            return true;
        }
        return false;
    }

    /* TO DELETE */
   /* public void setPlayerLocation(String playerName) {
        players.get(playerName).setCurrentLocation(getStartLocation());
    }*/

    public void setPlayerNewLocation(String playerName) {
        players.get(playerName).setNewCurrentLocation(getStartNewLocation());
    }

    public void addNewPlayer(String playerName) {
        Player newPlayer = new Player();
        newPlayer.setStartLocation(getStartLocation());
        players.put(playerName, newPlayer);
    }

    public String getArtefact(ArrayList<String> currentLocation, String artefactName) {
        return locations.get(currentLocation).get("artefacts").get(artefactName);
    }

    /* TO DELETE */
    public ArrayList<String> getStartLocation() {
        return locations.entrySet().iterator().next().getKey();
    }

    public String getStartNewLocation() {
        return newLocations.entrySet().iterator().next().getKey();
    }

    /* TO DELETE */
   /* public ArrayList<String> getPlayerLocation(String currentPlayer) {
        return players.get(currentPlayer).getCurrentLocation();
    }*/

    public String getPlayerNewLocation(String currentPlayer) {
        return players.get(currentPlayer).getNewCurrentLocation();
    }

    public void updatePlayerLocation(String playerName, String locationName) {
        ArrayList<String> location = new ArrayList<>();
        location.add(locationName);
        //location.add()
        //players.get(playerName).setCurrentLocation();
        players.get(playerName).setNewCurrentLocation(locationName);
    }

    /* Look command */
    public ArrayList<String> lookCommand(ArrayList<String> currentLocation, String newCurrentLocation) {
        HashMap<String,String> entities;
        Location location;
        ArrayList<String> output = new ArrayList<>();

        /* NEED TO DEAL WITH NULL EXCEPTIONS FOR EACH OF THESE*/

        //output.add("You are in: " + currentLocation.get(1));
        output.add("You are in: " + newLocations.get(newCurrentLocation).getDescription());

       // entities = locations.get(currentLocation).get("furniture");
       // System.out.println("cur locaiton: " +  currentLocation);
        location = newLocations.get(newCurrentLocation);
        //System.out.println("1");
        //System.out.println(entities);
       // System.out.println("I'm interested: " + location.getFurniture());
       // System.out.println("I'm interested: " + location.getDescription());

        /* new location */
        output.addAll(location.getFurniture());

        /* old location */
        /*for (String key: entities.keySet()) {
            processedEntities.add("A " + entities.get(key));
        }*/

        //entities = locations.get(currentLocation).get("artefacts");
        //System.out.println("2");
        //System.out.println(entities);
        /* new location */
        output.addAll(location.getArtefacts());
        /*
        for (String key: entities.keySet()) {
            output.add("A " + entities.get(key));
        }*/

        /*entities = locations.get(currentLocation).get("characters");
        System.out.println("3");
        System.out.println(entities);
        for (String key: entities.keySet()) {
            processedEntities.add(entities.get(key));
        }*/

        output.add("You can access from here:\n" + paths.get(newCurrentLocation));

        return output;
    }



    /* TO DELETE */
    public void addArtefactToPlayer(String playerName, String currentLocation, String artefact) {
        System.out.println("adding artefact");
        System.out.println(playerName);
        System.out.println(artefact);
        String artefactDescription = newLocations.get(currentLocation).getArtefactDescription(artefact);
        players.get(playerName).addArtefactToInventory(artefact, artefactDescription);
    }

    public void addNewArtefactToPlayer(String currentPlayer, String currentLocation, String artefact) {

        /* Adding to player's inventory */

        players.get(currentPlayer).addArtefactToNewInventory(artefact);

    }

    /* TO DELETE */
    public void deleteArtefact(ArrayList<String> currentLocation, String artefactName) {
        locations.get(currentLocation).get("artefacts").remove(artefactName);
    }

    public void newDeleteArtefact(String currentLocation, String artefactName) {
        newLocations.get(currentLocation).deleteArtefact(artefactName);
    }


    /* FOR TESTING  */
    /* TO DELETE */
    public ArrayList<String> getPlayerInventory(String playerName) {
        System.out.println("getting inv");
        return players.get(playerName).getInventory();
    }

    /*public ArrayList<String> getPlayerNewInventory(String playerName) {
        ArrayList<String> entityNames = new ArrayList<>();
        ArrayList<String> entityDescriptions = new ArrayList<>();
        entityNames = players.get(playerName).getNewInventory();

        for(int i = 0; i < entityNames.size(); i++) {
            entityDescriptions.add()
        }*/


}




