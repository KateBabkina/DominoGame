package ru.vsu.cs.babkina.GameModel;

import java.util.ArrayList;
import java.util.List;

public class Human implements Player{
    private List<DominoTile> tilesOfHuman = new ArrayList<>();
    private DominoTile dominoTile;

    public void setDominoTile(DominoTile dominoTile) {
        this.dominoTile = dominoTile;
    }

    @Override
    public ResultOfLogicExecution makeAMove(Round round) {
        int value = round.getDominoTileIndex(tilesOfHuman, dominoTile.getLeftNumber(), dominoTile.getRightNumber());

        int right = round.getRight();
        int left = round.getLeft();

        if (round.getQuantityOfExtraTiles() == 0) {
            boolean findSuitableTile = false;
            for (DominoTile tileOfHuman : tilesOfHuman) {
                if (tileOfHuman.getRightNumber() == right || tileOfHuman.getRightNumber() == left
                        || tileOfHuman.getLeftNumber() == right || tileOfHuman.getLeftNumber() == left) {
                    findSuitableTile = true;
                    break;
                }
            }
            if (!findSuitableTile) {
                return ResultOfLogicExecution.THE_END_OF_THE_GAME;
            }
        }
        if (dominoTile.getLeftNumber() != left && dominoTile.getLeftNumber() != right && dominoTile.getRightNumber() != left && dominoTile.getRightNumber() != right) {
            return ResultOfLogicExecution.WRONG_TILE;
        }
        if ((dominoTile.getLeftNumber() == left || dominoTile.getRightNumber() == left) && (dominoTile.getLeftNumber() == right || dominoTile.getRightNumber() == right)) {
            return ResultOfLogicExecution.POSITION_CHOICE;
        }
        if (dominoTile.getLeftNumber() == left || dominoTile.getRightNumber() == left) {
            if (dominoTile.getLeftNumber() == left) {
                dominoTile.changeLeftRight();
            }
            round.putTileToTheLeft(tilesOfHuman.get(value), tilesOfHuman);
        } else if (dominoTile.getLeftNumber() == right || dominoTile.getRightNumber() == right) {
            if (dominoTile.getRightNumber() == right) {
                dominoTile.changeLeftRight();
            }
            round.putTileToTheRight(tilesOfHuman.get(value), tilesOfHuman);
        }

        return ResultOfLogicExecution.CORRECT;
    }

    @Override
    public List<DominoTile> getTilesOfPlayer() {
        return tilesOfHuman;
    }

}
