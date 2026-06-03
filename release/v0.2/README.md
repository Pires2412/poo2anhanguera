# Tetris v0.2

acrescenta ao v0.1:
- queda controlada por uma **thread** dedicada (`GameLoop`), com velocidade
  que aumenta conforme o nível.
- preview da próxima peça.
- botao Restart.
- gravação das pontuações máximas em `scores.json` ao fim da partida.

rodar:
```
javac App.java model/*.java view/*.java controller/*.java
java App
```
