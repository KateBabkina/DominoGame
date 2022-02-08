package ru.vsu.cs.babkina.SwingClientServer;

import ru.vsu.cs.babkina.GameModel.DominoTile;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class DominoButton extends JButton implements Serializable {
    private DominoTile dominoTile;

    public DominoButton() {
        setEnabled(false);
        setFont(Fonts.DOMINO_TILE_FONT.getFont());
    }

    public DominoTile getDominoTile() {
        return dominoTile;
    }

    public void setDominoTile(DominoTile dominoTile) {
        this.dominoTile = dominoTile;
        init();
    }

    private void init(){
        setBackground(Color.ORANGE);
        setEnabled(true);
        setText(dominoTile.getLeftNumber() + " | " + dominoTile.getRightNumber());
    }

    public void clear(){
        setBackground(null);
        setEnabled(false);
        setText(null);
    }
}
