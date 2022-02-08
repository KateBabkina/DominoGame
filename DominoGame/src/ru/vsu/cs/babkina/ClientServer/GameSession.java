package ru.vsu.cs.babkina.ClientServer;

import java.net.Socket;

public class GameSession implements Runnable {

    private final SocketVisualization socketVisualization;

    public GameSession(Socket socket) {
        socketVisualization = new SocketVisualization(socket);
    }

    public void run() {
        boolean gameIsOver = false;
        while (!gameIsOver) {
            gameIsOver = socketVisualization.printGameProcess();
        }
        System.out.println("Game Over!");
    }

}