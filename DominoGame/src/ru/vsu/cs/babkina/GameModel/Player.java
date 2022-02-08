package ru.vsu.cs.babkina.GameModel;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {
    ResultOfLogicExecution makeAMove(Round round);
    List<DominoTile> getTilesOfPlayer();
}
