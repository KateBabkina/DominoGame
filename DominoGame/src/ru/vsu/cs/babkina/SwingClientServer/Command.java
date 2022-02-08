package ru.vsu.cs.babkina.SwingClientServer;

import java.io.Serializable;

public enum Command implements Serializable {
    GAME("GAME"),
    MOVE("MOVE"),
    EXTRA_TILE("EXTRA TILE"),
    POSITION_CHOICE("POSITION_CHOICE"),
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    END("END");

    private final String commandString;

    Command(String commandString) {
        this.commandString = commandString;
    }

}