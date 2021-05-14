package StagServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Actions {

    private ArrayList<HashMap<String, ArrayList<String>>> actions;

    public Actions() {
        actions = new ArrayList<>();
    }

    public void setActions(HashMap<String, ArrayList<String>> action) {
        actions.add(action);
    }



    /* FOR TESTING */
    public ArrayList<HashMap<String, ArrayList<String>>> getActions() {
        return actions;
    }
}
