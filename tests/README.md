# Testes Unitários (JUnit 5)

Cobrem as classes de lógica e persistência: `Board`, `Tetromino`,
`TetrominoFactory`, `GameState`, `Json`, `ScoreEntry`, `ScoreRepository` e
`GameSave`. As classes de `view` são interface gráfica (Swing) e não são
testadas unitariamente.

## Como rodar

1. Baixe o JUnit Platform Console Standalone (jar único) e coloque na raiz do
   projeto:

   https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar

2. Compile o projeto e os testes (a partir da raiz):

   ```
   javac App.java model/*.java view/*.java controller/*.java
   javac -cp ".:junit-platform-console-standalone-1.10.2.jar" -d out-tests tests/*.java
   ```

   No Windows, troque `:` por `;` e `/` por `\`:

   ```
   javac App.java model\*.java view\*.java controller\*.java
   javac -cp ".;junit-platform-console-standalone-1.10.2.jar" -d out-tests tests\*.java
   ```

3. Execute:

   ```
   java -jar junit-platform-console-standalone-1.10.2.jar -cp ".:out-tests" --scan-classpath
   ```

   (Windows: `-cp ".;out-tests"`)
