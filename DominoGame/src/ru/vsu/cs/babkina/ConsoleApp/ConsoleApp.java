package ru.vsu.cs.babkina.ConsoleApp;

public class ConsoleApp{

    public static void main(String[] args) {

        ConsoleVisualization consoleVisualization = new ConsoleVisualization();
        boolean gameIsOver = false;
        while (!gameIsOver) {
            gameIsOver = consoleVisualization.printGameProcess();
        }
    }
}
