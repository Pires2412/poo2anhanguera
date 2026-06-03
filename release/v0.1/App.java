import controller.*;

/**
 * Ponto de entrada da aplicação. Apenas instancia o {@link controller.GameController},
 * que cuida de exibir a tela inicial e iniciar o jogo.
 */
public class App {

    /**
     * Inicia o jogo Tetris.
     *
     * @param args não utilizado
     * @throws Exception se a inicialização falhar
     */
    public static void main(String[] args) throws Exception {
        new GameController();
    }
}
