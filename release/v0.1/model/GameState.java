package model;

/**
 * Núcleo de regras do jogo. Mantém o tabuleiro, a peça atual e a próxima,
 * a pontuação, o nível e o estado de fim de jogo, e implementa toda a lógica:
 * movimentação, colisão, fixação de peças, limpeza de linhas e pontuação.
 */
public class GameState {

    private Board board;
    private Tetromino currentTetromino;
    private Tetromino nextTetromino;

    private boolean gameOver = false;

    private long score = 0;
    private int level = 1;

    /** Cria um estado de jogo novo com tabuleiro vazio. */
    public GameState() {
        this.board = new Board();
    }

    /** @return a peça que está caindo no momento, ou {@code null} */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /** @return a próxima peça a entrar (usada no preview), ou {@code null} */
    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    /** @return o tabuleiro */
    public Board getBoard() {
        return board;
    }

    /** @return {@code true} se o jogo terminou */
    public boolean isGameOver() {
        return gameOver;
    }

    /** @return a pontuação atual */
    public long getScore() {
        return score;
    }

    /** @return o nível atual */
    public int getLevel() {
        return level;
    }

    /**
     * Define a pontuação (usado ao carregar um jogo salvo).
     *
     * @param score a pontuação
     */
    public void setScore(long score) {
        this.score = score;
    }

    /**
     * Define o nível (usado ao carregar um jogo salvo).
     *
     * @param level o nível
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Define o estado de fim de jogo (usado ao carregar um jogo salvo).
     *
     * @param gameOver o estado
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Define a peça atual (usado ao carregar um jogo salvo).
     *
     * @param currentTetromino a peça
     */
    public void setCurrentTetromino(Tetromino currentTetromino) {
        this.currentTetromino = currentTetromino;
    }

    /**
     * Define a próxima peça (usado ao carregar um jogo salvo).
     *
     * @param nextTetromino a peça
     */
    public void setNextTetromino(Tetromino nextTetromino) {
        this.nextTetromino = nextTetromino;
    }

    /**
     * Calcula o intervalo de queda (em milissegundos) com base no nível:
     * quanto maior o nível, menor o intervalo, com um piso de 100ms.
     *
     * @return o atraso de queda em milissegundos
     */
    public int getFallDelay() {
        int delay = 1000 - ((level - 1) * 25);
        return Math.max(delay, 100);
    }

    /** Inicializa a primeira peça e a próxima, começando o jogo. */
    public void initGame() {
        nextTetromino = TetrominoFactory.createRandomTetromino(0, 0);
        spawnNewTetromino();
    }

    /** Reinicia completamente o jogo: limpa tabuleiro, pontuação e nível. */
    public void resetGame() {
        board.clearBoard();
        currentTetromino = null;
        nextTetromino = null;
        gameOver = false;
        score = 0;
        level = 1;
        initGame();
    }

    /**
     * Gera uma nova peça no topo do tabuleiro, promove a próxima peça e
     * sorteia a seguinte. Se a nova peça já colidir com blocos fixos ao
     * nascer, marca fim de jogo.
     */
    public void spawnNewTetromino() {

        int startX = board.getColumns() / 2;

        Tetromino newTetromino = nextTetromino != null
                ? nextTetromino
                : TetrominoFactory.createRandomTetromino(startX, 0);

        newTetromino.setPositionX(startX);
        newTetromino.setPositionY(0);

        nextTetromino = TetrominoFactory.createRandomTetromino(0, 0);

        if (collidesWithBoard(newTetromino)) {
            gameOver = true;
            return;
        }

        this.currentTetromino = newTetromino;
    }

