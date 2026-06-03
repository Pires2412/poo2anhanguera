package view;

import javax.swing.*;
import java.awt.*;

import model.GameState;
import model.Tetromino;

/**
 * Painel lateral (v0.2): preview da próxima peça, HUD de nível/pontuação e
 * botões Play, Pause, Restart e Exit.
 */
public class GamePanel {

    private JPanel panel;

    private JButton playButton;
    private JButton pauseButton;
    private JButton restartButton;
    private JButton exitButton;

    private JLabel levelLabel;
    private JLabel scoreLabel;

    private JPanel nextPiecePanel;

    private GameState gameState;

    /**
     * Cria o painel lateral.
     *
     * @param gameState o estado de jogo (usado no preview)
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
                int offsetX = (getWidth() - shape[0].length * cellSize) / 2;
                int offsetY = (getHeight() - shape.length * cellSize) / 2;
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
        levelLabel = label("LEVEL: --");
        panel.add(levelLabel);
        panel.add(Box.createVerticalStrut(20));
        scoreLabel = label("SCORE: --");
        panel.add(scoreLabel);
        panel.add(Box.createVerticalGlue());

        playButton    = createButton("Play",    true);
        pauseButton   = createButton("Pause",   true);
        restartButton = createButton("Restart", true);
        exitButton    = createButton("Exit",    true);

        panel.add(playButton);    panel.add(Box.createVerticalStrut(10));
        panel.add(pauseButton);   panel.add(Box.createVerticalStrut(10));
        panel.add(restartButton); panel.add(Box.createVerticalStrut(10));
        panel.add(exitButton);    panel.add(Box.createVerticalStrut(30));
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 22));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
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
     * @param score a pontuação
     * @param level o nível
     */
    public void updateHud(long score, int level) {
        scoreLabel.setText("SCORE: " + score);
        levelLabel.setText("LEVEL: " + level);
    }

    /** Redesenha o preview da próxima peça. */
    public void repaintNextPiece() { nextPiecePanel.repaint(); }

    /** @return o componente do painel */
    public JPanel getGamePanel() { return panel; }
    /** @return o botão Play */
    public JButton getPlayButton() { return playButton; }
    /** @return o botão Pause */
    public JButton getPauseButton() { return pauseButton; }
    /** @return o botão Restart */
    public JButton getRestartButton() { return restartButton; }
    /** @return o botão Exit */
    public JButton getExitButton() { return exitButton; }
}
