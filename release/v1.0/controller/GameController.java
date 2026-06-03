package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import model.GameSave;
import model.GameState;
import model.ScoreEntry;
import model.ScoreRepository;
import view.GameWindow;
import view.RankingScreen;
import view.StartScreen;

/**
 * Controlador principal (camada controller do padrão MVC). Conecta o estado
 * de jogo ({@link GameState}) às telas ({@link view}), gerencia o laço de
 * jogo em thread ({@link GameLoop}), trata a entrada do teclado e os botões,
 * e coordena salvar/carregar partida, ranking e fim de jogo.
 */
public class GameController {

    private GameWindow gameWindow;
    private GameState gameState;
    private GameLoop gameLoop;

    private final ScoreRepository scoreRepository = new ScoreRepository();
    private final GameSave gameSave = new GameSave();

    private boolean gameOverHandled = false;

    /** Cria o controlador e exibe a tela inicial. */
    public GameController() {
        showStartScreen();
    }

    private void showStartScreen() {

        StartScreen startScreen = new StartScreen(gameSave.exists());

        startScreen.setOnNewGame(() -> {
            startScreen.close();
            startNewGame();
        });

        startScreen.setOnLoadGame(() -> {
            try {
                GameState loaded = gameSave.load();
                startScreen.close();
                startGameWith(loaded);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Nao foi possivel carregar o jogo:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        startScreen.setOnRanking(() ->
                new RankingScreen(null, scoreRepository.topScores(10)).showDialog());

        startScreen.setOnExit(() -> System.exit(0));

        startScreen.show();
    }

    private void startNewGame() {
        GameState state = new GameState();
        state.initGame();
        startGameWith(state);
    }

    private void startGameWith(GameState state) {

        this.gameState = state;
        this.gameOverHandled = false;

        if (gameState.getCurrentTetromino() == null && !gameState.isGameOver()) {
            gameState.initGame();
        }

        this.gameWindow = new GameWindow(gameState);
        gameWindow.running();

        enableControls();
        wireButtons();
        wireKeys();
        refreshView();

        startLoop();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void enableControls() {
        gameWindow.getGamePanel().getRestartButton().setEnabled(true);
        gameWindow.getGamePanel().getSaveButton().setEnabled(true);
        gameWindow.getGamePanel().getRankingButton().setEnabled(true);
    }

    private void wireButtons() {

        gameWindow.getGamePanel().getPlayButton()
                .addActionListener(e -> resumeGame());

        gameWindow.getGamePanel().getPauseButton()
                .addActionListener(e -> pauseGame());

        gameWindow.getGamePanel().getRestartButton()
                .addActionListener(e -> restartGame());

        gameWindow.getGamePanel().getSaveButton()
                .addActionListener(e -> saveGame());

        gameWindow.getGamePanel().getRankingButton()
                .addActionListener(e -> showRanking());

        gameWindow.getGamePanel().getExitButton()
                .addActionListener(e -> System.exit(0));
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

                    case KeyEvent.VK_LEFT -> {
                        gameState.moveTetrominoLeft();
                        refreshView();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        gameState.moveTetrominoRight();
                        refreshView();
                    }
                    case KeyEvent.VK_DOWN -> {
                        gameState.moveTetrominoDown();
                        refreshView();
                        checkGameOver();
                    }
                    case KeyEvent.VK_UP -> {
                        gameState.rotateTetromino();
                        refreshView();
                    }
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
        if (gameLoop == null || !gameLoop.isRunning()) {
            startLoop();
        } else {
            gameLoop.resume();
        }
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void pauseGame() {
        if (gameLoop == null) return;
        if (gameLoop.isPaused()) {
            gameLoop.resume();
        } else {
            gameLoop.pause();
        }
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

    private void saveGame() {

        boolean wasRunning = gameLoop != null && gameLoop.isRunning() && !gameLoop.isPaused();
        if (gameLoop != null) gameLoop.pause();

        try {
            gameSave.save(gameState);
            JOptionPane.showMessageDialog(gameWindow.getScreen(),
                    "Jogo salvo com sucesso!", "Salvar", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(gameWindow.getScreen(),
                    "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        if (wasRunning && !gameState.isGameOver()) gameLoop.resume();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void showRanking() {

        boolean wasRunning = gameLoop != null && gameLoop.isRunning() && !gameLoop.isPaused();
        if (gameLoop != null) gameLoop.pause();

        new RankingScreen(gameWindow.getScreen(), scoreRepository.topScores(10)).showDialog();

        if (wasRunning && !gameState.isGameOver()) gameLoop.resume();
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

        String name = JOptionPane.showInputDialog(gameWindow.getScreen(),
                "Game Over!\nPontuacao: " + gameState.getScore() + "\n\nDigite seu nome:",
                "Fim de Jogo", JOptionPane.PLAIN_MESSAGE);

        if (name == null || name.trim().isEmpty()) name = "Anonimo";

        scoreRepository.addScore(new ScoreEntry(name.trim(), gameState.getScore(), gameState.getLevel()));

        new RankingScreen(gameWindow.getScreen(), scoreRepository.topScores(10)).showDialog();
        gameWindow.getScreen().requestFocusInWindow();
    }

    private void refreshView() {
        gameWindow.getGameBoard().updateAndRepaint();
        gameWindow.getGamePanel().repaintNextPiece();
        gameWindow.getGamePanel().updateHud(gameState.getScore(), gameState.getLevel());
    }
}
