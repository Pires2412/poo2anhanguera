import model.Tetromino;
import model.TetrominoType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TetrominoTest {

    private Tetromino novaT() {
        return new Tetromino(new int[][]{{0,1,0},{1,1,1}}, TetrominoType.T, 4, 0);
    }

    @Test
    void construtorDefineDimensoesEPosicao() {
        Tetromino t = novaT();
        assertEquals(2, t.getRows());
        assertEquals(3, t.getColumns());
        assertEquals(4, t.getPositionX());
        assertEquals(0, t.getPositionY());
        assertEquals(TetrominoType.T, t.getType());
    }

    @Test
    void movimentos() {
        Tetromino t = novaT();
        t.moveDown();
        assertEquals(1, t.getPositionY());
        t.moveRight();
        assertEquals(5, t.getPositionX());
        t.moveLeft();
        assertEquals(4, t.getPositionX());
    }

    @Test
    void rotacaoNaoAlteraOriginalEGiraCorretamente() {
        Tetromino t = novaT();
        int[][] r = t.rotatedShape();
        // original continua 2x3
        assertEquals(2, t.getShape().length);
        // rotacionado vira 3x2
        assertEquals(3, r.length);
        assertEquals(2, r[0].length);
    }

    @Test
    void setShapeAtualizaDimensoes() {
        Tetromino t = novaT();
        t.setShape(new int[][]{{1},{1},{1},{1}});
        assertEquals(4, t.getRows());
        assertEquals(1, t.getColumns());
    }

    @Test
    void corVemDoTipo() {
        Tetromino t = novaT();
        assertEquals(TetrominoType.T.getColor(), t.getColor());
    }
}
