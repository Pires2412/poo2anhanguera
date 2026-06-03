import model.ScoreEntry;
import model.ScoreRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ScoreRepositoryTest {

    @TempDir
    Path tmp;

    @Test
    void repositorioNovoVemVazio() {
        ScoreRepository repo = new ScoreRepository(tmp.resolve("s.json"));
        assertTrue(repo.load().isEmpty());
        assertTrue(repo.topScores(10).isEmpty());
    }

    @Test
    void adicionaOrdenaDecrescenteEPersiste() {
        Path file = tmp.resolve("s.json");
        ScoreRepository repo = new ScoreRepository(file);
        repo.addScore(new ScoreEntry("Ana", 5000, 1));
        repo.addScore(new ScoreEntry("Bia", 20000, 3));
        repo.addScore(new ScoreEntry("Caio", 12000, 2));

        List<ScoreEntry> top = repo.topScores(10);
        assertEquals(20000, top.get(0).getScore());
        assertEquals(12000, top.get(1).getScore());
        assertEquals(5000, top.get(2).getScore());

        // persistencia: novo repo lendo o mesmo arquivo
        assertEquals(3, new ScoreRepository(file).load().size());
    }

    @Test
    void topScoresLimita() {
        Path file = tmp.resolve("s.json");
        ScoreRepository repo = new ScoreRepository(file);
        for (int i = 0; i < 15; i++) repo.addScore(new ScoreEntry("P" + i, i * 100, 1));
        assertEquals(10, repo.topScores(10).size());
    }
}
