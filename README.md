# Tetris

Jogo de Tetris em Java + Swing, desenvolvido de forma incremental em três
versões (MVP). Arquitetura em camadas (MVC): `model`, `view`, `controller`.

## Como rodar

A partir da pasta raiz do projeto:

```
javac App.java model/*.java view/*.java controller/*.java
java App
```

> Requer JDK 14 ou superior (o projeto usa `switch` com seta). No Windows,
> use `\` no lugar de `/` nos caminhos.

## Controles

- Setas esquerda/direita: mover a peça
- Seta para cima: rotacionar
- Seta para baixo: descer mais rápido
- P: pausar/retomar
- R: reiniciar (na tela de Game Over)

## Estrutura do repositório

```
.
├── App.java                 # ponto de entrada
├── model/                   # regras do jogo e persistência
├── view/                    # telas e componentes Swing
├── controller/              # controle do jogo e laço em thread
├── docs/                    # documentação Javadoc (HTML)
├── tests/                   # testes unitários JUnit 5
└── release/                 # 3 versões de MVP
    ├── v0.1/
    ├── v0.2/
    └── v1.0/
```

## Versões (release)

- **v0.1** — Tetris jogável com GUI Swing: colisão, fixação de peças e game
  over. Queda por `Timer` (sem concorrência), sem serialização.
- **v0.2** — Queda controlada por **thread** com velocidade variável por
  nível, preview da próxima peça, botão Restart e gravação das pontuações
  máximas em `scores.json`.
- **v1.0** — Tela inicial (novo jogo / carregar), salvar e carregar partida,
  pedido de nome ao fim, e tela com os 10 melhores placares.

## Persistência

- `scores.json` — ranking de pontuações (ordenado por pontuação decrescente).
- `savegame.json` — estado da partida salva (apenas v1.0).

Ambos em JSON, gerados na pasta de onde o jogo é executado. A serialização
JSON é feita por um utilitário próprio (`model/Json.java`), sem bibliotecas
externas.

## Documentação

A documentação completa (Javadoc) está em `docs/`. Abra `docs/index.html` no
navegador.

## Testes

Os testes unitários (JUnit 5) estão em `tests/`. Veja `tests/README.md` para
o passo a passo de execução.
