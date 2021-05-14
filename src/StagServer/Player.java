package StagServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private HashMap<String, String> inventory;
    private ArrayList<String> newInventory;
    private ArrayList<String> currentLocation;
    private String newCurrentLocation;
    //private String playerName;

    public Player() {
        inventory = new HashMap<>();
        newInventory = new ArrayList<>();
    }

    public void setStartLocation(ArrayList<String> startLocation) {
        this.currentLocation = startLocation;
        /* need to add newcurrentlocation here?? */
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

    public void addArtefactToNewInventory(String artefactName) {
        this.newInventory.add(artefactName);
    }

    public boolean itemExists(String item) {
        if(!inventory.containsKey(item)) {
            return false;
        }
        return true;
    }

    /* TO DELETE */
   /* public void setCurrentLocation(ArrayList<String> currentLocation) {
        this.currentLocation = currentLocation;
    }*/

    /* TO DELETE */
    /*public ArrayList<String> getCurrentLocation() {
        return this.currentLocation;
    }*/

    public void setNewCurrentLocation(String newCurrentLocation) {
        this.newCurrentLocation = newCurrentLocation;
        System.out.printf("New location: %s\n\n\n\n", newCurrentLocation);
    }

    public String getNewCurrentLocation() {
        return this.newCurrentLocation;
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

    public ArrayList<String> getNewInventory() {
        return newInventory;
    }
    /*public ArrayList<String> getInventory() {
        return inventory;
    }*/


    /*public String printInventory() {
        System.out.println(inventory);
        return null;
    }*/
}
