package ru.vsu.cs.babkina.SwingClientServer;

import ru.vsu.cs.babkina.GameModel.Round;

import java.io.Serializable;

public class Response implements Serializable {
    private Command command;
    private Round round;

    public Command getCommand() {
        return command;
    }

    public Round getRound() {
        return round;
    }

    public Response(Command command, Round round) {
        this.command = command;
        this.round = round;
    }

    @Override
    public String toString() {
        return "Response{" +
                "command=" + command + " " +
                round.toString() +
                '}';
    }
}
