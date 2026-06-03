# Tetris v0.1

tetris jogável com interface Swing: detecção de colisão, fixação de peças e
game over (quando uma nova peça colide ao nascer). Queda controlada por
`javax.swing.Timer` em velocidade fixa — sem concorrência explícita e sem
serializaçao.

 pra rodar:
```
javac App.java model/*.java view/*.java controller/*.java
java App
```
