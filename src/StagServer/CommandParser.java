package StagServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandParser {

    private String command;
    private Integer i = 0;
    private String currentPlayer;
    private String currentLocation;

    public CommandParser(String command) {
        this.command = command;
    }

    public ArrayList<String> parse(Entities entities, Actions actions) {

        /* NEED TO DEAL WITH NULL EXCEPTIONS FOR ALL OF THESE: I.E. artefact not exisiting etc. */

        // System.out.println("Inside parser");
        String[] tokenizedCommand = tokenize();


       // entities.printPlayer("Gabriel");

        currentPlayer = tokenizedCommand[i];

        /* Add new player and set its start location */
        if(!entities.playerExists(tokenizedCommand[i])) {
            entities.addNewPlayer(tokenizedCommand[i]);
            entities.setPlayerLocation(currentPlayer);
        }

        currentLocation = entities.getPlayerLocation(currentPlayer);

        /* Skipping initial ":" */
        i+=2;

        /* Inventory command */
        if(tokenizedCommand[i].toLowerCase().equals("inventory") || tokenizedCommand[i].toLowerCase().equals("inv")) {
            /* Execute inventory command - maybe change to executeInventoryCommand*/
            /* NEED NULL POINTER EXCEPTION*/
            return entities.getPlayerInventory(currentPlayer);
        }

        /* "Look" command */
        else if(tokenizedCommand[i].toLowerCase().equals("look")) {
            return entities.executeLook(currentLocation);
        }

        /* "Get" command */
        else if(tokenizedCommand[i].toLowerCase().equals("get")) {
            i++;
            /* Add artefact to player's inventory */
            entities.addArtefactToPlayer(currentPlayer, currentLocation, tokenizedCommand[i]);
            /* Delete artefact from current location*/
            entities.deleteArtefact(currentLocation, tokenizedCommand[i]);
        }

        /* "Drop" command */
        else if(tokenizedCommand[i].toLowerCase().equals("drop")) {
            i++;
            /* Add artefact to current location   --  might be too long method name*/
            entities.addArtefactToLocation(currentLocation, currentPlayer, tokenizedCommand[i]);
            /* Delete artefact from player's inventory  --  might be too long method name */
            entities.deleteArtefactFromPlayer(currentPlayer, tokenizedCommand[i]);
        }

        /* Goto command */
        else if(tokenizedCommand[i].toLowerCase().equals("goto")) {
            i++;
            /* Update current player's location */
            entities.updatePlayerLocation(currentPlayer, tokenizedCommand[i]);
        }

        // System.out.println("ACTIONS\n\n\n");

        // ACTIONS
        /* Check that trigger word exists. If exists then
        * Check that subjects are present: location or player inv
        * Check that consumed are present: location
        * Remove "consumed" from location/inv - remove from game completely
        * Add "produced" to location - might be a whole location too?? i.e. cellar
        * */

        else {
            for(int j = 0; j < actions.getActions().size(); j++) {
                /* Check that trigger word exists in set of trigger words */
                if(actions.getActions().get(j).get("triggers").contains(tokenizedCommand[i])) {
                    i++;
                    /* Check that subjects exist in action set - to verify action in case of repeated trigger word */
                    if(actions.getActions().get(j).get("subjects").contains(tokenizedCommand[i])) {
                        /* Check that subjects are present in location or player inventory */
                        if(entities.subjectsExist(actions.getActions().get(j).get("subjects"), currentLocation, currentPlayer)) {
                            System.out.println("SUBJECTS ACTUALLY EXIST\n\n");
                            /* Removing item from current location or current player's inventory */
                            entities.removeItemConsumed(actions.getActions().get(j).get("consumed").get(0), currentLocation, currentPlayer);
                            /* Moving "produced" item from unplaced location to current location */
                            entities.addItemProduced(actions.getActions().get(j).get("produced").get(0), currentLocation);
                            return actions.getActions().get(j).get("narration");
                        }
                    }
                    i--;
                }
            }
        }

        return null;
    }

    private String[] tokenize() {
        String[] tokenizedCommand;
        tokenizedCommand = command.trim().split("\\s+|((?<=:)|(?=:))");
        return tokenizedCommand;
    }

    private HashMap<String, ArrayList<String>> newTokenize() {
        String[] tokenizedCommand;
        HashMap<String, ArrayList<String>> parsedCommand = new HashMap<>();
        tokenizedCommand = command.trim().split("\\s+|((?<=:)|(?=:))");
        ArrayList<String> command = new ArrayList<>(Arrays.asList(tokenizedCommand));
        String currentPlayer = command.get(0);
        command.remove(0);

        parsedCommand.put(currentPlayer, command);
        return parsedCommand;
    }

    /*private void splitWithDelimiter(String[] tokens) {
        for(int i = 0; i < tokens.length; i++) {
            String[] token = tokens[i].split("((?<=;)|(?=;))");
        }
    }*/
}
