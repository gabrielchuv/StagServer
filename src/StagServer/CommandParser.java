package StagServer;

import java.util.ArrayList;

public class CommandParser {

    private String command;

    public CommandParser(String command) {
        this.command = command;
    }

    public void parse() {

        System.out.println("Inside parser");
        String[] tokenizedCommand = tokenize();

        for(int i = 0; i < tokenizedCommand.length; i++) {
            System.out.println(tokenizedCommand[i]);
        }

    }

    private String[] tokenize() {
        String[] tokenizedCommand;
        tokenizedCommand = command.trim().split(" +");
        return tokenizedCommand;
    }
}
