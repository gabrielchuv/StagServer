package StagServer;

import java.util.ArrayList;
import java.util.HashMap;

public class Actions {

    private ArrayList<HashMap<String, ArrayList<String>>> actions;

    public Actions() {
        actions = new ArrayList<>();
    }

    /* Add an action (from action.json file) */
    public void setAction(HashMap<String, ArrayList<String>> action) {
        actions.add(action);
    }

    /* Get all actions */
    public ArrayList<HashMap<String, ArrayList<String>>> getActions() {
        return actions;
    }

    /* Get "triggers" for a given action */
    public ArrayList<String> getTriggers(Integer position) {
        return actions.get(position).get("triggers");
    }

    /* Get "subjects" for a given action */
    public ArrayList<String> getSubjects(Integer position) {
        return actions.get(position).get("subjects");
    }

    /* Get "consumed" for a given action */
    public ArrayList<String> getConsumed(Integer position) {
        return actions.get(position).get("consumed");
    }

    /* Get "triggers" for a given action */
    public ArrayList<String> getProduced(Integer position) {
        return actions.get(position).get("produced");
    }

    /* Get "narration" for a given action */
    public ArrayList<String> getNarration(Integer position) {
        return actions.get(position).get("narration");
    }
}
