package ru.vsu.cs.babkina.ClientServer;

import ru.vsu.cs.babkina.ConsoleApp.ConsoleVisualization;
import ru.vsu.cs.babkina.GameModel.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SocketVisualization extends ConsoleVisualization {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public SocketVisualization(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot connect to client", ex);
        }
    }

    private String getMoveString() {
        StringBuilder builder = new StringBuilder();
        builder.append(currRound.getGameState().getStringOfState());
        builder.append(" Extra tiles ").append(currRound.getQuantityOfExtraTiles()).append("  ");
        List<DominoTile> tilesOfPlayer = currRound.getTilesOfHuman();
        for (int indexOfDominoTile = 0; indexOfDominoTile < tilesOfPlayer.size(); indexOfDominoTile++) {
            builder.append("(").append(indexOfDominoTile).append(")❚ ").append(tilesOfPlayer.get(indexOfDominoTile).getLeftNumber()).append(" | ").append(tilesOfPlayer.get(indexOfDominoTile).getRightNumber()).append(" ❚  ");
        }
        builder.append("    ");
        List<DominoTile> currentGameProcess = currRound.getCurrentGameProcess();
        for (DominoTile gameProcessTile : currentGameProcess) {
            builder.append("❚ ").append(gameProcessTile.getLeftNumber()).append(" | ").append(gameProcessTile.getRightNumber()).append(" ❚  ");
        }
        builder.append("    ");
        List<DominoTile> tilesOfComputer = currRound.getTilesOfComputer();
        for (int indexOfDominoTile = 0; indexOfDominoTile < tilesOfComputer.size(); indexOfDominoTile++) {
            builder.append("(").append(indexOfDominoTile).append(")❚  |  ❚ ");
        }
        return builder.toString();
    }

    @Override
    public void printMove() {
        String command = Command.MOVE.getCommandString();
        System.out.println("To client: " + command);
        command += Command.SEPARATOR + getMoveString();
        out.println(command);
    }

    @Override
    public Integer getNext() {
        String answer;
        try {
            while ((answer = in.readLine()) == null) {
            }
            String[] answerParsed = answer.split(Command.SEPARATOR);
            if (Command.RESP.getCommandString().equals(answerParsed[0])) {
                System.out.println("From client: " + answer);
            } else {
                throw new IllegalArgumentException("Client response is not recognized: " + answer);
            }
            return Integer.parseInt(answerParsed[1]);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot communicate with a client", ex);
        }
    }

    @Override
    public void endGame() {
        try {
            String command = Command.END.getCommandString();
            System.out.println("To client:" + command);
            command += Command.SEPARATOR + getMoveString();
            out.println(command);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Cannot close socket: " + e.getMessage());
            }
        }
    }

}
