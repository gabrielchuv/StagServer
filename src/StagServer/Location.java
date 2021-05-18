package StagServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Location {

    private String description;
    private HashMap<String, HashMap<String,String>> locationContents;

    public Location() {
        locationContents = new HashMap<>();
    }

    /* Set description extracted from entities.dot file */
    public void setDescription(String description) {
        this.description = description;
    }

    /* Set all contents of location into data structure (from entities.dot file) */
    public void setLocationContents(HashMap<String, HashMap<String,String>> locationContents) {
        this.locationContents = locationContents;
    }

    /* Get location's description */
    public String getDescription() {
        return description;
    }

    /* Get description for a single entity in location */
    public String getEntityDescription(String entityType, String entityName) {
        return locationContents.get(entityType).get(entityName);
    }

    /* Get descriptions for all entities in location */
    public ArrayList<String> getEntityDescriptions(String entityType) {
        ArrayList<String> artefacts = new ArrayList<>();
        for (String key: locationContents.get(entityType).keySet()) {
            artefacts.add(locationContents.get(entityType).get(key));
        }
        return artefacts;
    }

    /* Check whether a given entity exists in this location in general (artefact, furniture, or character) */
    public boolean entityExists(String entity) {
        return specificEntityExists("artefacts", entity) || specificEntityExists("furniture", entity)
        || specificEntityExists("characters", entity);
    }

    /* Check whether a given entity exists in this location as a specific entity type (artefact, furniture, character) */
    public boolean specificEntityExists(String entityType, String entity) {
        if(entitiesExist(entityType) && locationContents.get(entityType).containsKey(entity)) {
            return true;
        }
        return false;
    }

    /* Delete a given entity from location */
    public void deleteEntity(String entity) {
            if(entitiesExist("artefacts")) {
                locationContents.get("artefacts").remove(entity);
            }
            if(entitiesExist("furniture")) {
                locationContents.get("furniture").remove(entity);
            }
            if(entitiesExist("characters")) {
                locationContents.get("characters").remove(entity);
            }
    }

    /* Add a given entity (name + description) to location */
    public void addEntity(String entityType, String entityName, String entityDescription) {
        HashMap<String, String> entity = new HashMap<>();
        entity.put(entityName, entityDescription);
        /* If entity type doesn't exist yet */
        if(!entitiesExist(entityType)) {
            locationContents.put(entityType, entity);
        }
        /* If entity type already exists */
        locationContents.get(entityType).put(entityName, entityDescription);
    }

    /* Checks whether a specific entity type exists (artefact, character, furniture) */
    public boolean entitiesExist(String entityType) {
        return locationContents.containsKey(entityType);
    }
}
