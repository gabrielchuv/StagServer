package StagServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class StagServer {

    private Entities entities;

    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {
            EntityParser entitiesParser = new EntityParser(entityFilename);
            entities = entitiesParser.parse();
            /* for testing */
            entitiesParser.printing();

            ActionParser actionParser = new ActionParser(actionFilename);
            actionParser.parse();
            /* for testing */
            //actionParser.printing();

            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        String line = in.readLine();
        out.write("You said... " + line + "\n");
        CommandParser commandParser = new CommandParser(line);
        ArrayList<String> output = new ArrayList<>();
        output = commandParser.parse(entities);
        if(output != null) {
            for(int i = 0; i < output.size(); i++) {
                out.write(output.get(i) + "\n");
                //System.out.println();
            }
        }

    }
}
