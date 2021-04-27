package StagServer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ActionParser {

    private String actionFileName;
    private Actions actions;

    public ActionParser(String actionFileName) {
        this.actionFileName = actionFileName;
        actions = new Actions();
    }

    public void parse() {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(actionFileName);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            //System.out.println(jsonObject);

            JSONArray actions = (JSONArray) jsonObject.get("actions");

            for(Object action : actions) {
                HashMap<String, ArrayList<String>> parsedAction = new HashMap<>();
                JSONObject jsonAction = (JSONObject) action;

                ArrayList<String> triggers = (ArrayList<String>) jsonAction.get("triggers");
                parsedAction.put("triggers", triggers);

                ArrayList<String> subjects = (ArrayList<String>) jsonAction.get("subjects");
                parsedAction.put("subjects", subjects);

                ArrayList<String> consumed = (ArrayList<String>) jsonAction.get("consumed");
                parsedAction.put("consumed", consumed);

                ArrayList<String> produced = (ArrayList<String>) jsonAction.get("produced");
                parsedAction.put("produced", produced);

                String stringNarration = (String) jsonAction.get("narration");
                ArrayList<String> arrayNarration = new ArrayList<>();
                arrayNarration.add(stringNarration);
                parsedAction.put("narration", arrayNarration);

                this.actions.setActions(parsedAction);
               /* ArrayList<String> narration = (ArrayList<String>) jsonAction.get("narration");
                parsedAction.put("narration", narration);*/
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public void printing() {
        System.out.println("PRINTING");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(actions.getActions());
    }

}
