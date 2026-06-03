package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import model.GameState;
import model.ScoreEntry;
import model.ScoreRepository;
import view.GameWindow;

/**
 * Controlador (v0.2). Inicia o jogo diretamente, roda a queda em uma thread
 * ({@link GameLoop}) com velocidade variável por nível, trata teclado e
 * botões (Play/Pause/Restart/Exit) e, ao fim do jogo, grava a pontuação
 * máxima em {@code scores.json}.
 */
public class GameController {

    private GameWindow gameWindow;
    private GameState gameState;
    private GameLoop gameLoop;

    private final ScoreRepository scoreRepository = new ScoreRepository();
    private boolean gameOverHandled = false;

    /** Cria o controlador e inicia uma nova partida. */
    public GameController() {
        gameState = new GameState();
        gameState.initGame();

        gameWindow = new GameWindow(gameState);
        gameWindow.running();

        wireButtons();
        wireKeys();
        refreshView();
        startLoop();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void wireButtons() {
        gameWindow.getGamePanel().getPlayButton().addActionListener(e -> resumeGame());
        gameWindow.getGamePanel().getPauseButton().addActionListener(e -> pauseGame());
        gameWindow.getGamePanel().getRestartButton().addActionListener(e -> restartGame());
        gameWindow.getGamePanel().getExitButton().addActionListener(e -> System.exit(0));
    }

    private void wireKeys() {
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
                    case KeyEvent.VK_DOWN -> { gameState.moveTetrominoDown(); refreshView(); checkGameOver(); }
                    case KeyEvent.VK_UP -> { gameState.rotateTetromino(); refreshView(); }
                    case KeyEvent.VK_P -> pauseGame();
                }
            }
        });
    }

    private void startLoop() {
        if (gameLoop != null) gameLoop.stop();
        gameLoop = new GameLoop(gameState, this::refreshView, this::onGameOver);
        gameLoop.start();
    }

    private void resumeGame() {
        if (gameState.isGameOver()) return;
        if (gameLoop == null || !gameLoop.isRunning()) startLoop();
        else gameLoop.resume();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void pauseGame() {
        if (gameLoop == null) return;
        if (gameLoop.isPaused()) gameLoop.resume(); else gameLoop.pause();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void restartGame() {
        if (gameLoop != null) gameLoop.stop();
        gameState.resetGame();
        gameOverHandled = false;
        refreshView();
        startLoop();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void checkGameOver() {
        if (gameState.isGameOver()) onGameOver();
    }

    private void onGameOver() {
        if (gameOverHandled) return;
        gameOverHandled = true;
        if (gameLoop != null) gameLoop.stop();
        refreshView();
        scoreRepository.addScore(new ScoreEntry("Player", gameState.getScore(), gameState.getLevel()));
        JOptionPane.showMessageDialog(gameWindow.getScreen(),
                "Game Over!\nPontuacao: " + gameState.getScore() + "\nRecorde salvo em scores.json",
                "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void refreshView() {
        gameWindow.getGameBoard().updateAndRepaint();
        gameWindow.getGamePanel().repaintNextPiece();
        gameWindow.getGamePanel().updateHud(gameState.getScore(), gameState.getLevel());
    }
}
