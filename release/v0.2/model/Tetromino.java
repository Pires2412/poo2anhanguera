package model;

/**
 * Uma peça do Tetris em jogo. Mantém sua forma (matriz de 0s e 1s), seu
 * {@link TetrominoType} e sua posição (coluna/linha) no tabuleiro.
 */
public class Tetromino {

    private int[][] shape;
    private int rows;
    private int columns;
    private int positionX;
    private int positionY;

    private TetrominoType type;

    /**
     * Cria uma peça.
     *
     * @param shape     a matriz de forma (1 = bloco preenchido, 0 = vazio)
     * @param type      o tipo da peça
     * @param positionX a coluna inicial
     * @param positionY a linha inicial
     */
    public Tetromino(int[][] shape, TetrominoType type, int positionX, int positionY) {
        this.shape = shape;

        this.rows = shape.length;
        this.columns = shape[0].length;

        this.positionX = positionX;
        this.positionY = positionY;

        this.type = type;
    }

    /** @return o tipo da peça */
    public TetrominoType getType() {
        return type;
    }

    /** @return a cor da peça, derivada do seu tipo */
    public java.awt.Color getColor() {
        return type.getColor();
    }

    /** @return a matriz de forma atual da peça */
    public int[][] getShape() {
        return shape;
    }

    /**
     * Calcula a forma resultante de uma rotação de 90° no sentido horário,
     * sem alterar a peça.
     *
     * @return a nova matriz de forma rotacionada
     */
    public int[][] rotatedShape() {

        int[][] original = shape;

        int rows = original.length;
        int cols = original[0].length;

        int[][] rotated = new int[cols][rows];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rotated[c][rows - 1 - r] = original[r][c];
            }
        }

        return rotated;
    }

    /**
     * Substitui a forma da peça (usado após uma rotação válida).
     *
     * @param newShape a nova matriz de forma
     */
    public void setShape(int[][] newShape) {
        this.shape = newShape;
        this.rows = newShape.length;
        this.columns = newShape[0].length;
    }

    /** @return o número de linhas da forma */
    public int getRows() {
        return rows;
    }

    /** @return o número de colunas da forma */
    public int getColumns() {
        return columns;
    }

    /** @return a coluna atual da peça */
    public int getPositionX() {
        return positionX;
    }

    /** @return a linha atual da peça */
    public int getPositionY() {
        return positionY;
    }

    /**
     * @param positionX a nova coluna
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /**
     * @param positionY a nova linha
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /** Desce a peça uma linha. */
    public void moveDown() {
        positionY++;
    }

    /** Move a peça uma coluna para a direita. */
    public void moveRight() {
        positionX++;
    }

    /** Move a peça uma coluna para a esquerda. */
    public void moveLeft() {
        positionX--;
    }
}
