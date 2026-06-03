package model;

/**
 * Uma entrada imutável do ranking: o nome do jogador, sua pontuação e o
 * nível alcançado.
 */
public class ScoreEntry {

    private final String name;
    private final long score;
    private final int level;

    /**
     * Cria uma entrada de ranking.
     *
     * @param name  o nome do jogador
     * @param score a pontuação final
     * @param level o nível alcançado
     */
    public ScoreEntry(String name, long score, int level) {
        this.name = name;
        this.score = score;
        this.level = level;
    }

    /** @return o nome do jogador */
    public String getName() {
        return name;
    }

    /** @return a pontuação */
    public long getScore() {
        return score;
    }

    /** @return o nível alcançado */
    public int getLevel() {
        return level;
    }
}
