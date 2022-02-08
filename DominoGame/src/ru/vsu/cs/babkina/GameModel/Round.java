package ru.vsu.cs.babkina.GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Round implements Serializable {
    private final List<DominoTile> extraTiles = new ArrayList<>();
    private final List<DominoTile> currentGameProcess = new ArrayList<>();
    private final Player human;
    private final Player computer;
    private GameState gameState;

    public int getRight() {
        return currentGameProcess.get(currentGameProcess.size() - 1).getRightNumber();
    }

    public int getLeft() {
        return currentGameProcess.get(0).getLeftNumber();
    }

    public int getQuantityOfExtraTiles() {
        return extraTiles.size();
    }

    public int getQuantityOfHumanTiles() {
        return getTilesOfHuman().size();
    }

    public List<DominoTile> getTilesOfHuman() {
        return human.getTilesOfPlayer();
    }

    public int getQuantityOfComputerTiles() {
        return getTilesOfComputer().size();
    }

    public List<DominoTile> getTilesOfComputer() {
        return computer.getTilesOfPlayer();
    }

    public List<DominoTile> getCurrentGameProcess() {
        return currentGameProcess;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Round(Player human, Player computer) {
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                extraTiles.add(new DominoTile(i, j));
            }
        }

        this.human = human;
        this.computer = computer;

        for (int countOfAddition = 0; countOfAddition < 7; countOfAddition++) {
            getTilesOfHuman().add(this.takeRandomExtraTile());
        }
        for (int countOfAddition = 0; countOfAddition < 7; countOfAddition++) {
            getTilesOfComputer().add(this.takeRandomExtraTile());
        }

        currentGameProcess.add(getTilesOfComputer().remove((int) (Math.random() * getTilesOfComputer().size())));
        gameState = GameState.TURN_OF_HUMAN;
    }


    public DominoTile takeRandomExtraTile() {
        int index = (int) (Math.random() * extraTiles.size());
        return extraTiles.remove(index);
    }

    public void putTileToTheRight(DominoTile dominoTile, List<DominoTile> tilesOfPlayer) {
        int right = currentGameProcess.get(currentGameProcess.size() - 1).getRightNumber();
        tilesOfPlayer.remove(getDominoTileIndex(tilesOfPlayer, dominoTile.getLeftNumber(), dominoTile.getRightNumber()));
        if (dominoTile.getRightNumber() == right) {
            dominoTile.changeLeftRight();
        }
        currentGameProcess.add(dominoTile);
    }

    public void putTileToTheLeft(DominoTile dominoTile, List<DominoTile> tilesOfPlayer) {
        int left = currentGameProcess.get(0).getLeftNumber();
        tilesOfPlayer.remove(getDominoTileIndex(tilesOfPlayer, dominoTile.getLeftNumber(), dominoTile.getRightNumber()));
        if (dominoTile.getLeftNumber() == left) {
            dominoTile.changeLeftRight();
        }
        currentGameProcess.add(0, dominoTile);
    }

    public void updateGameStateAfterHumanMove(DominoTile currDominoTile) {
        ((Human) human).setDominoTile(currDominoTile);
        ResultOfLogicExecution resultOfLogicExecution = human.makeAMove(this);

        switch (resultOfLogicExecution) {
            case CORRECT:
                if (human.getTilesOfPlayer().size() == 0) {
                    gameState = GameState.HUMAN_WINNING;
                    break;
                }
                gameState = GameState.TURN_OF_COMPUTER;
                updateGameStateAfterComputerMove();
                break;
            case WRONG_TILE:
                gameState = GameState.WRONG_TILE;
                break;
            case POSITION_CHOICE:
                if (human.getTilesOfPlayer().size() == 1) {
                    gameState = GameState.HUMAN_WINNING;
                    break;
                }
                gameState = GameState.POSITION_CHOICE;
                break;
            case THE_END_OF_THE_GAME:
                if (this.getQuantityOfHumanTiles() < this.getQuantityOfComputerTiles()) {
                    gameState = GameState.HUMAN_WINNING;
                } else if (this.getQuantityOfHumanTiles() > this.getQuantityOfComputerTiles()) {
                    gameState = GameState.HUMAN_LOOSING;
                } else {
                    gameState = GameState.DRAW;
                }
                break;
            default:
                break;
        }
    }

    public void updateGameStateAfterComputerMove() {
        ResultOfLogicExecution resultOfComputerMove = ResultOfLogicExecution.WRONG_TILE;
        while (resultOfComputerMove != ResultOfLogicExecution.CORRECT && resultOfComputerMove != ResultOfLogicExecution.THE_END_OF_THE_GAME) {
            resultOfComputerMove = computer.makeAMove(this);
            switch (resultOfComputerMove) {
                case CORRECT:
                    if (computer.getTilesOfPlayer().size() == 0) {
                        gameState = GameState.HUMAN_LOOSING;
                        break;
                    }
                    gameState = GameState.TURN_OF_HUMAN;
                    break;
                case TAKE_EXTRA_TILE:
                    gameState = GameState.TURN_OF_COMPUTER;
                    break;
                case THE_END_OF_THE_GAME:
                    if (this.getQuantityOfHumanTiles() < this.getQuantityOfComputerTiles()) {
                        gameState = GameState.HUMAN_WINNING;
                    } else if (this.getQuantityOfHumanTiles() > this.getQuantityOfComputerTiles()) {
                        gameState = GameState.HUMAN_LOOSING;
                    } else {
                        gameState = GameState.DRAW;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public int getDominoTileIndex(List<DominoTile> tilesOfPlayer, int leftNumber, int rightNumber) {
        for (int dominoTileIndex = 0; dominoTileIndex < tilesOfPlayer.size(); dominoTileIndex++) {
            if (leftNumber == tilesOfPlayer.get(dominoTileIndex).getLeftNumber() && rightNumber == tilesOfPlayer.get(dominoTileIndex).getRightNumber()) {
                return dominoTileIndex;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Round{" +
                "gameState=" + gameState +
                '}';
    }
}
