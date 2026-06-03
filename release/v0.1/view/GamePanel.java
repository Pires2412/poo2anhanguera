package view;

import javax.swing.*;
import java.awt.*;

import model.GameState;

/**
 * Painel lateral (v0.1): HUD de nível/pontuação e botões Play, Pause e Exit.
 * Sem preview da próxima peça.
 */
public class GamePanel {

    private JPanel panel;
    private JButton playButton;
    private JButton pauseButton;
    private JButton exitButton;
    private JLabel levelLabel;
    private JLabel scoreLabel;
    private GameState gameState;

    /**
     * Cria o painel lateral.
     *
     * @param gameState o estado de jogo
     */
    public GamePanel(GameState gameState) {
        this.gameState = gameState;

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 800));
        panel.setBackground(Color.decode("#1C1C1C"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalStrut(60));
        levelLabel = label("LEVEL: --");
        panel.add(levelLabel);
        panel.add(Box.createVerticalStrut(20));
        scoreLabel = label("SCORE: --");
        panel.add(scoreLabel);
        panel.add(Box.createVerticalGlue());

        playButton  = createButton("Play");
        pauseButton = createButton("Pause");
        exitButton  = createButton("Exit");

        panel.add(playButton);  panel.add(Box.createVerticalStrut(10));
        panel.add(pauseButton); panel.add(Box.createVerticalStrut(10));
        panel.add(exitButton);  panel.add(Box.createVerticalStrut(30));
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 22));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
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

    /** @return o componente do painel */
    public JPanel getGamePanel() { return panel; }
    /** @return o botão Play */
    public JButton getPlayButton() { return playButton; }
    /** @return o botão Pause */
    public JButton getPauseButton() { return pauseButton; }
    /** @return o botão Exit */
    public JButton getExitButton() { return exitButton; }
}
