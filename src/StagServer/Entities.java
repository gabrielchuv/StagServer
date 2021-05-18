package StagServer;

import java.util.*;

public class Entities {

    private final HashMap<String, ArrayList<String>> paths;
    private final HashMap<String, Player> players;
    private final LinkedHashMap<String, Location> locations;

    public Entities() {
        players = new HashMap<>();
        locations = new LinkedHashMap<>();
        paths = new HashMap<>();
    }

    /* Add a new location */
    public void addLocation(String locationName, Location location) {
        locations.put(locationName, location);
    }

    /* Add new path to a location */
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

    /* Check whether a given player exists */
    public Boolean playerExists(String playerName) {
        return players.containsKey(playerName);
    }

    /* Get a given player's location */
    public String getPlayerLocation(String currentPlayer) {
        return players.get(currentPlayer).getCurrentLocation();
    }

    /* Set player's start location */
    public void setPlayerLocation(String playerName) {
        players.get(playerName).setCurrentLocation(getStartLocation());
    }

    /* Set up new player in start location */
    public void addNewPlayer(String playerName) {
        Player newPlayer = new Player();
        newPlayer.setCurrentLocation(getStartLocation());
        players.put(playerName, newPlayer);
    }

    /* "Inventory" command */
    public ArrayList<String> getPlayerInventory(String playerName) {
        return players.get(playerName).getInventoryDescriptions();
    }

    /* Get command - Return an artefact if it exists in current location */
    public String matchLocationArtefact(ArrayList<String> command, String currentLocation) {
        for (String s : command) {
            if (locations.get(currentLocation).specificEntityExists("artefacts", s)) {
                return s;
            }
        }
        return null;
    }

    /* Get command - Add a given artefact to a player's inventory */
    public void addArtefactToPlayer(String playerName, String currentLocation, String artefact) {
        String artefactDescription = locations.get(currentLocation).getEntityDescription("artefacts", artefact);
        players.get(playerName).addArtefact(artefact, artefactDescription);
    }

    /* Get command - Delete artefact from location */
    public void deleteArtefact(String currentLocation, String artefactName) {
        locations.get(currentLocation).deleteEntity(artefactName);
    }

    /* Drop command - Return an artefact if it exists in player's inventory */
    public String matchPlayerArtefact(ArrayList<String> command, String currentPlayer) {
        for (String s : command) {
            if (players.get(currentPlayer).artefactExists(s)) {
                return s;
            }
        }
        return null;
    }

    /* Drop command - Add a given artefact from player's inventory to a location */
    public void addArtefactToLocation(String locationName, String playerName, String artefactName) {
        /* get artefact description */
        String artefactDescription = players.get(playerName).getArtefactDescription(artefactName);
        /* place name and description into location - check if artefacts exist first */
        //locations.get(locationName).addArtefact(artefactName, artefactDescription);
        locations.get(locationName).addEntity("artefacts", artefactName, artefactDescription);
    }

    /* Drop command - Delete artefact from player's inventory */
    public void deleteArtefactFromPlayer(String playerName, String artefactName) {
        players.get(playerName).deleteArtefact(artefactName);
    }

    /* Goto command - Return a location name if it exists as a path */
    public String matchLocation(ArrayList<String> command, String currentLocation) {
        for (String s : command) {
            if (paths.get(currentLocation).contains(s)) {
                return s;
            }
        }
        return null;
    }

    /* Goto command - Change a given player's location */
    public void updatePlayerLocation(String playerName, String locationName) {
        players.get(playerName).setCurrentLocation(locationName);
    }

    /* "Health" command */
    public ArrayList<String> getHealth(String currentPlayer) {
        ArrayList<String> healthMessage = new ArrayList<>();
        Integer health = players.get(currentPlayer).getHealth();
        healthMessage.add("You have " + health + " health units");
        return healthMessage;
    }

    /* Action command - Entity "consumption" */
    public void deleteEntitiesConsumed(ArrayList<String> consumedEntities, String currentLocation, String currentPlayer) {
        for (String consumedEntity : consumedEntities) {

            // If entity is health, decrease current player's health
            if (consumedEntity.toLowerCase().equals("health")) {
                players.get(currentPlayer).decreaseHealth();
                if (players.get(currentPlayer).getHealth() == 0) {
                    respawnPlayer(currentPlayer, currentLocation);
                }
            }
            // If entity is a location, delete path from that location
            if (locations.containsKey(consumedEntity)) {
                paths.get(currentLocation).remove(consumedEntity);
            }
            // If entity exists in location, delete from location
            if (locations.get(currentLocation).entityExists(consumedEntity)) {
                locations.get(currentLocation).deleteEntity(consumedEntity);
            }
            // If entity exists in players' inventory, delete from inventory
            if (players.get(currentPlayer).artefactExists(consumedEntity)) {
                players.get(currentPlayer).deleteArtefact(consumedEntity);
            }
        }
    }

