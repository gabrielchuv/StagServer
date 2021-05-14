package StagServer;

import java.lang.reflect.Array;
import java.util.*;

public class Entities {

   // private HashMap<String,String> paths;
    private HashMap<String, ArrayList<String>> newPaths;
    private HashMap<String, Player> players;
    private LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;
    /* MIGHT WANT TO MAKE THE ARRAYLIST A TUPLE BUT NEED TO USE NEW LIBRARY */
    private LinkedHashMap<String, Location> newLocations;


    //private TreeMap<ArrayList<String>, HashMap<String, HashMap<String,String>>> locations;


    public Entities() {
        locations = new LinkedHashMap<>();
      //  paths = new HashMap<>();
        players = new HashMap<>();
        newLocations = new LinkedHashMap<>();
        newPaths = new HashMap<>();
    }

    public void setLocation(ArrayList<String> location, HashMap<String, HashMap<String,String>> elements) {
        locations.put(location, elements);
    }

    public void setNewLocation(String locationName, Location location) {
        newLocations.put(locationName, location);
    }

    /*public void setPath(String startLocation, String endLocation) {
        paths.put(startLocation, endLocation);
    }*/

    public void setNewPath(String startLocation, String endLocation) {
        if(newPaths.containsKey(startLocation)) {
            newPaths.get(startLocation).add(endLocation);
        }
        else {
            ArrayList<String> endLocations = new ArrayList<>();
            endLocations.add(endLocation);
            newPaths.put(startLocation, endLocations);
        }
    }


    /* FOR TESTING */
    public LinkedHashMap<ArrayList<String>, HashMap<String, HashMap<String, String>>> getLocations() {
        return locations;
    }

    public LinkedHashMap<String, Location> getNewLocations() {
        for(int i = 0; i < newLocations.size(); i++) {
           /* System.out.println("START");
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
            System.out.println(newLocations.get("unplaced").getLocationContents());*/
        }
        return newLocations;
    }

    public boolean subjectsExist(ArrayList<String> subjects, String currentLocation, String currentPlayer) {
        for(int i = 0; i < subjects.size(); i++) {
            if(!newLocations.get(currentLocation).itemExists(subjects.get(i)) &&
                    !players.get(currentPlayer).itemExists(subjects.get(i))) {
                return false;
            }
        }
        return true;
    }


    public void removeItemConsumed(String consumedItem, String currentLocation, String currentPlayer) {
        // If in location, remove from location
        if(newLocations.get(currentLocation).itemExists(consumedItem)) {
            newLocations.get(currentLocation).deleteItem(consumedItem);
        }
        // If in players' inventory, remove from inv
        if(players.get(currentPlayer).itemExists(consumedItem)) {
            players.get(currentPlayer).deleteArtefact(consumedItem);
        }
    }

    public void addItemProduced(String producedItem, String currentLocation) {

        // Deal with adding health here

        // If a location, add path to that location
        if(newLocations.containsKey(producedItem)) {
            newPaths.get(currentLocation).add(producedItem);
        }

        // Otherwise, simply add item to current location
        // If it exists as an artefact, add as an artefact
        if(newLocations.get("unplaced").artefactExists(producedItem)) {
            String artefactDescription = newLocations.get("unplaced").getArtefactDescription(producedItem);
            newLocations.get(currentLocation).addArtefact(producedItem, artefactDescription);
        }
        // If it exists as furniture, add as furniture
        if(newLocations.get("unplaced").furnitureExists(producedItem)) {
            String furnitureDescription = newLocations.get("unplaced").getFurnitureDescription(producedItem);
            newLocations.get(currentLocation).addFurniture(producedItem, furnitureDescription);
        }
        // If it exists as a character, add as character
        if(newLocations.get("unplaced").furnitureExists(producedItem)) {
            String characterDescription = newLocations.get("unplaced").getCharacterDescription(producedItem);
            newLocations.get(currentLocation).addCharacter(producedItem, characterDescription);
        }

    }



    /* FOR TESTING */
   /* public HashMap<String, String> getPaths() {
        return paths;
    }*/

    public HashMap<String, ArrayList<String>> getNewPaths() {
        return newPaths;
    }


    public Boolean playerExists(String playerName) {
        if(players.containsKey(playerName)) {
            return true;
        }
        return false;
    }

    public void setPlayerLocation(String playerName) {
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

    public String getPlayerLocation(String currentPlayer) {
        return players.get(currentPlayer).getNewCurrentLocation();
    }

    public void updatePlayerLocation(String playerName, String locationName) {
       // ArrayList<String> location = new ArrayList<>();
        //location.add(locationName);
        //location.add()
        //players.get(playerName).setCurrentLocation();
        players.get(playerName).setNewCurrentLocation(locationName);
    }

    /* Look command */
    public ArrayList<String> executeLook(String newCurrentLocation) {
        HashMap<String,String> entities;
        Location location;
        ArrayList<String> output = new ArrayList<>();

        /* NEED TO DEAL WITH NULL EXCEPTIONS FOR EACH OF THESE*/

        output.add("You are in: " + newLocations.get(newCurrentLocation).getDescription());

        // System.out.println("New location: " + newCurrentLocation);
        location = newLocations.get(newCurrentLocation);
        // System.out.println("loc desc: " + location.getDescription());

        /* Furniture */
        if(location.furnitureExist()) {
            output.addAll(location.getFurniture());
        }

        /* Artefacts */
        if(location.artefactsExist()) {
            output.addAll(location.getArtefacts());
        }
        /* Characters */
        if(location.charactersExist()) {
            output.addAll(location.getCharacters());
        }

        //output.add("You can access from here:\n" + paths.get(newCurrentLocation));
        output.add("You can access from here:");
        output.addAll(newPaths.get(newCurrentLocation));

        return output;
    }

    public void addArtefactToLocation(String locationName, String playerName, String artefactName) {
        /* get artefact description */
        String artefactDescription = players.get(playerName).getArtefactDescription(artefactName);
        /* place name and description into location - check if artefacts exist first */
        newLocations.get(locationName).addArtefact(artefactName, artefactDescription);
    }





    /* TO DELETE */
    public void addArtefactToPlayer(String playerName, String currentLocation, String artefact) {
       // System.out.println("adding artefact");
        // System.out.println(playerName);
        //System.out.println(artefact);
        String artefactDescription = newLocations.get(currentLocation).getArtefactDescription(artefact);
        players.get(playerName).addArtefactToInventory(artefact, artefactDescription);
    }

    public void addNewArtefactToPlayer(String currentPlayer, String currentLocation, String artefact) {

        /* Adding to player's inventory */

        players.get(currentPlayer).addArtefactToNewInventory(artefact);

    }

    public void deleteArtefact(String currentLocation, String artefactName) {
        newLocations.get(currentLocation).deleteArtefact(artefactName);
    }


    /* FOR TESTING  */
    /* TO DELETE */
    public ArrayList<String> getPlayerInventory(String playerName) {
        // System.out.println("getting inv");
        return players.get(playerName).getInventory();
    }

    public void deleteArtefactFromPlayer(String playerName, String artefactName) {
        players.get(playerName).deleteArtefact(artefactName);
    }

    /*public ArrayList<String> getPlayerNewInventory(String playerName) {
        ArrayList<String> entityNames = new ArrayList<>();
        ArrayList<String> entityDescriptions = new ArrayList<>();
        entityNames = players.get(playerName).getNewInventory();

        for(int i = 0; i < entityNames.size(); i++) {
            entityDescriptions.add()
        }*/


}




