package StagServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private HashMap<String, String> inventory;
   // private ArrayList<String> currentLocation;
    private String currentLocation;

    public Player() {
        inventory = new HashMap<>();
    }

    public void addArtefactToInventory(String artefactName, String artefactDescription) {
        this.inventory.put(artefactName, artefactDescription);
    }

    public void deleteArtefact(String artefactName) {
        this.inventory.remove(artefactName);
    }

    public String getArtefactDescription(String artefactName) {
        return this.inventory.get(artefactName);
    }

    public boolean itemExists(String item) {
        if(!inventory.containsKey(item)) {
            return false;
        }
        return true;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
        System.out.printf("New location: %s\n\n\n\n", currentLocation);
    }

    public String getCurrentLocation() {
        return this.currentLocation;
    }

    public ArrayList<String> getInventory() {
        System.out.println("getting inv inside player");
        ArrayList<String> inventory = new ArrayList<>();
        for (String key: this.inventory.keySet()) {
            inventory.add(this.inventory.get(key));
        }
        System.out.println("actual inv: " + inventory);
        return inventory;
    }

}
