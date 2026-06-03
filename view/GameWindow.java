package view;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.GameState;

/**
 * Janela principal do jogo. Monta, lado a lado, o tabuleiro
 * ({@link GameBoard}) e o painel lateral de HUD/controles
 * ({@link GamePanel}) dentro de um {@link JFrame}.
 */
public class GameWindow {

    private JFrame screen;
    private GameBoard gameBoard;
    private GamePanel gamePanel;

    private static boolean notResizable = false;

    /**
     * Cria a janela e seus painéis a partir do estado de jogo.
     *
     * @param gameState o estado de jogo a ser exibido
     */
    public GameWindow(GameState gameState) {

        this.screen = new JFrame("Tetris");

        this.gameBoard = new GameBoard(gameState);

        this.gamePanel = new GamePanel(gameState);
    }

    /** Configura o layout, dimensiona e exibe a janela. */
    public void running() {

        screen.setSize(1000, 800);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.X_AXIS));

        body.add(gameBoard.getGameBoard());
        body.add(gamePanel.getGamePanel());

        screen.add(body);

        screen.setResizable(notResizable);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setLocationRelativeTo(null);
        screen.setVisible(true);

        screen.requestFocus();
    }

    /** @return o painel lateral de HUD e controles */
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    /** @return o painel do tabuleiro */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /** @return o {@link JFrame} da janela */
    public JFrame getScreen() {
        return screen;
    }
}
