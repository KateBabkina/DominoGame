package ru.vsu.cs.babkina.SwingClientServer;

import ru.vsu.cs.babkina.GameModel.DominoTile;

import java.io.Serializable;

public class Request implements Serializable {
    private final Command command;
    private final DominoTile dominoTile;

    public Command getCommand() {
        return command;
    }

    public DominoTile getDominoTile() {
        return dominoTile;
    }

    public Request(Command command, DominoTile dominoTile) {
        this.command = command;
        this.dominoTile = dominoTile;
    }

    @Override
    public String toString() {
        if (dominoTile == null){
            return "Request{" +
                    "command=" + command +
                    '}';
        }
        return "Request{" +
                "command=" + command + " " +
                dominoTile +
                '}';
    }
}
