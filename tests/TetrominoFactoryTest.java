import model.Tetromino;
import model.TetrominoFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TetrominoFactoryTest {

    @Test
    void criaPecaNaoNula() {
        Tetromino t = TetrominoFactory.createRandomTetromino(5, 0);
        assertNotNull(t);
        assertNotNull(t.getType());
        assertNotNull(t.getShape());
    }

    @Test
    void criaPecaNaPosicaoInformada() {
        Tetromino t = TetrominoFactory.createRandomTetromino(7, 2);
        assertEquals(7, t.getPositionX());
        assertEquals(2, t.getPositionY());
    }

    @Test
    void formaContemAlgumBloco() {
        for (int i = 0; i < 50; i++) {
            int[][] shape = TetrominoFactory.createRandomTetromino(0, 0).getShape();
            int blocos = 0;
            for (int[] row : shape) for (int v : row) blocos += v;
            assertTrue(blocos >= 4, "Todo tetromino tem ao menos 4 blocos");
        }
    }
}
