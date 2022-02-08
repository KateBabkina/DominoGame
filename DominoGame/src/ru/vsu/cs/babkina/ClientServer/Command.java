package ru.vsu.cs.babkina.ClientServer;

public enum Command {
    MOVE("MOVE"),
    RESP("RESP"),
    END("END");

    public static final String SEPARATOR = ":";

    private final String commandString;

    Command(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }
}