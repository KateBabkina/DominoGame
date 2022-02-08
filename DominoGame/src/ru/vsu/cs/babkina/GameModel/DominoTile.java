package ru.vsu.cs.babkina.GameModel;

import java.io.Serializable;

public class DominoTile implements Serializable {
    private int leftNumber;
    private int rightNumber;

    public DominoTile(int leftNumber, int rightNumber) {
        this.leftNumber = leftNumber;
        this.rightNumber = rightNumber;
    }

    public int getLeftNumber() {
        return leftNumber;
    }

    public int getRightNumber() {
        return rightNumber;
    }

    public void changeLeftRight() {
        int temp = leftNumber;
        leftNumber = rightNumber;
        rightNumber = temp;
    }

    @Override
    public String toString() {
        return "DominoTile{" +
                "leftNumber=" + leftNumber +
                ", rightNumber=" + rightNumber +
                '}';
    }
}
