Este trabalho foi feito individualmente pelo aluno Tomaz Augusto Silva Apostolos
Para a criação do projeto foi utilizado a linguagem Java e o netbeans 8.2. 
O arquivo executaval esta na pasta dist/Trabalho_Pratico_Galaxian.jar
Para que o jogo compile o caminho da pasta do projeto precisa ser "C:\Trabalho Pratico Galaxian"

Teclas:
	- ← e → controlam a nave do jogador e opcoes nos menus;
	- ⬆ e ⬇ controlam as opcoes nos menus
	- enter confirma as opcoes nos menus
	- space dá um tiro para cima;
	- clicando na tecla p, o jogo deve pausar/continuar;
	- clicando em r, a fase é reiniciada (não o jogo todo);
	- clicando em esc, chama o menu de configuracoes(contem a opcao de sair do jogo) e sai de uma opcao nos menus.
	- clicando em s, chama o menu de shopping/upgrade
	- clicando em c, chama o menu de status ("C" de "C"haracter, ja que o "S" ja estava sendo utilizado)

Funcoes adicionais do jogo:

*Animacoes:
	- Existe diversas animacoes no jogo
	- As mais marcantes sao a de explosao, e animacao com efeito de movimentacao no players e inimigos
	- Outras interessantes sao animacoes de dado(o player/inimigo fica "brilhante"), cor e deslocamento nas opcoes dos menus

*Contagem de dano:
	- Quando sao acertados é apresentado o dano causado no inimigo/player

*Fase:
	- O jogo termina se o inimigo enconstar no fim da tela ou se o player zerar o hp e vidas.
	- Caso o player mate todos os inimigos uma nova fase é iniciada.
	- A cada nova fase os status dos inimigos sao aumentados para aumentar a dificuldade e forcar a compra de upgrades

*Hud:
	- Apresenta a porcentagem de hp e vida do player
	- Apresenta o numero de pontos que o player tem
	- Apresenta o numero de inimigos que resta para o fim da fase
	- Apresenta o numero da fase em que o player esta

*Background:
	- No menu de titulo e na janela principal de jogo o fundo se alterna entre tres imagens simples criadas por mim

*Player:
	- O player nao morre automaticamente, possui uma quantidade de hp e um numero de vida.
	- O player pode utilizar uma skin da sua escolha dentre as disponiveis
	- É possivel utilizar um tipo de nave mais "robusta" que atira duas balas ao mesmo tempo

*Inimigos:
	- Sao gerenciados em bloco
	- O numero de inimigos no bloco é aumentado em cada fim de fase
	- Possuem hp que é aumentado de acordo com a progressao de jogo
	- Possuem ataque que é aumentado de acordo com a progressao do jogo
	- Cada vez que um morre é adicionado uma certa quantia de pontos para o player. Tambem é aumentada com a progressao do jogo
	- Os inimigos possuem comportamentos diferentes em relacao a hora de atirar e podem variar entre 3 cores
	- O bloco permite apenas 10 inimigos por linha, apos esse numero uma nova coluna é adicionada no bloco
	- A velocidade com que o bloco se aproxima do fim da tela é aumentada a cada fase

*Menu de titulo:
	- Menu inicial com as opcoes de novo jogo, configuracoes e sair.

*Som:
	- Som quando navega nos menus criados
	- Sons de tiros e explosoes.
	- Som para contagem regressiva.
	- Musica de fundo

*Menu de configuracoes:
	- alterar configuracoes do player(apenas a sua skins)
	- alterar o modo de jogo (competitivo: a frequencia de tiro dos inimigos é aumentada gradativamente de acordo com a progressao do jogo)
	- alterar a musica de fundo do jogo.
	- opcao de sair do jogo

*Menu de shopping:
	- comprar novas skins para a nave
	- comprar upgrades para facilitar na progressao de jogo
	**opcaoes de upgrades:
		- ataque: cada compra aumenta o ataque em 2
		- speed: cada compra aumenta a velocidade do player em 0.3
		- tiro: cada compra aumenta a velocidade do tiro em 0.3
		- hpMax: cada compra aumenta o hpmax em 10
		- vidas: cada compra aumenta o numero de vidas em 1

*Menu de status:
	- Menu simples que mostra os dados do player e inimigos.
	- Serve para consultas e auxilia na hora de comprar os ups

**dificuldades:
	- Nao consegui resolver um bug do som que faz com que o som trave quando varios sons sao tocados ao mesmo tempo
	- Nao consegui resolver um pequeno bug que faz com que so seja possivel usar o teclado para jogar e nos menus se voce clicar na tela antes

***dicas:
	- Foque o gasto de pontos em ataque pois o hpmax dos inimigos aumenta a medida que o jogo vai se progredindo
	- Foque tambem em tiro para aumentar a frequencia de tiro/s, o numero de inimigos aumenta a medida que o jogo vai se progredindo
	- tambem é interessante guardar e comprar uma nave mais robusta

****creditos:
	- Com excecao das fontes, dos efeitos sonoros e dos templetes usados para a animacao e img dos player, inimigos e explosoes, todos os recursos foram feitos por mim
	- Os templetes foram retirados do site: https://www.spriters-resource.com/arcade/galaxian/sheet/78617/
	- Os efeitos sonoros foram retirados do rtp do rpg maker: https://www.rpgmakerweb.com/
	- Fonte Over There: https://www.dafont.com/pt/over-there.font
	- Fonte Sega: https://www.dafont.com/pt/sega.font