import model.ScoreEntry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreEntryTest {

    @Test
    void getters() {
        ScoreEntry e = new ScoreEntry("Ana", 9000L, 2);
        assertEquals("Ana", e.getName());
        assertEquals(9000L, e.getScore());
        assertEquals(2, e.getLevel());
    }
}
