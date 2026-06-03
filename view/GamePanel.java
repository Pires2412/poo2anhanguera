package view;

import javax.swing.*;
import java.awt.*;

import model.GameState;
import model.Tetromino;

/**
 * Painel lateral do jogo. Mostra o preview da próxima peça, os rótulos de
 * nível e pontuação (HUD) e os botões de controle (Play, Pause, Restart,
 * Save, Ranking, Exit).
 */
public class GamePanel {

    private JPanel panel;

    private JButton playButton;
    private JButton pauseButton;
    private JButton exitButton;

    private JButton restartButton;
    private JButton saveButton;
    private JButton rankingButton;

    private JLabel levelLabel;
    private JLabel scoreLabel;

    private JPanel nextPiecePanel;

    private GameState gameState;

    /**
     * Cria o painel lateral ligado ao estado de jogo.
     *
     * @param gameState o estado de jogo (usado para o preview da próxima peça)
     */
    public GamePanel(GameState gameState) {

        this.gameState = gameState;

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 800));
        panel.setBackground(Color.decode("#1C1C1C"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel nextLabel = new JLabel("NEXT:");
        nextLabel.setForeground(Color.WHITE);
        nextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(nextLabel);
        panel.add(Box.createVerticalStrut(10));

        nextPiecePanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Tetromino next = gameState.getNextTetromino();

                if (next == null) return;

                int[][] shape = next.getShape();
                int cellSize = 28;

                int pieceW = shape[0].length * cellSize;
                int pieceH = shape.length * cellSize;

                int offsetX = (getWidth() - pieceW) / 2;
                int offsetY = (getHeight() - pieceH) / 2;

                for (int row = 0; row < shape.length; row++) {
                    for (int col = 0; col < shape[0].length; col++) {

                        if (shape[row][col] == 1) {

                            int x = offsetX + col * cellSize;
                            int y = offsetY + row * cellSize;

                            g.setColor(next.getColor());
                            g.fillRect(x, y, cellSize, cellSize);

                            g.setColor(Color.BLACK);
                            g.drawRect(x, y, cellSize, cellSize);
                        }
                    }
                }
            }
        };

        nextPiecePanel.setPreferredSize(new Dimension(160, 160));
        nextPiecePanel.setMaximumSize(new Dimension(160, 160));
        nextPiecePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextPiecePanel.setBackground(Color.decode("#262626"));
        nextPiecePanel.setBorder(BorderFactory.createLineBorder(Color.decode("#404040"), 2));

        panel.add(nextPiecePanel);

        panel.add(Box.createVerticalStrut(30));

        levelLabel = new JLabel("LEVEL: --");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 22));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(levelLabel);

        panel.add(Box.createVerticalStrut(20));

        scoreLabel = new JLabel("SCORE: --");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 22));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(scoreLabel);

        panel.add(Box.createVerticalGlue());

        playButton    = createButton("Play",    true);
        pauseButton   = createButton("Pause",   true);
        restartButton = createButton("Restart", false);
        saveButton    = createButton("Save",    false);
        rankingButton = createButton("Ranking", false);
        exitButton    = createButton("Exit",    true);

        panel.add(playButton);
        panel.add(Box.createVerticalStrut(10));

        panel.add(pauseButton);
        panel.add(Box.createVerticalStrut(10));

        panel.add(restartButton);
        panel.add(Box.createVerticalStrut(10));

        panel.add(saveButton);
        panel.add(Box.createVerticalStrut(10));

        panel.add(rankingButton);
        panel.add(Box.createVerticalStrut(10));

        panel.add(exitButton);
        panel.add(Box.createVerticalStrut(30));
    }

    private JButton createButton(String text, boolean enabled) {

        JButton button = new JButton(text);

        button.setEnabled(enabled);
        button.setFocusable(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setMinimumSize(new Dimension(200, 50));

        button.setBackground(Color.decode("#3A3A3A"));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));

        return button;
    }

    /**
     * Atualiza os rótulos de pontuação e nível.
     *
     * @param score a pontuação a exibir
     * @param level o nível a exibir
     */
    public void updateHud(long score, int level) {
        scoreLabel.setText("SCORE: " + score);
        levelLabel.setText("LEVEL: " + level);
    }

    /** Redesenha o preview da próxima peça. */
    public void repaintNextPiece() {
        nextPiecePanel.repaint();
    }

    /** @return o componente Swing do painel lateral */
    public JPanel getGamePanel() {
        return panel;
    }

    /** @return o painel de preview da próxima peça */
    public JPanel getNextPiecePanel() {
        return nextPiecePanel;
    }

    /** @return o botão Play */
    public JButton getPlayButton() {
        return playButton;
    }

    /** @return o botão Pause */
    public JButton getPauseButton() {
        return pauseButton;
    }

    /** @return o botão Exit */
    public JButton getExitButton() {
        return exitButton;
    }

    /** @return o botão Restart */
    public JButton getRestartButton() {
        return restartButton;
    }

    /** @return o botão Save */
    public JButton getSaveButton() {
        return saveButton;
    }

    /** @return o botão Ranking */
    public JButton getRankingButton() {
        return rankingButton;
    }
}
