package view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela inicial do jogo. Oferece as opções de iniciar um novo jogo, carregar
 * um jogo salvo, ver o ranking ou sair. As ações são ligadas pelo
 * controlador através dos métodos {@code setOn...}.
 */
public class StartScreen {

    private final JFrame frame;

    private final JButton newGameButton;
    private final JButton loadGameButton;
    private final JButton rankingButton;
    private final JButton exitButton;

    /**
     * Cria a tela inicial.
     *
     * @param saveExists se {@code true}, habilita o botão "Carregar Jogo"
     */
    public StartScreen(boolean saveExists) {

        frame = new JFrame("Tetris");
        frame.setSize(420, 560);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel();
        root.setBackground(Color.decode("#1C1C1C"));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("TETRIS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 56));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        root.add(Box.createVerticalGlue());
        root.add(title);
        root.add(Box.createVerticalStrut(50));

        newGameButton  = createButton("Novo Jogo", true);
        loadGameButton = createButton("Carregar Jogo", saveExists);
        rankingButton  = createButton("Ranking", true);
        exitButton     = createButton("Sair", true);

        root.add(newGameButton);
        root.add(Box.createVerticalStrut(15));
        root.add(loadGameButton);
        root.add(Box.createVerticalStrut(15));
        root.add(rankingButton);
        root.add(Box.createVerticalStrut(15));
        root.add(exitButton);
        root.add(Box.createVerticalGlue());

        frame.add(root);
    }

    private JButton createButton(String text, boolean enabled) {

        JButton button = new JButton(text);

        button.setEnabled(enabled);
        button.setFocusable(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setPreferredSize(new Dimension(260, 55));
        button.setMaximumSize(new Dimension(260, 55));
        button.setMinimumSize(new Dimension(260, 55));

        button.setBackground(Color.decode("#3A3A3A"));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));

        return button;
    }

    /** @param action ação executada ao clicar em "Novo Jogo" */
    public void setOnNewGame(Runnable action) {
        newGameButton.addActionListener(e -> action.run());
    }

    /** @param action ação executada ao clicar em "Carregar Jogo" */
    public void setOnLoadGame(Runnable action) {
        loadGameButton.addActionListener(e -> action.run());
    }

    /** @param action ação executada ao clicar em "Ranking" */
    public void setOnRanking(Runnable action) {
        rankingButton.addActionListener(e -> action.run());
    }

    /** @param action ação executada ao clicar em "Sair" */
    public void setOnExit(Runnable action) {
        exitButton.addActionListener(e -> action.run());
    }

    /** Exibe a tela inicial. */
    public void show() {
        frame.setVisible(true);
    }

    /** Fecha a tela inicial. */
    public void close() {
        frame.dispose();
    }
}
