package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Salva e carrega o estado completo de uma partida em um arquivo JSON
 * ({@code savegame.json} por padrão): tabuleiro, peça atual, próxima peça,
 * pontuação, nível e estado de fim de jogo.
 */
public class GameSave {

    private final Path file;

    /** Cria um gerenciador usando o arquivo padrão {@code savegame.json}. */
    public GameSave() {
        this(Paths.get("savegame.json"));
    }

    /**
     * Cria um gerenciador usando um arquivo específico.
     *
     * @param file o caminho do arquivo de save
     */
    public GameSave(Path file) {
        this.file = file;
    }

    /** @return {@code true} se existe um jogo salvo */
    public boolean exists() {
        return Files.exists(file);
    }

    /**
     * Grava o estado informado no arquivo de save.
     *
     * @param state o estado de jogo a salvar
     * @throws IOException se houver erro de escrita
     */
    public void save(GameState state) throws IOException {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("score", state.getScore());
        root.put("level", state.getLevel());
        root.put("gameOver", state.isGameOver());

        Board board = state.getBoard();
        List<Object> rows = new ArrayList<>();
        for (int r = 0; r < board.getRows(); r++) {
            List<Object> cols = new ArrayList<>();
            for (int c = 0; c < board.getColumns(); c++) {
                TetrominoType type = board.getCell(r, c);
                cols.add(type == null ? null : type.name());
            }
            rows.add(cols);
        }
        root.put("board", rows);

        root.put("current", tetrominoToMap(state.getCurrentTetromino()));
        root.put("next", tetrominoToMap(state.getNextTetromino()));

        Files.writeString(file, Json.write(root), StandardCharsets.UTF_8);
    }

    /**
     * Lê o arquivo de save e reconstrói o estado de jogo.
     *
     * @return o {@link GameState} carregado
     * @throws IOException se houver erro de leitura ou o arquivo for inválido
     */
    public GameState load() throws IOException {
        String text = Files.readString(file, StandardCharsets.UTF_8);
        Object parsed = Json.parse(text);
        if (!(parsed instanceof Map<?, ?> root)) {
            throw new IOException("Arquivo de save invalido.");
        }

        GameState state = new GameState();
        state.getBoard().clearBoard();

        Object boardObj = root.get("board");
        if (boardObj instanceof List<?> rows) {
            for (int r = 0; r < rows.size(); r++) {
                if (rows.get(r) instanceof List<?> cols) {
                    for (int c = 0; c < cols.size(); c++) {
                        Object cell = cols.get(c);
                        if (cell != null) {
                            state.getBoard().setCell(r, c, TetrominoType.valueOf(cell.toString()));
                        }
                    }
                }
            }
        }

        state.setCurrentTetromino(mapToTetromino(root.get("current")));
        state.setNextTetromino(mapToTetromino(root.get("next")));
        state.setScore(toLong(root.get("score"), 0L));
        state.setLevel((int) toLong(root.get("level"), 1L));
        state.setGameOver(Boolean.TRUE.equals(root.get("gameOver")));

        return state;
    }

    private Map<String, Object> tetrominoToMap(Tetromino tetromino) {
        if (tetromino == null) return null;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", tetromino.getType().name());
        map.put("x", tetromino.getPositionX());
        map.put("y", tetromino.getPositionY());

        List<Object> shape = new ArrayList<>();
        for (int[] row : tetromino.getShape()) {
            List<Object> cols = new ArrayList<>();
            for (int value : row) cols.add(value);
            shape.add(cols);
        }
        map.put("shape", shape);
        return map;
    }

    private Tetromino mapToTetromino(Object obj) {
        if (!(obj instanceof Map<?, ?> map)) return null;

        TetrominoType type = TetrominoType.valueOf(String.valueOf(map.get("type")));
        int x = (int) toLong(map.get("x"), 0L);
        int y = (int) toLong(map.get("y"), 0L);

        int[][] shape;
        if (map.get("shape") instanceof List<?> rows && !rows.isEmpty()) {
            shape = new int[rows.size()][];
            for (int r = 0; r < rows.size(); r++) {
                List<?> cols = (List<?>) rows.get(r);
                shape[r] = new int[cols.size()];
                for (int c = 0; c < cols.size(); c++) {
                    shape[r][c] = (int) toLong(cols.get(c), 0L);
                }
            }
        } else {
            shape = new int[][]{{1}};
        }

        return new Tetromino(shape, type, x, y);
    }

    private long toLong(Object value, long fallback) {
        if (value instanceof Number number) return number.longValue();
        return fallback;
    }
}
