package StagServer;

import java.util.ArrayList;
import java.util.Arrays;

public class ParserInterpreter {

    private String command;
    private Integer i = 0;
    private String currentPlayer;
    private String currentLocation;
    private String[] builtinCommands = new String[] {"inventory", "inv", "get", "drop", "goto", "look", "health"};
    private Entities entities = new Entities();
    private Actions actions = new Actions();
    private ArrayList<String> tokenizedCommand = new ArrayList<>();

    public ParserInterpreter(String command) {
        this.command = command;
    }

    public ArrayList<String> parse(Entities entities, Actions actions) {

        /* Populating entities and actions in current class */
        this.entities = entities;
        this.actions = actions;

        /* Get tokenized user command (discarding player name) */
        this.tokenizedCommand = getUserCommand();
        /* Set player name in current class */
        setCurrentPlayer();

        /* Add new player and set its start location */
        if(!entities.playerExists(currentPlayer)) {
            entities.addNewPlayer(currentPlayer);
            entities.setPlayerLocation(currentPlayer);
        }

        /* Set player's location */
        currentLocation = entities.getPlayerLocation(currentPlayer);

        /* Checking for incorrect user input */
        if(checkIncorrectInput()!= null) {
            return checkIncorrectInput();
        }

        /* Builtin commands - Inventory, Look, Get, Drop, Goto, Health */

        /* "Inventory" command */
        if(tokenizedCommand.contains("inventory") || tokenizedCommand.contains("inv")) {
            return entities.getPlayerInventory(currentPlayer);
        }

        /* "Look" command */
        else if(tokenizedCommand.contains("look")) {
            return entities.executeLook(currentLocation, currentPlayer);
        }

        /* "Get" command */
        else if(tokenizedCommand.contains("get")) {
            return getCommand();
        }

        /* "Drop" command */
        else if(tokenizedCommand.contains("drop")) {
            return dropCommand();
        }

        else if(tokenizedCommand.contains("goto")) {
            return gotoCommand();
        }

        /* "Health" command */
        else if(tokenizedCommand.contains("health")) {
            return entities.getHealth(currentPlayer);
        }

        /* Action commands */
        else {
            return actionCommand();
        }
    }

    /* "Get" command */
    private ArrayList<String> getCommand() {
        ArrayList<String> userMessage = new ArrayList<>();

        /* Find if token within user command matches an artefact in current location */
        String artefact = entities.matchLocationArtefact(tokenizedCommand, currentLocation);
        /* If there's a match */
        if(artefact != null) {
            /* Add artefact to player's inventory */
            entities.addArtefactToPlayer(currentPlayer, currentLocation, artefact);
            /* Delete artefact from current location*/
            entities.deleteArtefact(currentLocation, artefact);
            userMessage.add("You picked up a " + artefact);
            return userMessage;
        }
        /* If there's no match */
        userMessage.add("You can't pick that up!");
        return userMessage;
    }

    /* "Drop" command */
    private ArrayList<String> dropCommand() {
        ArrayList<String> userMessage = new ArrayList<>();
        /* Find if token within user command matches an artefact in player's inventory */
        String artefact = entities.matchPlayerArtefact(tokenizedCommand, currentPlayer);
        /* If there's a match */
        if(artefact != null) {
            /* Add artefact to current location */
            entities.addArtefactToLocation(currentLocation, currentPlayer, artefact);
            /* Delete artefact from player's inventory */
            entities.deleteArtefactFromPlayer(currentPlayer, artefact);
            userMessage.add("You dropped your " + artefact);
            return userMessage;
        }
        /* If there's no match */
        userMessage.add("You can't drop what you don't have");
        return userMessage;
    }

    /* "Goto" command */
    private ArrayList<String> gotoCommand() {
        ArrayList<String> userMessage = new ArrayList<>();
        /* Find if token within user command matches a location in game world */
        String newLocation = entities.matchLocation(tokenizedCommand, currentLocation);
        /* If there's a match */
        if(newLocation != null) {
            /* Update current player's location */
            entities.updatePlayerLocation(currentPlayer, newLocation);
            /* Automatic "Look" to display information about new location */
            return entities.executeLook(newLocation, currentPlayer);
        }
        /* If there's no match */
        userMessage.add("You can't go there!");
        return userMessage;
    }

