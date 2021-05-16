package StagServer;

import java.util.*;

public class Entities {

    private HashMap<String, ArrayList<String>> paths;
    private HashMap<String, Player> players;
    private LinkedHashMap<String, Location> locations;

    public Entities() {
        players = new HashMap<>();
        locations = new LinkedHashMap<>();
        paths = new HashMap<>();
    }

    public void addLocation(String locationName, Location location) {
        locations.put(locationName, location);
    }

    public void setPath(String startLocation, String endLocation) {
        if(paths.containsKey(startLocation)) {
            paths.get(startLocation).add(endLocation);
        }
        else {
            ArrayList<String> endLocations = new ArrayList<>();
            endLocations.add(endLocation);
            paths.put(startLocation, endLocations);
        }
    }

    // FOR TESTING ONLY
    public LinkedHashMap<String, Location> getLocations() {
        for(int i = 0; i < locations.size(); i++) {
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
        return locations;
    }

    public boolean subjectsExist(ArrayList<String> subjects, String currentLocation, String currentPlayer) {
        for(int i = 0; i < subjects.size(); i++) {
            if(!locations.get(currentLocation).itemExists(subjects.get(i)) &&
                    !players.get(currentPlayer).artefactExists(subjects.get(i))) {
                return false;
            }
        }
        return true;
    }

    /* Executing "Action" command */
    public void removeItemConsumed(String consumedItem, String currentLocation, String currentPlayer) {
        // If in location, remove from location
        if(locations.get(currentLocation).itemExists(consumedItem)) {
            locations.get(currentLocation).deleteItem(consumedItem);
        }
        // If in players' inventory, remove from inv
        if(players.get(currentPlayer).artefactExists(consumedItem)) {
            players.get(currentPlayer).deleteArtefact(consumedItem);
        }
    }

    /* Executing "Action" command */
    public void addItemProduced(String producedItem, String currentLocation) {
        System.out.println("ADD ITEM PRODUCED");
        // Deal with adding health here

        // If a location, add path to that location
        if(locations.containsKey(producedItem)) {
            paths.get(currentLocation).add(producedItem);
        }

        // Otherwise, simply add item to current location
        for (String key : locations.keySet()) {
            // If it exists as an artefact, add as an artefact
            if(locations.get(key).artefactExists(producedItem)) {
                String artefactDescription = locations.get(key).getArtefactDescription(producedItem);
                locations.get(currentLocation).addArtefact(producedItem, artefactDescription);
                removeFromLocation(producedItem, key);
            }
            // If it exists as furniture, add as furniture
            if(locations.get(key).furnitureExists(producedItem)) {
                String furnitureDescription = locations.get(key).getFurnitureDescription(producedItem);
                locations.get(currentLocation).addFurniture(producedItem, furnitureDescription);
                removeFromLocation(producedItem, key);
            }
            // If it exists as a character, add as character
            if(locations.get(key).characterExists(producedItem)) {
                String characterDescription = locations.get(key).getCharacterDescription(producedItem);
                locations.get(currentLocation).addCharacter(producedItem, characterDescription);
                removeFromLocation(producedItem, key);
            }
        }
    }

    private void removeFromLocation(String producedItem, String location) {
       /* for (String key : locations.keySet()) {
            if(locations.get(key).itemExists(producedItem)) {
                locations.get(key).deleteItem(producedItem);
            }
        }*/
        locations.get(location).deleteItem(producedItem);
        System.out.println("Artefacts in unplaced: " + locations.get("unplaced").getArtefacts());
    }

    public HashMap<String, ArrayList<String>> getPaths() {
        return paths;
    }

    public Boolean playerExists(String playerName) {
        if(players.containsKey(playerName)) {
            return true;
        }
        return false;
    }

    public void setPlayerLocation(String playerName) {
        players.get(playerName).setCurrentLocation(getStartLocation());
    }

    public void addNewPlayer(String playerName) {
        Player newPlayer = new Player();
        newPlayer.setCurrentLocation(getStartLocation());
        players.put(playerName, newPlayer);
    }

    public String getStartLocation() {
        return locations.entrySet().iterator().next().getKey();
    }

    public String getPlayerLocation(String currentPlayer) {
        return players.get(currentPlayer).getCurrentLocation();
    }
    /* IS IT POSSIBLE TO SIMPLIFY THIS THROUGH A CONTAINS. */
    public String matchLocationArtefact(ArrayList<String> command, String currentLocation) {
        for(int i = 0; i < command.size(); i++) {
            if (locations.get(currentLocation).artefactExists(command.get(i))) {
                return command.get(i);
            }
        }
        return null;
    }
    /* IS IT POSSIBLE TO SIMPLIFY THIS THROUGH A CONTAINS. */
    public String matchPlayerArtefact(ArrayList<String> command, String currentPlayer) {
        for(int i = 0; i < command.size(); i++) {
            if (players.get(currentPlayer).artefactExists(command.get(i))) {
                return command.get(i);
            }
        }
        return null;
    }
    /* IS IT POSSIBLE TO SIMPLIFY THIS THROUGH A CONTAINS. */
    public String matchLocation(ArrayList<String> command, String currentLocation) {
        for(int i = 0; i < command.size(); i++) {
            if (paths.get(currentLocation).contains(command.get(i))) {
                return command.get(i);
            }
        }
        return null;
    }

    public void updatePlayerLocation(String playerName, String locationName) {
        players.get(playerName).setCurrentLocation(locationName);
    }

    /* Look command */
    public ArrayList<String> executeLook(String currentLocation) {
        Location location;
        ArrayList<String> output = new ArrayList<>();

        /* NEED TO DEAL WITH NULL EXCEPTIONS FOR EACH OF THESE*/

        output.add("You are in: " + locations.get(currentLocation).getDescription());

        location = locations.get(currentLocation);

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

        output.add("You can access from here:");
        output.addAll(paths.get(currentLocation));

        return output;
    }

    public void addArtefactToLocation(String locationName, String playerName, String artefactName) {
        /* get artefact description */
        String artefactDescription = players.get(playerName).getArtefactDescription(artefactName);
        /* place name and description into location - check if artefacts exist first */
        locations.get(locationName).addArtefact(artefactName, artefactDescription);
    }

    public void addArtefactToPlayer(String playerName, String currentLocation, String artefact) {
        String artefactDescription = locations.get(currentLocation).getArtefactDescription(artefact);
        players.get(playerName).addArtefactToInventory(artefact, artefactDescription);
    }

    public void deleteArtefact(String currentLocation, String artefactName) {
        locations.get(currentLocation).deleteItem(artefactName);
    }

    /* FOR TESTING  */
    public ArrayList<String> getPlayerInventory(String playerName) {
        return players.get(playerName).getInventory();
    }

    public void deleteArtefactFromPlayer(String playerName, String artefactName) {
        players.get(playerName).deleteArtefact(artefactName);
    }


}




