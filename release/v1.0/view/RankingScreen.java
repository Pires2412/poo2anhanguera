package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import model.ScoreEntry;

/**
 * Diálogo modal que exibe as 10 melhores pontuações, em ordem decrescente,
 * mostrando posição, nome do jogador e pontuação.
 */
public class RankingScreen {

    private final JDialog dialog;

    /**
     * Cria o diálogo de ranking.
     *
     * @param owner  a janela dona do diálogo (pode ser {@code null})
     * @param scores as entradas a exibir, já ordenadas
     */
    public RankingScreen(Window owner, List<ScoreEntry> scores) {

        dialog = new JDialog(owner, "Top 10 - Melhores Pontuacoes", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(460, 600);
        dialog.setLocationRelativeTo(owner);

        JPanel root = new JPanel();
        root.setBackground(Color.decode("#1C1C1C"));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("TOP 10");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        root.add(title);
        root.add(Box.createVerticalStrut(25));

        root.add(buildRow("#", "NOME", "PONTOS", true));
        root.add(Box.createVerticalStrut(10));

        if (scores.isEmpty()) {
            JLabel empty = new JLabel("Nenhuma pontuacao registrada ainda.");
            empty.setForeground(Color.LIGHT_GRAY);
            empty.setFont(new Font("Arial", Font.PLAIN, 16));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            root.add(Box.createVerticalStrut(20));
            root.add(empty);
        } else {
            int rank = 1;
            for (ScoreEntry entry : scores) {
                root.add(buildRow(
                        String.valueOf(rank),
                        entry.getName(),
                        String.valueOf(entry.getScore()),
                        false));
                root.add(Box.createVerticalStrut(6));
                rank++;
            }
        }

        root.add(Box.createVerticalGlue());

        JButton close = new JButton("Fechar");
        close.setFocusable(false);
        close.setAlignmentX(Component.CENTER_ALIGNMENT);
        close.setPreferredSize(new Dimension(160, 45));
        close.setMaximumSize(new Dimension(160, 45));
        close.setBackground(Color.decode("#3A3A3A"));
        close.setForeground(Color.WHITE);
        close.setFont(new Font("Arial", Font.BOLD, 16));
        close.addActionListener(e -> dialog.dispose());

        root.add(Box.createVerticalStrut(15));
        root.add(close);

        dialog.add(root);
    }

    private JPanel buildRow(String rank, String name, String score, boolean header) {

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel rankLabel = new JLabel(rank);
        JLabel nameLabel = new JLabel(name);
        JLabel scoreLabel = new JLabel(score, SwingConstants.RIGHT);

        Font font = new Font("Arial", header ? Font.BOLD : Font.PLAIN, header ? 16 : 18);
        Color color = header ? Color.decode("#9765c8") : Color.WHITE;

        for (JLabel label : new JLabel[]{rankLabel, nameLabel, scoreLabel}) {
            label.setForeground(color);
            label.setFont(font);
        }

        rankLabel.setPreferredSize(new Dimension(45, 30));
        scoreLabel.setPreferredSize(new Dimension(120, 30));

        row.add(rankLabel, BorderLayout.WEST);
        row.add(nameLabel, BorderLayout.CENTER);
        row.add(scoreLabel, BorderLayout.EAST);

        return row;
    }

    /** Exibe o diálogo (bloqueante até ser fechado). */
    public void showDialog() {
        dialog.setVisible(true);
    }
}
