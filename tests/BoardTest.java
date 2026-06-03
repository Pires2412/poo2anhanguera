import model.Board;
import model.TetrominoType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void dimensoesPadrao() {
        Board board = new Board();
        assertEquals(20, board.getRows());
        assertEquals(10, board.getColumns());
    }

    @Test
    void celulaComecaVazia() {
        Board board = new Board();
        assertNull(board.getCell(0, 0));
        assertNull(board.getCell(19, 9));
    }

    @Test
    void setCellEGetCell() {
        Board board = new Board();
        board.setCell(5, 3, TetrominoType.T);
        assertEquals(TetrominoType.T, board.getCell(5, 3));
    }

    @Test
    void clearBoardEsvaziaTudo() {
        Board board = new Board();
        board.setCell(0, 0, TetrominoType.I);
        board.setCell(19, 9, TetrominoType.Z);
        board.clearBoard();
        assertNull(board.getCell(0, 0));
        assertNull(board.getCell(19, 9));
    }
}
