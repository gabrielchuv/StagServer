package StagServer;

import java.util.HashMap;

public class Players {

    private HashMap<String, Player> players;

    public Players() {

    }

    public void addPlayer(String playerName, Player player) {
        this.players.put(playerName, player);
    }
}
