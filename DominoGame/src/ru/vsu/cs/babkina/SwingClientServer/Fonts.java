package ru.vsu.cs.babkina.SwingClientServer;

import java.awt.*;

public enum Fonts {
    DOMINO_TILE_FONT(new Font("DOMINO_TILE_FONT", Font.BOLD, 50)),
    EXTRA_TILE_BUTTON_FONT(new Font("EXTRA_TILE_BUTTON_FONT", Font.BOLD, 30)),
    TEXT_FONT(new Font("TEXT_FONT", Font.BOLD, 20));

    private final Font font;

    Fonts(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}
