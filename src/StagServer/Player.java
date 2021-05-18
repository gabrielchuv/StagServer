package StagServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private HashMap<String, String> inventory;
    private String currentLocation;
    private Integer health;

    public Player() {
        inventory = new HashMap<>();
        health = 3;
    }

    /* Add artefact to inventory */
    public void addArtefact(String artefactName, String artefactDescription) {
        this.inventory.put(artefactName, artefactDescription);
    }

    /* Delete artefact from inventory */
    public void deleteArtefact(String artefactName) {
        this.inventory.remove(artefactName);
    }

    /* Given an artefact's name, return its description */
    public String getArtefactDescription(String artefactName) {
        return this.inventory.get(artefactName);
    }

    /* Check whether an artefact exists */
    public boolean artefactExists(String artefact) {
        if(!inventory.containsKey(artefact)) {
            return false;
        }
        return true;
    }

    /* Change player's location */
    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    /* Get player's location */
    public String getCurrentLocation() {
        return this.currentLocation;
    }

    /* Get all artefact descriptions in inventory */
    public ArrayList<String> getInventoryDescriptions() {
        ArrayList<String> inventory = new ArrayList<>();
        for (String key: this.inventory.keySet()) {
            inventory.add(this.inventory.get(key));
        }
        return inventory;
    }

    /* Get all artefact names in inventory */
    public ArrayList<String> getInventoryId() {
        ArrayList<String> inventory = new ArrayList<>();
        for (String key: this.inventory.keySet()) {
            inventory.add(key);
        }
        return inventory;
    }

    /* Delete entire inventory (for respawning player) */
    public void deleteAllInventory() {
        inventory.clear();
    }

    /* Reset health units (for respawning player) */
    public void resetHealth() {
        this.health = 3;
    }

    /* Decrease health */
    public void decreaseHealth() {
        this.health--;
    }

    /* Increase health */
    public void increaseHealth() {
        this.health++;
    }

    /* Get health */
    public Integer getHealth() {
        return this.health;
    }

}
