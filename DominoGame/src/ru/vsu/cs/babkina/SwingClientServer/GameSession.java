package ru.vsu.cs.babkina.SwingClientServer;

import ru.vsu.cs.babkina.GameModel.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameSession implements Runnable {

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private final Player human = new Human();
    private final Player computer = new Computer();
    private final Round currRound = new Round(human, computer);

    public GameSession(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot connect to client", ex);
        }
    }

    public void run() {
        try {
            out.writeObject(currRound);
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Request input = (Request) in.readObject();
                DominoTile dominoTile = input.getDominoTile();
                Command command = input.getCommand();
                if (command == Command.END) {
                    socket.close();
                    System.out.println("Socked closed");
                    break;
                }
                System.out.println("From client " + input);
                if (command == Command.MOVE && dominoTile != null) {
                    currRound.updateGameStateAfterHumanMove(dominoTile);
                }
                if (command != Command.MOVE && dominoTile != null) {
                    if (command == Command.RIGHT) {
                        currRound.putTileToTheRight(dominoTile, currRound.getTilesOfHuman());
                    } else {
                        currRound.putTileToTheLeft(dominoTile, currRound.getTilesOfHuman());
                    }
                    currRound.updateGameStateAfterComputerMove();
                }
                if (command == Command.EXTRA_TILE) {
                    currRound.getTilesOfHuman().add(currRound.takeRandomExtraTile());
                }

                Command outputCommand = Command.GAME;
                if (currRound.getGameState() == GameState.POSITION_CHOICE) {
                    outputCommand = Command.POSITION_CHOICE;
                }
                if (currRound.getGameState() == GameState.DRAW
                        || currRound.getGameState() == GameState.HUMAN_LOOSING
                        || currRound.getGameState() == GameState.HUMAN_WINNING) {
                    outputCommand = Command.END;
                }
                Response output = new Response(outputCommand, currRound);
                out.writeObject(output);
                out.reset();

                System.out.println("To client " + output);
                if (outputCommand == Command.END) {
                    socket.close();
                    System.out.println("Socked closed");
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}