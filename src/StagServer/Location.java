package StagServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Location {

    private String description;
    private HashMap<String, HashMap<String,String>> locationContents;

    public Location() {
        locationContents = new HashMap<>();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void deleteArtefact(String artefactName) {
        locationContents.get("artefacts").remove(artefactName);
    }

    public void setLocationContents(HashMap<String, HashMap<String,String>> locationContents) {
        this.locationContents = locationContents;
    }

    public String getDescription() {
        return description;
    }

    public String getArtefactDescription(String artefactName) {
        return locationContents.get("artefacts").get(artefactName);
    }

    public String getFurnitureDescription(String furnitureName) {
        return locationContents.get("furniture").get(furnitureName);
    }

    public String getCharacterDescription(String characterName) {
        return locationContents.get("characters").get(characterName);
    }

    public ArrayList<String> getFurniture() {
        ArrayList<String> furniture = new ArrayList<>();
        for (String key: locationContents.get("furniture").keySet()) {
            furniture.add(locationContents.get("furniture").get(key));
        }
        return furniture;
    }


    public ArrayList<String> getCharacters() {
        ArrayList<String> furniture = new ArrayList<>();
        for (String key: locationContents.get("characters").keySet()) {
            furniture.add(locationContents.get("characters").get(key));
        }
        return furniture;
    }

    public ArrayList<String> getArtefacts() {
        ArrayList<String> artefacts = new ArrayList<>();
        for (String key: locationContents.get("artefacts").keySet()) {
            artefacts.add(locationContents.get("artefacts").get(key));
        }
        return artefacts;
    }

    public boolean artefactExists(String artefact) {
        if(artefactsExist() && locationContents.get("artefacts").containsKey(artefact)) {
            return true;
        }
        return false;
    }

    public boolean furnitureExists(String furniture) {
        if(artefactsExist() && locationContents.get("furniture").containsKey(furniture)) {
            return true;
        }
        return false;
    }

    public boolean characterExists(String character) {
        if(artefactsExist() && locationContents.get("characters").containsKey(character)) {
            return true;
        }
        return false;
    }

    public boolean itemExists(String item) {
        for(int i = 0; i < locationContents.size(); i++) {
            if(artefactsExist() && locationContents.get("artefacts").containsKey(item)) {
                return true;
            }
            if(furnitureExist() && locationContents.get("furniture").containsKey(item)) {
                return true;
            }
            if(charactersExist() && locationContents.get("characters").containsKey(item)) {
                return true;
            }
        }
        return false;
    }

    public void deleteItem(String item) {
        /* NEED TO REFACTOR THIS CODE TOGETHER WITH "ITEM EXISTS" METHOD  */
        for(int i = 0; i < locationContents.size(); i++) {
            // for(int j = 0; j < items.size(); j++) {
            if(artefactsExist()) {
                locationContents.get("artefacts").remove(item);
            }
            if(furnitureExist()) {
                locationContents.get("furniture").remove(item);
            }
            if(charactersExist()) {
                locationContents.get("characters").remove(item);
            }
        }
    }

    public void addArtefact(String artefactName, String artefactDescription) {
        HashMap<String, String> artefact = new HashMap<>();
        artefact.put(artefactName, artefactDescription);
        if(!artefactsExist()) {
            locationContents.put("artefacts", artefact);
        }
        locationContents.get("artefacts").put(artefactName, artefactDescription);
    }

    public void addFurniture(String furnitureName, String furnitureDescription) {
        HashMap<String, String> furniture = new HashMap<>();
        furniture.put(furnitureName, furnitureDescription);
        if(!artefactsExist()) {
            locationContents.put("artefacts", furniture);
        }
        locationContents.get("artefacts").put(furnitureName, furnitureDescription);
    }

    public void addCharacter(String characterName, String characterDescription) {
        HashMap<String, String> character = new HashMap<>();
        character.put(characterName, characterDescription);
        if(!artefactsExist()) {
            locationContents.put("artefacts", character);
        }
        locationContents.get("artefacts").put(characterName, characterDescription);
    }

    public boolean artefactsExist() {
        return locationContents.containsKey("artefacts");
    }

    public boolean charactersExist() {
        return locationContents.containsKey("characters");
    }

    public boolean furnitureExist() {
        return locationContents.containsKey("furniture");
    }

    public HashMap<String, HashMap<String,String>> getLocationContents() {
        return locationContents;
    }
}
