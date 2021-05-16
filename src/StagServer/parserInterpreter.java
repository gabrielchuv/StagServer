package StagServer;

import javax.annotation.processing.SupportedSourceVersion;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class parserInterpreter {

    private String command;
    private Integer i = 0;
    private String currentPlayer;
    private String currentLocation;
    private String[] builtinCommands = new String[] {"inventory", "inv", "get", "drop", "goto", "look"};

    public parserInterpreter(String command) {
        this.command = command;
    }

    public ArrayList<String> parse(Entities entities, Actions actions) {

        /* NEED TO DEAL WITH NULL EXCEPTIONS FOR ALL OF THESE: I.E. artefact not exisiting etc. */

     //   String[] tokenizedCommand = tok();
        // NEW APPROACH
      //  HashMap<String, ArrayList<String>> newTokenizedCommand = newTokenize();
        // EVEN NEWER APPROACH
        ArrayList<String> newTokenizedCommand = getCommand();

        // Populating current player
        //currentPlayer = tokenizedCommand[i];
       // currentPlayer = newTokenizedCommand.keySet().iterator().next();
        setCurrentPlayer();
        //System.out.println("player supposed to be: " + newTokenizedCommand.keySet().iterator().next());

        /* Add new player and set its start location */
        if(!entities.playerExists(currentPlayer)) {
            entities.addNewPlayer(currentPlayer);
            entities.setPlayerLocation(currentPlayer);
        }

        currentLocation = entities.getPlayerLocation(currentPlayer);

        /* Skipping initial ":" */
       // i+=2;

        /* Inventory command */
       // if(tokenizedCommand[i].toLowerCase().equals("inventory") || tokenizedCommand[i].toLowerCase().equals("inv")) {
            /* Execute inventory command - maybe change to executeInventoryCommand*/
            /* NEED NULL POINTER EXCEPTION*/
          //  return entities.getPlayerInventory(currentPlayer);
      //  }
        /* NEW APPROACH - Inventory command */
      /*  if(newTokenizedCommand.get(currentPlayer).contains("inventory") || newTokenizedCommand.get(currentPlayer).contains("inv")) {
            return entities.getPlayerInventory(currentPlayer);
        }*/

        /* CHECKING FOR INCORRECT USER INPUT */
        if(multipleBuiltinExist(newTokenizedCommand)) {
            ArrayList<String> errorMessage = new ArrayList<>();
            errorMessage.add("Invalid command: multiple builtin commands");
            return errorMessage;
        }


        /* EVEN NEWER APPROACH - Inventory command */
        if(newTokenizedCommand.contains("inventory") || newTokenizedCommand.contains("inv")) {
            return entities.getPlayerInventory(currentPlayer);
        }

        /* "Look" command */
        /*else if(tokenizedCommand[i].toLowerCase().equals("look")) {
            return entities.executeLook(currentLocation);
        }*/

        /* NEW APPROACH - "Look" command */
        else if(newTokenizedCommand.contains("look")) {
            return entities.executeLook(currentLocation);
        }

        /* "Get" command */
      /*  else if(tokenizedCommand[i].toLowerCase().equals("get")) {
            i++;
            /* Add artefact to player's inventory */
       /*     entities.addArtefactToPlayer(currentPlayer, currentLocation, tokenizedCommand[i]);
            /* Delete artefact from current location*/
     /*       entities.deleteArtefact(currentLocation, tokenizedCommand[i]);
        }*/

        /* NEW APPROACH - "Get" command */
        else if(newTokenizedCommand.contains("get")) {
         //   i++;
            /* Find if keyword within user command matches an artefact in location */
            String artefact = entities.matchLocationArtefact(newTokenizedCommand, currentLocation);
            if(artefact != null) {
                /* Add artefact to player's inventory */
                entities.addArtefactToPlayer(currentPlayer, currentLocation, artefact);
                /* Delete artefact from current location*/
                entities.deleteArtefact(currentLocation, artefact);
            }

        }

        /* "Drop" command */
        /*else if(tokenizedCommand[i].toLowerCase().equals("drop")) {
            i++;
            /* Add artefact to current location   --  might be too long method name*/
       /*     entities.addArtefactToLocation(currentLocation, currentPlayer, tokenizedCommand[i]);
            /* Delete artefact from player's inventory  --  might be too long method name */
        /*    entities.deleteArtefactFromPlayer(currentPlayer, tokenizedCommand[i]);
        }*/

        /* NEW APPROACH - "Drop" command */
        else if(newTokenizedCommand.contains("drop")) {
           // i++;
            /* Find if keyword within user command matches an artefact in player's inventory */
            String artefact = entities.matchPlayerArtefact(newTokenizedCommand, currentPlayer);
            if(artefact != null) {
                /* Add artefact to current location   --  might be too long method name*/
                entities.addArtefactToLocation(currentLocation, currentPlayer, artefact);
                /* Delete artefact from player's inventory  --  might be too long method name */
                entities.deleteArtefactFromPlayer(currentPlayer, artefact);
            }
        }



        /* Goto command */
       /* else if(tokenizedCommand[i].toLowerCase().equals("goto")) {
            i++;
            /* Update current player's location */
       /*     entities.updatePlayerLocation(currentPlayer, tokenizedCommand[i]);
        }*/

        else if(newTokenizedCommand.contains("goto")) {
         //   i++;
            /* Find if keyword within user command matches an location in game world */
            String location = entities.matchLocation(newTokenizedCommand, currentLocation);
            if(location != null) {
                /* Update current player's location */
                entities.updatePlayerLocation(currentPlayer, location);
            }
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
                if(containMatchingElement(actions.getActions().get(j).get("triggers"), newTokenizedCommand)) {
                //if(actions.getActions().get(j).get("triggers").contains(tokenizedCommand[i])) {
                 //   i++;
                    /* Check that subject exist in action set - to verify action in case of repeated trigger word */
                    if(!containMatchingElement(actions.getActions().get(j).get("subjects"), newTokenizedCommand)) {
                        ArrayList<String> errorMessage = new ArrayList<>();
                        errorMessage.add("Ambiguous command!");
                        return errorMessage;
                    }
                    else {//(containMatchingElement(actions.getActions().get(j).get("subjects"), newTokenizedCommand)) {
                   // if(actions.getActions().get(j).get("subjects").contains(tokenizedCommand[i])) {
                        /* Check that subjects are present in location or player inventory */
                        System.out.println("INSIDE ELSE:");
                        if(entities.subjectsExist(actions.getActions().get(j).get("subjects"), currentLocation, currentPlayer)) {
                            System.out.println("SUBJECTS ACTUALLY EXIST\n\n");
                            /* Removing item from current location or current player's inventory */
                            if(actions.getActions().get(j).get("consumed").size() > 0) {
                                entities.removeItemConsumed(actions.getActions().get(j).get("consumed").get(0), currentLocation, currentPlayer);
                            }
                            /* Adding "produced" item  to current location */
                            if(actions.getActions().get(j).get("produced").size() > 0) {
                                System.out.println("inside interpreter produced");
                                entities.addItemProduced(actions.getActions().get(j).get("produced").get(0), currentLocation);
                            }
                            /* Removing "produced" item from its current location */
                           // entities.removeFromLocation(actions.getActions().get(j).get("produced").get(0));
                            return actions.getActions().get(j).get("narration");
                        }
                    }
                   // i--;
                }
            }
        }
        return null;
    }

    private boolean containMatchingElement(ArrayList<String> actionElement, ArrayList<String> tokenizedCommand) {
        System.out.println("COMPARING MATCHING ELEMENTS");
        System.out.println("actionElement: " + actionElement);
        System.out.println("command: " + tokenizedCommand);
        for(int i = 0; i < actionElement.size(); i++) {
            if(tokenizedCommand.contains(actionElement.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void setCurrentPlayer() {
        String[] tokenizedCommand;
        tokenizedCommand = tokenize(command);
        this.currentPlayer = tokenizedCommand[0];
    }

    private String[] tokenize(String userInput) {
        String[] tokenizedInput = userInput.trim().split("\\s+|((?<=:)|(?=:))");
        return tokenizedInput;
    }

    private ArrayList<String> getCommand() {
        String[] tokenizedCommand;
        tokenizedCommand = tokenize(command);
        ArrayList<String> processedCommand = new ArrayList<String>(Arrays.asList(tokenizedCommand));
        /* Remove player */
        processedCommand.remove(0);
        /* Remove ":" */
        processedCommand.remove(0);
        /* If dealing with a built-in command, make it case insensitive */
        makeCaseInsensitive(processedCommand);
        return processedCommand;
    }

    /* Making builtin commands case insensitive */
    private void makeCaseInsensitive(ArrayList<String> command) {
        System.out.println("makeCaseInsensitive");
        for(int i = 0; i < command.size(); i++) {
            System.out.println("1");
            for(int j = 0; j < builtinCommands.length; j++) {
                System.out.println("2");
                if(builtinExists(command.get(i), builtinCommands[j])) {
                    System.out.println("command: " + command.get(i));
                    System.out.println("builtin: " + builtinCommands[j]);
                    command.set(i, builtinCommands[j]);
                }
            }
        }
    }


    private boolean builtinExists(String keyword, String builtin) {
        System.out.println("builtinExists: kw- " + keyword + " built- " + builtin);
        if(keyword.toLowerCase().equals(builtin)) {
            //System.out.println("TRUE");
            return true;
        }
        return false;
    }

    private boolean multipleBuiltinExist(ArrayList<String> command) {
        Integer counter = 0;
        for(int i = 0; i < command.size(); i++) {
            for(int j = 0; j < builtinCommands.length; j++) {
                if(builtinExists(command.get(i), builtinCommands[j])) {
                    counter++;
                    if(counter == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* TO DELETE */
    private String[] tok() {
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
}
