import model.GameState;
import model.TetrominoType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void estadoInicial() {
        GameState gs = new GameState();
        assertFalse(gs.isGameOver());
        assertEquals(0, gs.getScore());
        assertEquals(1, gs.getLevel());
        assertNull(gs.getCurrentTetromino());
    }

    @Test
    void initGameCriaPecaAtualEProxima() {
        GameState gs = new GameState();
        gs.initGame();
        assertNotNull(gs.getCurrentTetromino());
        assertNotNull(gs.getNextTetromino());
    }

    @Test
    void fallDelayDiminuiComNivelERespeitaPiso() {
        GameState gs = new GameState();
        gs.setLevel(1);
        assertEquals(1000, gs.getFallDelay());
        gs.setLevel(5);
        assertEquals(1000 - 4 * 25, gs.getFallDelay());
        gs.setLevel(100);
        assertEquals(100, gs.getFallDelay(), "Nunca abaixo do piso de 100ms");
    }

    @Test
    void pecaDesceComMoveDown() {
        GameState gs = new GameState();
        gs.initGame();
        int y0 = gs.getCurrentTetromino().getPositionY();
        gs.moveTetrominoDown();
        assertEquals(y0 + 1, gs.getCurrentTetromino().getPositionY());
    }

    @Test
    void limparLinhaSomaPontuacao() {
        GameState gs = new GameState();
        gs.initGame();
        // Preenche toda a ultima linha; ao fixar a proxima peca, ela deve limpar.
        for (int c = 0; c < gs.getBoard().getColumns(); c++) {
            gs.getBoard().setCell(19, c, TetrominoType.O);
        }
        int guarda = 0;
        while (gs.getScore() == 0 && guarda++ < 300) {
            gs.moveTetrominoDown();
        }
        assertEquals(1000, gs.getScore(), "Uma linha limpa vale 1000 pontos");
    }

    @Test
    void gameOverQuandoPecaColideAoNascer() {
        GameState gs = new GameState();
        gs.initGame();
        assertFalse(gs.isGameOver());
        // bloqueia a regiao de nascimento (linhas 0-1) sem completar linha
        for (int r = 0; r < 2; r++)
            for (int c = 1; c < 10; c++)
                gs.getBoard().setCell(r, c, TetrominoType.O);
        gs.spawnNewTetromino();
        assertTrue(gs.isGameOver(), "Nascer sobre blocos deve encerrar o jogo");
    }

    @Test
    void resetGameLimpaTudo() {
        GameState gs = new GameState();
        gs.initGame();
        gs.setScore(5000);
        gs.setLevel(3);
        gs.getBoard().setCell(10, 5, TetrominoType.S);
        gs.resetGame();
        assertEquals(0, gs.getScore());
        assertEquals(1, gs.getLevel());
        assertFalse(gs.isGameOver());
        assertNull(gs.getBoard().getCell(10, 5));
    }

    @Test
    void naoMoveAlemDaParedeEsquerda() {
        GameState gs = new GameState();
        gs.initGame();
        for (int i = 0; i < 20; i++) gs.moveTetrominoLeft();
        assertTrue(gs.getCurrentTetromino().getPositionX() >= 0);
    }

    @Test
    void naoMoveAlemDaParedeDireita() {
        GameState gs = new GameState();
        gs.initGame();
        for (int i = 0; i < 20; i++) gs.moveTetrominoRight();
        int maxX = gs.getCurrentTetromino().getPositionX() + gs.getCurrentTetromino().getColumns();
        assertTrue(maxX <= gs.getBoard().getColumns());
    }
}
