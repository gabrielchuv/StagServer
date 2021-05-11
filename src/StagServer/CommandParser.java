package StagServer;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandParser {

    private String command;
    private Integer i = 0;
    private String currentPlayer;
    private ArrayList<String> currentLocation;
    private String newCurrentLocation;

    public CommandParser(String command) {
        this.command = command;
    }

    public ArrayList<String> parse(Entities entities) {

        /* NEED TO DEAL WITH NULL EXCEPTIONS FOR ALL OF THESE: I.E. artefact not exisiting etc. */


        System.out.println("Inside parser");
        String[] tokenizedCommand = tokenize();

        System.out.println("NO PLAYERS:");
       // entities.printPlayer("Gabriel");
        /* Parse player  */
        currentPlayer = tokenizedCommand[i];
        //currentLocation = entities.getStartLocation();
        /* I will use this one below */

        if(!entities.playerExists(tokenizedCommand[i])) {
            /* add player */
            entities.addNewPlayer(tokenizedCommand[i]);
            /* TO DELETE */
            //entities.setPlayerLocation(currentPlayer);
            entities.setPlayerNewLocation(currentPlayer);
        }
        /* TO DELETE */
       // currentLocation = entities.getPlayerLocation(currentPlayer);
        newCurrentLocation = entities.getPlayerNewLocation(currentPlayer);

        System.out.println("YES PLAYERS:");
        //System.out.println(currentLocation);
        //entities.printPlayerInventory("Gabriel");
        i+=2;

        /* Inventory command */
        if(tokenizedCommand[i].toLowerCase().equals("inventory") || tokenizedCommand[i].toLowerCase().equals("inv")) {
            /* Execute inventory - print it to client terminal*/

            /* NEED NULL POINTER EXCEPTION*/
            return entities.getPlayerInventory(currentPlayer);
            //return entities.getPlayerNewInventory(currentPlayer);

        }
        /* Look command */
        else if(tokenizedCommand[i].toLowerCase().equals("look")) {
            /* Execute look */
            //System.out.println(currentLocation);
            System.out.println("CP: PROCESSED ENTITIES");
           // System.out.println(entities.lookCommand(currentLocation));
            return entities.lookCommand(currentLocation, newCurrentLocation);

        }
        /* Get command */
        else if(tokenizedCommand[i].toLowerCase().equals("get")) {
            i++;
            /* Execute get */
            /* Add to player's inventory - from curLocaiton and artefacts*/

            entities.addArtefactToPlayer(currentPlayer, newCurrentLocation, tokenizedCommand[i]);

           /* ArrayList<String> artefact = new ArrayList<>();
            artefact.add(tokenizedCommand[i]);

            artefact.add(entities.getArtefact(currentLocation, tokenizedCommand[i]));
            entities.addArtefactToPlayer(currentPlayer, artefact);*/
            /* Delete from location*/
            //entities.deleteArtefact(currentLocation, tokenizedCommand[i]);
            entities.newDeleteArtefact(newCurrentLocation, tokenizedCommand[i]);


        }
        else if(tokenizedCommand[i].toLowerCase().equals("drop")) {
            /* Execute drop */
            /* Add artefact to location */

            /* Delete from player's inventory */
        }
        else if(tokenizedCommand[i].toLowerCase().equals("goto")) {
            /* Execute goto */
            i++;
            entities.updatePlayerLocation(currentPlayer, tokenizedCommand[i]);

        }


        for(int i = 0; i < tokenizedCommand.length; i++) {
            System.out.println(tokenizedCommand[i]);
        }


        return null;

    }

    private String[] tokenize() {
        String[] tokenizedCommand;
        tokenizedCommand = command.trim().split("\\s+|((?<=:)|(?=:))");
        return tokenizedCommand;
    }

    private void splitWithDelimiter(String[] tokens) {
        for(int i = 0; i < tokens.length; i++) {
            String[] token = tokens[i].split("((?<=;)|(?=;))");
        }
    }
}
