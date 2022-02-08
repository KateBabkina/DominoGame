package ru.vsu.cs.babkina.ConsoleApp;

import ru.vsu.cs.babkina.GameModel.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleVisualization {
    protected Player human = new Human();
    protected Player computer = new Computer();
    protected final Round currRound = new Round(human, computer);


    public boolean printGameProcess() {
        printMove();
        int indexOfDominoTile = getNext();
        if ((indexOfDominoTile >= currRound.getQuantityOfHumanTiles() || indexOfDominoTile < 0) && indexOfDominoTile != 50) {
            return false;
        }

        if (indexOfDominoTile == 50) {
            if (currRound.getQuantityOfExtraTiles() == 0) {
                for (DominoTile tile : currRound.getTilesOfHuman()) {
                    if (tile.getLeftNumber() == currRound.getLeft() || tile.getLeftNumber() == currRound.getRight()
                            || tile.getRightNumber() == currRound.getLeft() || tile.getRightNumber() == currRound.getRight()) {
                        havingTile();
                    }
                }
                endGame();
                return true;
            }
            currRound.getTilesOfHuman().add(currRound.takeRandomExtraTile());
        } else {
            currRound.updateGameStateAfterHumanMove(currRound.getTilesOfHuman().get(indexOfDominoTile));
        }

        if (currRound.getGameState() == GameState.POSITION_CHOICE) {
            int indexOfPosition = -1;
            while (indexOfPosition != 0 && indexOfPosition != 1) {
                printMove();
                indexOfPosition = getNext();
            }
            if (indexOfPosition == 0) {
                currRound.putTileToTheLeft(currRound.getTilesOfHuman().get(indexOfDominoTile), currRound.getTilesOfHuman());
            } else {
                currRound.putTileToTheRight(currRound.getTilesOfHuman().get(indexOfDominoTile), currRound.getTilesOfHuman());
            }
            currRound.updateGameStateAfterComputerMove();
        }

        if (currRound.getGameState() == GameState.DRAW
                || currRound.getGameState() == GameState.HUMAN_LOOSING
                || currRound.getGameState() == GameState.HUMAN_WINNING) {
            endGame();
            return true;
        }
        return false;
    }

    protected Integer getNext() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    protected void endGame() {
        printMove();
    }

    private void printTileOfHuman(DominoTile tile) {
        System.out.printf("❚ %d | %d ❚", tile.getLeftNumber(), tile.getRightNumber());
    }

    private void printTileOfComputer() {
        System.out.print("❚  |  ❚");
    }

    private void printTilesOfHuman() {
        List<DominoTile> tilesOfPlayer = currRound.getTilesOfHuman();
        for (int indexOfDominoTile = 0; indexOfDominoTile < tilesOfPlayer.size(); indexOfDominoTile++) {
            System.out.printf("(%d)", indexOfDominoTile);
            printTileOfHuman(tilesOfPlayer.get(indexOfDominoTile));
            System.out.print("    ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTilesOfComputer() {
        List<DominoTile> tilesOfPlayer = currRound.getTilesOfComputer();
        for (int indexOfDominoTile = 0; indexOfDominoTile < tilesOfPlayer.size(); indexOfDominoTile++) {
            System.out.printf("(%d)", indexOfDominoTile);
            printTileOfComputer();
            System.out.print("    ");
        }
        System.out.println();
        System.out.println();
    }

    private void printCurrentGameProcess() {
        List<DominoTile> currentGameProcess = currRound.getCurrentGameProcess();
        for (DominoTile gameProcessTile : currentGameProcess) {
            printTileOfHuman(gameProcessTile);
            System.out.print("  ");
        }
        System.out.println();
        System.out.println();
    }

    private void havingTile() {
        System.out.println("You have suitable tile!");
    }

    private void printQuantityOfExtraTiles() {
        System.out.println("Extra tiles left: " + currRound.getQuantityOfExtraTiles());
    }

    protected void printMove() {
        System.out.print(currRound.getGameState().getStringOfState());
        printQuantityOfExtraTiles();
        printTilesOfHuman();
        printCurrentGameProcess();
        printTilesOfComputer();
    }
}
