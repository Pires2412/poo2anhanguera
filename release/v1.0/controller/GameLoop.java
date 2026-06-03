package controller;

import javax.swing.SwingUtilities;

import model.GameState;

/**
 * Laço de jogo executado em uma thread dedicada. É a thread que controla a
 * velocidade de queda das peças: a cada ciclo ela dorme por
 * {@link GameState#getFallDelay()} milissegundos (que diminui conforme o
 * nível sobe) e então agenda, na thread da interface (EDT), a descida da
 * peça e a atualização da tela.
 *
 * <p>Suporta pausar/retomar (via {@code wait}/{@code notify}) e parar de
 * forma segura. Todas as alterações de estado ocorrem na EDT, evitando
 * condições de corrida com a entrada do teclado.
 */
public class GameLoop implements Runnable {

    private final GameState gameState;
    private final Runnable onTick;
    private final Runnable onGameOver;

    private volatile boolean running = true;
    private volatile boolean paused = false;
    private boolean gameOverNotified = false;

    private final Object pauseLock = new Object();
    private Thread thread;

    /**
     * Cria o laço de jogo.
     *
     * @param gameState  o estado de jogo controlado
     * @param onTick     ação executada na EDT após cada descida
     * @param onGameOver ação executada na EDT uma única vez ao fim do jogo
     */
    public GameLoop(GameState gameState, Runnable onTick, Runnable onGameOver) {
        this.gameState = gameState;
        this.onTick = onTick;
        this.onGameOver = onGameOver;
    }

    /** Inicia a thread do laço de jogo. */
    public void start() {
        thread = new Thread(this, "tetris-game-loop");
        thread.start();
    }

    /** Corpo da thread: dorme conforme o nível e agenda cada descida na EDT. */
    @Override
    public void run() {
        while (running) {

            synchronized (pauseLock) {
                while (paused && running) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        if (!running) return;
                    }
                }
            }

            if (!running) break;

            try {
                Thread.sleep(gameState.getFallDelay());
            } catch (InterruptedException e) {
                if (!running) break;
                continue;
            }

            if (!running) break;
            if (paused) continue;

            SwingUtilities.invokeLater(() -> {
                if (!running) return;

                if (gameState.isGameOver()) {
                    notifyGameOver();
                    return;
                }

                gameState.moveTetrominoDown();
                onTick.run();

                if (gameState.isGameOver()) {
                    notifyGameOver();
                }
            });
        }
    }

    private void notifyGameOver() {
        if (gameOverNotified) return;
        gameOverNotified = true;
        running = false;
        if (onGameOver != null) onGameOver.run();
    }

    /** Pausa a queda das peças. */
    public void pause() {
        paused = true;
    }

    /** Retoma a queda das peças. */
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    /** @return {@code true} se o laço está pausado */
    public boolean isPaused() {
        return paused;
    }

    /** @return {@code true} se o laço ainda está ativo */
    public boolean isRunning() {
        return running;
    }

    /** Encerra a thread do laço de jogo de forma definitiva. */
    public void stop() {
        running = false;
        resume();
        if (thread != null) thread.interrupt();
    }
}