    /* "Action" command */
    private ArrayList<String> actionCommand() {
        /* Error message if there is no entity specification */
        if(tokenizedCommand.size() == 1) {
            ArrayList<String> errorMessage = new ArrayList<>();
            errorMessage.add("Invalid command: need more information");
            return errorMessage;
        }

        /* Iterating through actions */
        for(int i = 0; i < actions.getActions().size(); i++) {
            /* Check that trigger is valid (exists for this action) */
            if(containMatchingElement(actions.getTriggers(i), tokenizedCommand)) {
                /* Check that subjects are valid for this action - in case of repeated trigger word */
                if(containMatchingElement(actions.getSubjects(i), tokenizedCommand)) {
                    /* Check that subjects are present in location or player inventory */
                    if(entities.subjectsExist(actions.getSubjects(i), currentLocation, currentPlayer)) {
                        /* Removing "consumed" entity from current location or current player's inventory */
                        if(actions.getConsumed(i).size() > 0) {
                            entities.deleteEntitiesConsumed(actions.getConsumed(i), currentLocation, currentPlayer);
                        }
                        /* Moving "produced" entity from their current location to the player's location */
                        if(actions.getProduced(i).size() > 0) {
                            entities.moveEntitiesProduced(actions.getProduced(i), currentLocation, currentPlayer);
                        }
                        return actions.getNarration(i);
                    }
                }
            }
        }
        return null;
    }

    /* Check for incorrect user input */
    private ArrayList<String> checkIncorrectInput() {
        ArrayList<String> errorMessage = new ArrayList<>();
        /* Multiple builtin commands */
        if(multipleBuiltinExist(tokenizedCommand)) {
            errorMessage.add("Invalid command: multiple builtin commands");
            return errorMessage;
        }
        /* No artefact or location specification */
        if(tokenizedCommand.size() == 1) {
            if(tokenizedCommand.get(0).equals("get") || tokenizedCommand.get(0).equals("goto")
                    || tokenizedCommand.get(0).equals("drop")) {
                errorMessage.add("Invalid command: need more information");
                return errorMessage;
            }
        }
        return null;
    }

    /* Checks whether user command contains an existing actionElement (trigger or subject) */
    private boolean containMatchingElement(ArrayList<String> actionElement, ArrayList<String> tokenizedCommand) {
        for (String s : actionElement) {
            if (tokenizedCommand.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /* Extracts player name from user command and sets it in current class */
    private void setCurrentPlayer() {
        String[] tokenizedCommand;
        tokenizedCommand = tokenize(command);
        this.currentPlayer = tokenizedCommand[0];
    }

    /* Extracts "pure" user command discarding player name */
    private ArrayList<String> getUserCommand() {
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

    /* Tokenizes user input by separating on ":" and " " */
    private String[] tokenize(String userInput) {
        String[] tokenizedInput = userInput.trim().split("\\s+|((?<=:)|(?=:))");
        return tokenizedInput;
    }

    /* Making builtin commands case insensitive */
    private void makeCaseInsensitive(ArrayList<String> command) {
        for(int i = 0; i < command.size(); i++) {
            for (String builtinCommand : builtinCommands) {
                if (builtinExists(command.get(i), builtinCommand)) {
                    command.set(i, builtinCommand);
                }
            }
        }
    }

    /* Checks whether a token is a builtin command */
    private boolean builtinExists(String token, String builtin) {
        return token.toLowerCase().equals(builtin);
    }

    /* Checks whether multiple builtin commands exists */
    private boolean multipleBuiltinExist(ArrayList<String> command) {
        int counter = 0;
        for (String s : command) {
            for (String builtinCommand : builtinCommands) {
                if (builtinExists(s, builtinCommand)) {
                    counter++;
                    if (counter == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
