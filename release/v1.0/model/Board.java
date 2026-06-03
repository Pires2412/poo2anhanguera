package model;

/**
 * Representa o tabuleiro do Tetris: uma grade fixa de 10 colunas por 20 linhas.
 * Cada célula guarda o {@link TetrominoType} da peça fixada ali, ou {@code null}
 * se estiver vazia.
 */
public class Board {

    private static final int WIDTH_SIZE = 10;
    private static final int HEIGHT_SIZE = 20;

    private TetrominoType[][] grid;

    /** Cria um tabuleiro vazio com as dimensões padrão. */
    public Board() {
        grid = new TetrominoType[HEIGHT_SIZE][WIDTH_SIZE];
    }

    /**
     * @return o número de linhas (altura) do tabuleiro
     */
    public int getRows() {
        return HEIGHT_SIZE;
    }

    /**
     * @return o número de colunas (largura) do tabuleiro
     */
    public int getColumns() {
        return WIDTH_SIZE;
    }

    /**
     * Retorna o tipo de peça fixada em uma célula.
     *
     * @param row    a linha (0 = topo)
     * @param column a coluna (0 = esquerda)
     * @return o {@link TetrominoType} da célula, ou {@code null} se vazia
     */
    public TetrominoType getCell(int row, int column) {
        return grid[row][column];
    }

    /**
     * Define o conteúdo de uma célula.
     *
     * @param row    a linha
     * @param column a coluna
     * @param type   o tipo a fixar, ou {@code null} para esvaziar
     */
    public void setCell(int row, int column, TetrominoType type) {
        grid[row][column] = type;
    }

    /** Esvazia todas as células do tabuleiro. */
    public void clearBoard() {
        for (int row = 0; row < HEIGHT_SIZE; row++) {
            for (int col = 0; col < WIDTH_SIZE; col++) {
                grid[row][col] = null;
            }
        }
    }
}
