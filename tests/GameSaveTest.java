import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class GameSaveTest {

    @TempDir
    Path tmp;

    @Test
    void salvarECarregarPreservaEstado() throws Exception {
        Path file = tmp.resolve("save.json");
        GameState gs = new GameState();
        gs.initGame();
        gs.setScore(34000);
        gs.setLevel(4);
        gs.getBoard().setCell(19, 0, TetrominoType.T);
        gs.getBoard().setCell(19, 1, TetrominoType.S);
        gs.getBoard().setCell(18, 5, TetrominoType.I);

        GameSave save = new GameSave(file);
        assertFalse(save.exists());
        save.save(gs);
        assertTrue(save.exists());

        GameState loaded = save.load();
        assertEquals(34000, loaded.getScore());
        assertEquals(4, loaded.getLevel());
        assertEquals(TetrominoType.T, loaded.getBoard().getCell(19, 0));
        assertEquals(TetrominoType.S, loaded.getBoard().getCell(19, 1));
        assertEquals(TetrominoType.I, loaded.getBoard().getCell(18, 5));
        assertNull(loaded.getBoard().getCell(0, 0));
        assertNotNull(loaded.getCurrentTetromino());
        assertNotNull(loaded.getNextTetromino());
        assertEquals(gs.getCurrentTetromino().getType(), loaded.getCurrentTetromino().getType());
        assertEquals(gs.getCurrentTetromino().getPositionX(), loaded.getCurrentTetromino().getPositionX());
    }
}
