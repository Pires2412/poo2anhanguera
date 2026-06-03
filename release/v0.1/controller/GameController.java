package controller;

import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import view.GameWindow;
import model.GameState;

/**
 * Controlador (v0.1). Usa um {@link javax.swing.Timer} (sem concorrência
 * explícita) para a queda das peças em velocidade fixa. Trata teclado e os
 * botões Play, Pause e Exit. Game over é detectado quando uma nova peça
 * colide ao nascer; pressione R para reiniciar.
 */
public class GameController {

    private GameWindow gameWindow;
    private Timer gameLoop;
    private GameState gameState;

    private static final int FALL_DELAY = 500;

    /** Cria o controlador e inicia uma nova partida. */
    public GameController() {
        gameState = new GameState();
        gameState.initGame();

        gameWindow = new GameWindow(gameState);
        gameWindow.running();

        gameWindow.getGamePanel().getPlayButton().addActionListener(e -> startGame());
        gameWindow.getGamePanel().getPauseButton().addActionListener(e -> pauseGame());
        gameWindow.getGamePanel().getExitButton().addActionListener(e -> System.exit(0));

        gameWindow.getScreen().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameState.isGameOver()) {
                    if (e.getKeyCode() == KeyEvent.VK_R) restartGame();
                    return;
                }
                if (gameState.getCurrentTetromino() == null) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> { gameState.moveTetrominoLeft(); refreshView(); }
                    case KeyEvent.VK_RIGHT -> { gameState.moveTetrominoRight(); refreshView(); }
                    case KeyEvent.VK_DOWN -> { gameState.moveTetrominoDown(); refreshView(); }
                    case KeyEvent.VK_UP -> { gameState.rotateTetromino(); refreshView(); }
                }
            }
        });

        refreshView();
        startGame();
        gameWindow.getScreen().requestFocusInWindow();
    }

    /** Inicia (ou retoma) o laço de queda das peças. */
    public void startGame() {
        if (gameLoop != null && gameLoop.isRunning()) return;
        gameLoop = new Timer(FALL_DELAY, e -> {
            if (gameState.isGameOver()) {
                ((Timer) e.getSource()).stop();
                refreshView();
                return;
            }
            gameState.moveTetrominoDown();
            refreshView();
        });
        gameLoop.start();
        gameWindow.getScreen().requestFocusInWindow();
    }

    /** Pausa ou retoma o laço de queda. */
    public void pauseGame() {
        if (gameLoop == null) return;
        if (gameLoop.isRunning()) gameLoop.stop();
        else gameLoop.start();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void restartGame() {
        if (gameLoop != null) gameLoop.stop();
        gameState.resetGame();
        refreshView();
        startGame();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void refreshView() {
        gameWindow.getGameBoard().updateAndRepaint();
        gameWindow.getGamePanel().updateHud(gameState.getScore(), gameState.getLevel());
    }
}
