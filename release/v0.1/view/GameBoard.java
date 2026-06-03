package view;

import javax.swing.JPanel;

import model.Board;
import model.GameState;
import model.Tetromino;
import model.TetrominoType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Painel que desenha o tabuleiro: a grade, as peças já fixadas, a peça em
 * queda e o overlay de fim de jogo ("GAME OVER").
 */
public class GameBoard {

    private Board board;
    private JPanel shape;
    private GameState gameState;

    /**
     * Cria o painel do tabuleiro ligado ao estado de jogo.
     *
     * @param gameState o estado de jogo a renderizar
     */
    public GameBoard(GameState gameState) {
        this.gameState = gameState;
        this.board = gameState.getBoard();

        this.shape = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int cellSize = 35;

                int gridWidth = board.getColumns() * cellSize;
                int gridHeight = board.getRows() * cellSize;

                int offsetX = (getWidth() - gridWidth) / 2;
                int offsetY = (getHeight() - gridHeight) / 2;

                for (int row = 0; row < board.getRows(); row++) {
                    for (int col = 0; col < board.getColumns(); col++) {

                        int x = offsetX + (col * cellSize);
                        int y = offsetY + (row * cellSize);

                        g.setColor(Color.decode("#1C1C1C"));
                        g.drawRect(x, y, cellSize, cellSize);

                        TetrominoType type = board.getCell(row, col);

                        if (type != null) {

                            g.setColor(type.getColor());
                            g.fillRect(x, y, cellSize, cellSize);

                            g.setColor(Color.BLACK);
                            g.drawRect(x, y, cellSize, cellSize);
                        }
                    }
                }

                Tetromino tetromino = gameState.getCurrentTetromino();

                if (tetromino != null) {

                    int[][] pieceShape = tetromino.getShape();

                    for (int row = 0; row < tetromino.getRows(); row++) {
                        for (int col = 0; col < tetromino.getColumns(); col++) {

                            if (pieceShape[row][col] == 1) {

                                int boardRow = tetromino.getPositionY() + row;

                                if (boardRow < 0) continue;

                                int x = offsetX + ((tetromino.getPositionX() + col) * cellSize);
                                int y = offsetY + (boardRow * cellSize);

                                g.setColor(tetromino.getColor());
                                g.fillRect(x, y, cellSize, cellSize);

                                g.setColor(Color.BLACK);
                                g.drawRect(x, y, cellSize, cellSize);
                            }
                        }
                    }
                }

                if (gameState.isGameOver()) {

                    g.setColor(new Color(0, 0, 0, 170));
                    g.fillRect(offsetX, offsetY, gridWidth, gridHeight);

                    g.setFont(new Font("Arial", Font.BOLD, 36));
                    FontMetrics fm = g.getFontMetrics();
                    String gameOverText = "GAME OVER";
                    int textX = offsetX + (gridWidth - fm.stringWidth(gameOverText)) / 2;
                    int textY = offsetY + gridHeight / 2 - 20;
                    g.setColor(Color.WHITE);
                    g.drawString(gameOverText, textX, textY);

                    g.setFont(new Font("Arial", Font.PLAIN, 18));
                    fm = g.getFontMetrics();
                    String restartText = "Press R to Restart";
                    int restartX = offsetX + (gridWidth - fm.stringWidth(restartText)) / 2;
                    g.drawString(restartText, restartX, textY + 40);
                }
            }
        };

        shape.setPreferredSize(new Dimension(600, 800));
        shape.setBackground(Color.decode("#262626"));
    }

    /** @return o componente Swing do tabuleiro */
    public JPanel getGameBoard() {
        return shape;
    }

    /** Redesenha o tabuleiro. */
    public void updateAndRepaint() {
        shape.repaint();
    }
}
