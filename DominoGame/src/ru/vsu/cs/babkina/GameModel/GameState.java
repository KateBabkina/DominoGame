package ru.vsu.cs.babkina.GameModel;

public enum GameState {
    TURN_OF_HUMAN("It's your turn!"),
    TURN_OF_COMPUTER("It's computer's turn!"),
    HUMAN_WINNING("The end of the game. You won!"),
    HUMAN_LOOSING("The end of the game. You lost!"),
    DRAW("The end of the game. It's a draw!"),
    POSITION_CHOICE("Choose position!"),
    WRONG_TILE("Wrong tile! Choose another one.");

    private final String stringOfState;

    GameState(String stringOfState) {
        this.stringOfState = stringOfState;
    }

    public String getStringOfState() {
        return stringOfState;
    }
}
