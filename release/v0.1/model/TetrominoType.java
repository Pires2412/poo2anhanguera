package model;

import java.awt.Color;

/**
 * Os sete tipos clássicos de peça do Tetris (tetrominós), cada um associado
 * a uma cor usada na renderização.
 */
public enum TetrominoType {

    O(Color.decode("#f23456")),
    I(Color.decode("#124e96")),
    T(Color.decode("#23b2da")),
    L(Color.decode("#a80139")),
    J(Color.decode("#fd0053")),
    S(Color.decode("#7a56d0")),
    Z(Color.decode("#9765c8"));

    private final Color color;

    TetrominoType(Color color) {
        this.color = color;
    }

    /**
     * @return a cor associada a este tipo de peça
     */
    public Color getColor() {
        return color;
    }
}
