package StagServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Location {

    private String description;
/*    HashMap<String, Artefact> artefacts;
    HashMap<String, Charact> characters;
    HashMap<String, Player> players;
    HashMap<String, Furniture> furniture;

 */
    HashMap<String, HashMap<String,String>> locationContents;

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

    public ArrayList<String> getFurniture() {
        ArrayList<String> furniture = new ArrayList<>();
        for (String key: locationContents.get("furniture").keySet()) {
            furniture.add("A " + locationContents.get("furniture").get(key));
        }
       /* System.out.println("even more intersted!");
        System.out.println(locationContents);*/
        return furniture;
    }

    public ArrayList<String> getArtefacts() {
        ArrayList<String> artefacts = new ArrayList<>();
        for (String key: locationContents.get("artefacts").keySet()) {
            artefacts.add("A " + locationContents.get("artefacts").get(key));
        }
        return artefacts;
    }

    public HashMap<String, HashMap<String,String>> getLocationContents() {
        return locationContents;
    }
}
