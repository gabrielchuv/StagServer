package StagServer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ActionParser {

    private String actionFileName;
    private Actions actions;

    public ActionParser(String actionFileName) {
        this.actionFileName = actionFileName;
        actions = new Actions();
    }

    public Actions parse() {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(actionFileName);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray actions = (JSONArray) jsonObject.get("actions");

            /* Parsing all actions in file */
            for(Object action : actions) {
                HashMap<String, ArrayList<String>> parsedAction = new HashMap<>();
                JSONObject jsonAction = (JSONObject) action;

                /* Parsing triggers */
                ArrayList<String> triggers = (ArrayList<String>) jsonAction.get("triggers");
                parsedAction.put("triggers", triggers);

                /* Parsing subjects */
                ArrayList<String> subjects = (ArrayList<String>) jsonAction.get("subjects");
                parsedAction.put("subjects", subjects);

                /* Parsing consumed */
                ArrayList<String> consumed = (ArrayList<String>) jsonAction.get("consumed");
                parsedAction.put("consumed", consumed);

                /* Parsing produced */
                ArrayList<String> produced = (ArrayList<String>) jsonAction.get("produced");
                parsedAction.put("produced", produced);

                /* Parsing narration */
                String stringNarration = (String) jsonAction.get("narration");
                ArrayList<String> arrayNarration = new ArrayList<>();
                arrayNarration.add(stringNarration);
                parsedAction.put("narration", arrayNarration);

                /* Populating data structure in Action class */
                this.actions.setAction(parsedAction);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return(actions);
    }
}
