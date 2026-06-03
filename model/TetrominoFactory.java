package model;

import java.util.Random;

/**
 * Fábrica responsável por criar peças aleatórias com a forma correta para
 * cada {@link TetrominoType}.
 */
public class TetrominoFactory {

    private static final Random random = new Random();

    private static final TetrominoType[] TYPES = TetrominoType.values();

    /**
     * Cria uma peça de tipo aleatório na posição informada.
     *
     * @param x a coluna inicial
     * @param y a linha inicial
     * @return uma nova {@link Tetromino} de tipo sorteado
     */
    public static Tetromino createRandomTetromino(int x, int y) {

        TetrominoType type = TYPES[random.nextInt(TYPES.length)];

        return new Tetromino(
            getShape(type),
            type,
            x,
            y
        );
    }

    private static int[][] getShape(TetrominoType type) {

        return switch (type) {

            case O -> new int[][]{{1,1},{1,1}};
            case I -> new int[][]{{1,1,1,1}};
            case T -> new int[][]{{0,1,0},{1,1,1}};
            case L -> new int[][]{{1,0,0},{1,1,1}};
            case J -> new int[][]{{0,0,1},{1,1,1}};
            case S -> new int[][]{{0,1,1},{1,1,0}};
            case Z -> new int[][]{{1,1,0},{0,1,1}};
        };
    }
}
