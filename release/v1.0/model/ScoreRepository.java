package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Persiste o ranking de pontuações em um arquivo JSON ({@code scores.json}
 * por padrão). As entradas são sempre ordenadas por pontuação decrescente.
 */
public class ScoreRepository {

    private final Path file;

    /** Cria um repositório usando o arquivo padrão {@code scores.json}. */
    public ScoreRepository() {
        this(Paths.get("scores.json"));
    }

    /**
     * Cria um repositório usando um arquivo específico.
     *
     * @param file o caminho do arquivo de ranking
     */
    public ScoreRepository(Path file) {
        this.file = file;
    }

    /**
     * Lê todas as entradas do arquivo. Devolve lista vazia se o arquivo não
     * existir ou estiver corrompido.
     *
     * @return as entradas lidas (não ordenadas)
     */
    public List<ScoreEntry> load() {
        List<ScoreEntry> result = new ArrayList<>();
        try {
            if (!Files.exists(file)) return result;
            String text = Files.readString(file, StandardCharsets.UTF_8);
            Object parsed = Json.parse(text);
            if (parsed instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof Map<?, ?> entry) {
                        String name = String.valueOf(entry.get("name"));
                        long score = toLong(entry.get("score"), 0L);
                        int level = (int) toLong(entry.get("level"), 1L);
                        result.add(new ScoreEntry(name, score, level));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler scores.json: " + e.getMessage());
        }
        return result;
    }

    /**
     * Grava a lista de entradas no arquivo, em formato JSON.
     *
     * @param scores as entradas a salvar
     */
    public void save(List<ScoreEntry> scores) {
        List<Object> array = new ArrayList<>();
        for (ScoreEntry entry : scores) {
            Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("name", entry.getName());
            map.put("score", entry.getScore());
            map.put("level", entry.getLevel());
            array.add(map);
        }
        try {
            Files.writeString(file, Json.write(array), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Erro ao salvar scores.json: " + e.getMessage());
        }
    }

    /**
     * Adiciona uma nova entrada, reordena por pontuação e persiste.
     *
     * @param entry a entrada a adicionar
     */
    public void addScore(ScoreEntry entry) {
        List<ScoreEntry> scores = load();
        scores.add(entry);
        scores.sort(Comparator.comparingLong(ScoreEntry::getScore).reversed());
        save(scores);
    }

    /**
     * Devolve as melhores pontuações, em ordem decrescente.
     *
     * @param n o número máximo de entradas
     * @return até {@code n} melhores entradas
     */
    public List<ScoreEntry> topScores(int n) {
        List<ScoreEntry> scores = load();
        scores.sort(Comparator.comparingLong(ScoreEntry::getScore).reversed());
        return new ArrayList<>(scores.subList(0, Math.min(n, scores.size())));
    }

    private long toLong(Object value, long fallback) {
        if (value instanceof Number number) return number.longValue();
        return fallback;
    }
}
