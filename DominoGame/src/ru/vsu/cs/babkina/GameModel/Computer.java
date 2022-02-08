package ru.vsu.cs.babkina.GameModel;


import java.util.ArrayList;
import java.util.List;

public class Computer implements Player{

    private List<DominoTile> tilesOfComputer = new ArrayList<>();

    @Override
    public ResultOfLogicExecution makeAMove(Round round) {

        int right = round.getRight();
        int left = round.getLeft();
        int index = 0;

        for (DominoTile tile : tilesOfComputer) {
            if ((tile.getLeftNumber() == left || tile.getRightNumber() == left) && (tile.getLeftNumber() == right || tile.getRightNumber() == right)) {
                int randomValue = (int) (Math.random() * 2);
                switch (randomValue) {
                    case (0):
                        if (tile.getRightNumber() == right) {
                            tile.changeLeftRight();
                        }
                        round.putTileToTheRight(tilesOfComputer.get(index), tilesOfComputer);
                        break;
                    case (1):
                        if (tile.getLeftNumber() == left) {
                            tile.changeLeftRight();
                        }
                        round.putTileToTheLeft(tilesOfComputer.get(index), tilesOfComputer);
                        break;
                    default:
                        break;
                }
                return ResultOfLogicExecution.CORRECT;
            } else if (tile.getLeftNumber() == left || tile.getRightNumber() == left) {
                if (tile.getLeftNumber() == left) {
                    tile.changeLeftRight();
                }
                round.putTileToTheLeft(tilesOfComputer.get(index), tilesOfComputer);
                return ResultOfLogicExecution.CORRECT;
            } else if (tile.getLeftNumber() == right || tile.getRightNumber() == right) {
                if (tile.getRightNumber() == right) {
                    tile.changeLeftRight();
                }
                round.putTileToTheRight(tilesOfComputer.get(index), tilesOfComputer);
                return ResultOfLogicExecution.CORRECT;
            }
            index++;
        }
        if (round.getQuantityOfExtraTiles() == 0) {
            return ResultOfLogicExecution.THE_END_OF_THE_GAME;
        }
        tilesOfComputer.add(round.takeRandomExtraTile());
        return ResultOfLogicExecution.TAKE_EXTRA_TILE;
    }

    @Override
    public List<DominoTile> getTilesOfPlayer() {
        return tilesOfComputer;
    }
}
