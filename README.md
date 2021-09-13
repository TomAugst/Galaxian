# Galaxian
Este foi o primeiro trabalho pratico da disciplina de computacao grafica.

<img src="https://github.com/TomAugst/Galaxian/blob/main/Screenshot/galaxian2.gif" width="500" height="300">

O objetivo foi criar um jogo baseado no antigo Galaxian. Nele, o jogador pilota uma nave que fica na parte de baixo da tela e, com ela, se defende de um ataque alienígena. Os alienígenas realizam o seu ataque como uma grande esquadra que se movimenta lateralmente na parte superior da tela. Os diversos alienígenas podem soltar bombas contra a heróica nave e, de tempos em tempos, saem da formação e mergulham individualmente ou em pequenas esquadrilhas num ataque mais agressivo contra o defensor solitário que, por sua vez, também se movimenta lateralmente esquivando-se das bombas e disparando mísseis contras os invasores alienígenas.

Na minha proposta nao implementei o modo rasante. Se o jogador elimina todos os alienígenas, uma nova fase é iniciada com um grau de dificuldade um pouco maior que a anterior. Porém, se algum alienígena conseguir chegar ao solo, então ele ativa sua arma secreta mutante e será o vencedor (game over). A movimentação dos inimigos é "em bloco" e, quando chega em um dos cantos laterais da tela, o bloco desce um pouco e começa a andar para o outro lado. Os inimigos lançam tiros eventualmente, dos quais o jogador deve desviar. Se um tiro acertar o jogador o mesmo perde pontos de hp e posteriormente o numero de vidas diminui. Caso o numero de vidas se encerre o jogo acaba.

Todo o trabalho foi feito em java.

<img src="https://github.com/TomAugst/Galaxian/blob/main/Screenshot/tile.gif" width="420" height="300"> <img src="https://github.com/TomAugst/Galaxian/blob/afe25b53357d5282aa35fda39f156de6a6fd7ca8/Screenshot/screenshot_pause.png" width="420" height="300">
<img src="https://github.com/TomAugst/Galaxian/blob/afe25b53357d5282aa35fda39f156de6a6fd7ca8/Screenshot/screenshot_levou_dano.png" width="420" height="300"> <img src="https://github.com/TomAugst/Galaxian/blob/afe25b53357d5282aa35fda39f156de6a6fd7ca8/Screenshot/screenshot_nave_robusta.png" width="420" height="300">

<a href="https://www.youtube.com/watch?v=1naGQJnH4ns">Video</a>

<a href="https://github.com/glenderbras/cefet-cg/blob/master/assignments/tp1-galaxian/README.md">Especificação do trabalho</a>

<a href="https://drive.google.com/file/d/1CsiBH_BQAgFfH7-7wAp0_oqmEUGkk-gB/view?usp=sharing">Projeto</a>