    /* Action command - Entity "production" */
    public void moveEntitiesProduced(ArrayList<String> producedEntities, String currentLocation, String currentPlayer) {
        for (String producedEntity : producedEntities) {

            // If entity is health, increase player's health
            if (producedEntity.toLowerCase().equals("health")) {
                players.get(currentPlayer).increaseHealth();
            }
            // If entity is a location, add path to that location (if it doesn't already exist)
            if (locations.containsKey(producedEntity) && !paths.get(currentLocation).contains(producedEntity)) {
                paths.get(currentLocation).add(producedEntity);
            }
            // Otherwise, simply add entity to current location
            for (String key : locations.keySet()) {

                // If entity is an artefact, delete from its location and add as an artefact (unless it exists in current location)
                deleteAddEntity("artefacts", key, currentLocation, producedEntity);
                // If entity is furniture, delete from its location and add as furniture (unless it exists in current location)
                deleteAddEntity("furniture", key, currentLocation, producedEntity);
                // If entity is a character, delete from its location and add as character (unless it exists in current location)
                deleteAddEntity("characters", key, currentLocation, producedEntity);
            }
        }
    }

    /* Action command - Check whether all action subjects exist in current location or player's inventory */
    public boolean subjectsExist(ArrayList<String> subjects, String currentLocation, String currentPlayer) {
        for (String subject : subjects) {
            if (!locations.get(currentLocation).entityExists(subject) &&
                    !players.get(currentPlayer).artefactExists(subject)) {
                return false;
            }
        }
        return true;
    }

    /* Look command */
    public ArrayList<String> executeLook(String currentLocation, String currentPlayer) {
        Location location;
        ArrayList<String> output = new ArrayList<>();

        output.add("You are in: " + locations.get(currentLocation).getDescription());
        location = locations.get(currentLocation);

        /* Add Furniture description */
        if(location.entitiesExist("furniture")) {
            output.addAll(location.getEntityDescriptions("furniture"));
        }
        /* Add Artefacts description */
        if(location.entitiesExist("artefacts")) {
            output.addAll(location.getEntityDescriptions("artefacts"));
        }
        /* Add Characters description */
        if(location.entitiesExist("characters")) {
            output.addAll(location.getEntityDescriptions("characters"));
        }
        /* Add player names */
        for (String key : players.keySet()) {
            /* Select other players that are in current location */
            if(!key.equals(currentPlayer) && players.get(key).getCurrentLocation().equals(currentLocation)) {
                output.add(key);
            }
        }
        output.add("You can access from here:");
        /* Add paths */
        output.addAll(paths.get(currentLocation));

        return output;
    }

    // Delete entity from its location and add to current location (unless it exists in current location)
    private void deleteAddEntity(String entityType, String key, String currentLocation, String entityName) {
        if (locations.get(key).specificEntityExists(entityType, entityName)
                && !locations.get(currentLocation).specificEntityExists(entityType, entityName)) {
            String entityDescription = locations.get(key).getEntityDescription(entityType, entityName);
            deleteFromLocation(entityName, key);
            locations.get(currentLocation).addEntity(entityType, entityName, entityDescription);
        }
    }

    /* Respawning player when they die */
    private void respawnPlayer(String currentPlayer, String currentLocation) {
        /* Add all artefacts in player's inventory to current location*/
        for(int i = 0; i < players.get(currentPlayer).getInventoryId().size(); i++) {
            addArtefactToLocation(currentLocation,currentPlayer,players.get(currentPlayer).getInventoryId().get(i));
        }
        /* Delete artefacts from players' inventory */
        players.get(currentPlayer).deleteAllInventory();
        /* Return player to start location */
        players.get(currentPlayer).setCurrentLocation(getStartLocation());
        /* Set their health back to 3 */
        players.get(currentPlayer).resetHealth();
    }

    /* Get start location (first location in entities file) */
    private String getStartLocation() {
        return locations.entrySet().iterator().next().getKey();
    }

    /* Delete artefact, furniture, or character from location */
    private void deleteFromLocation(String producedEntity, String location) {
        locations.get(location).deleteEntity(producedEntity);
    }
}




