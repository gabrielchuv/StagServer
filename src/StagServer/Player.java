package StagServer;

import java.util.ArrayList;

public class Player {

    private ArrayList<String> inventory;
    private String currentLocation;

    public Player() {

    }

    public void addInventoryItem(String item) {
        this.inventory.add(item);
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}
