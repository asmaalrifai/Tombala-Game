package com.mycompany.tambolagame;

public class Players {

    private String player1Name;
    private String player2Name;

    public Players(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }
}