    private boolean collidesWithBoard(Tetromino tetromino) {

        int[][] shape = tetromino.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {

                if (shape[row][col] == 1) {

                    int boardX = tetromino.getPositionX() + col;
                    int boardY = tetromino.getPositionY() + row;

                    if (boardY < 0) continue;

                    if (board.getCell(boardY, boardX) != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean canMoveDown() {

        int[][] shape = currentTetromino.getShape();
        int nextY = currentTetromino.getPositionY() + 1;

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {

                if (shape[row][col] == 1) {

                    int boardX = currentTetromino.getPositionX() + col;
                    int boardY = nextY + row;

                    if (boardY >= board.getRows())
                        return false;

                    if (boardY < 0) continue;

                    if (board.getCell(boardY, boardX) != null)
                        return false;
                }
            }
        }

        return true;
    }

    private void lockTetromino() {

        int[][] shape = currentTetromino.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {

                if (shape[row][col] == 1) {

                    int boardX = currentTetromino.getPositionX() + col;
                    int boardY = currentTetromino.getPositionY() + row;

                    if (boardY < 0) continue;

                    board.setCell(boardY, boardX, currentTetromino.getType());
                }
            }
        }
    }

    private int clearLines() {

        int clearedLines = 0;

        for (int row = board.getRows() - 1; row >= 0; row--) {

            if (isLineFull(row)) {
                removeLine(row);
                clearedLines++;
                row++;
            }
        }

        return clearedLines;
    }

    private boolean isLineFull(int row) {

        for (int col = 0; col < board.getColumns(); col++) {
            if (board.getCell(row, col) == null) {
                return false;
            }
        }

        return true;
    }

    private void removeLine(int row) {

        for (int r = row; r > 0; r--) {
            for (int col = 0; col < board.getColumns(); col++) {
                board.setCell(r, col, board.getCell(r - 1, col));
            }
        }

        for (int col = 0; col < board.getColumns(); col++) {
            board.setCell(0, col, null);
        }
    }

    private void addScore(int clearedLines) {

        if (clearedLines <= 0) return;

        score += (long) (1000 * Math.pow(2, clearedLines - 1));
        level = (int) (score / 10000) + 1;
    }

    /**
     * Avança a peça atual uma linha para baixo. Se não puder descer, fixa a
     * peça no tabuleiro, limpa linhas completas, soma a pontuação e gera a
     * próxima peça.
     */
    public void moveTetrominoDown() {

        if (gameOver) return;

        if (canMoveDown()) {
            currentTetromino.moveDown();
        } else {
            lockTetromino();
            int clearedLines = clearLines();
            addScore(clearedLines);
            spawnNewTetromino();
        }
    }

    /** Move a peça atual para a esquerda, se houver espaço. */
    public void moveTetrominoLeft() {
        if (canMoveLeft()) {
            currentTetromino.moveLeft();
        }
    }

    /** Move a peça atual para a direita, se houver espaço. */
    public void moveTetrominoRight() {
        if (canMoveRight()) {
            currentTetromino.moveRight();
        }
    }

    private boolean canMoveLeft() {

        int[][] shape = currentTetromino.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {

                if (shape[row][col] == 1) {

                    int boardX = currentTetromino.getPositionX() + col - 1;

                    if (boardX < 0) return false;

                    int boardY = currentTetromino.getPositionY() + row;

                    if (boardY < 0) continue;

                    if (board.getCell(boardY, boardX) != null) return false;
                }
            }
        }

        return true;
    }

    private boolean canMoveRight() {

        int[][] shape = currentTetromino.getShape();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {

                if (shape[row][col] == 1) {

                    int boardX = currentTetromino.getPositionX() + col + 1;

                    if (boardX >= board.getColumns()) return false;

                    int boardY = currentTetromino.getPositionY() + row;

                    if (boardY < 0) continue;

                    if (board.getCell(boardY, boardX) != null) return false;
                }
            }
        }

        return true;
    }

    private boolean canRotate(int[][] newShape) {

        for (int row = 0; row < newShape.length; row++) {
            for (int col = 0; col < newShape[0].length; col++) {

                if (newShape[row][col] == 1) {

                    int boardX = currentTetromino.getPositionX() + col;
                    int boardY = currentTetromino.getPositionY() + row;

                    if (boardX < 0 || boardX >= board.getColumns())
                        return false;

                    if (boardY >= board.getRows())
                        return false;

                    if (boardY < 0) continue;

                    if (board.getCell(boardY, boardX) != null)
                        return false;
                }
            }
        }

        return true;
    }

    /** Rotaciona a peça atual 90° no sentido horário, se a rotação for válida. */
    public void rotateTetromino() {

        int[][] rotated = currentTetromino.rotatedShape();

        if (canRotate(rotated)) {
            currentTetromino.setShape(rotated);
        }
    }
}
