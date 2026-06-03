import model.Json;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @Test
    void escreveELeObjetoSimples() {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("name", "Joao");
        m.put("score", 12000L);
        m.put("level", 3);
        Object back = Json.parse(Json.write(m));
        assertTrue(back instanceof Map);
        Map<?,?> bm = (Map<?,?>) back;
        assertEquals("Joao", bm.get("name"));
        assertEquals(12000L, ((Number) bm.get("score")).longValue());
        assertEquals(3, ((Number) bm.get("level")).intValue());
    }

    @Test
    void preservaEscapesEAcentos() {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("v", "Joao \"Z\"\n\tçãé");
        Map<?,?> bm = (Map<?,?>) Json.parse(Json.write(m));
        assertEquals("Joao \"Z\"\n\tçãé", bm.get("v"));
    }

    @Test
    void numerosNegativosEBooleanosENull() {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("neg", -7);
        m.put("flag", true);
        m.put("nil", null);
        Map<?,?> bm = (Map<?,?>) Json.parse(Json.write(m));
        assertEquals(-7, ((Number) bm.get("neg")).intValue());
        assertEquals(Boolean.TRUE, bm.get("flag"));
        assertNull(bm.get("nil"));
    }

    @Test
    void arraysAninhados() {
        List<Object> shape = new ArrayList<>();
        shape.add(Arrays.asList(0, 1, 0));
        shape.add(Arrays.asList(1, 1, 1));
        Object back = Json.parse(Json.write(shape));
        assertTrue(back instanceof List);
        assertEquals(2, ((List<?>) back).size());
        assertEquals(3, ((List<?>) ((List<?>) back).get(0)).size());
    }

    @Test
    void listaVaziaEObjetoVazio() {
        assertEquals(0, ((List<?>) Json.parse("[]")).size());
        assertEquals(0, ((Map<?,?>) Json.parse("{}")).size());
    }
}
