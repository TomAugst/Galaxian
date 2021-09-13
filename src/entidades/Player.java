/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import configuracoes.Gerais;
import configuracoes.Som;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import configuracoes.Sprites;

/**
 *
 * @author tata__000
 */
public class Player extends Sprites {

    //Classe responsavel pela criacao do player, seus status e suas imagens.
    //a variavel sprite armazena a img que sera renderizada pelo metodo render e a array
    //imagens é responsavel por armazenar as imagens que formam a animaçao do player,
    //ao atingir um certo numero de frames o imgIndex da animacao é alterado e a variavel
    //sprite recebe uma nova imagem imagens[imgIndex]
    private BufferedImage sprite;
    private int x;
    private int y;
    private BufferedImage imagens[];
    private int frames, maxFrames, imgIndex, index, contagemDano;
    private boolean right, left, atirou, morto, dano;
    private double speed = Gerais.XSPEED * 2;
    private Bala bala = new Bala(false, "balaPlayer"), bala2 = null;
    private int hp, maxHp, ataque, pontos = 0;
    private int vidas = 3;
    //essa variavel indica se a nave é de um tipo especial, que atira duas balas ao mesmo tempo
    private boolean robusta = false;

    public Player() {
        super("/recursos/players.png");
        imagens = new BufferedImage[3];
        frames = 0;
        maxFrames = 30;
        index = 0;
        maxHp = 100;
        hp = maxHp;
        ataque = 25;
        for (int i = 0; i < 3; i++) {
            imagens[i] = getSprite(i * 16, 0, 16, 16);
        }
        posicaoInicial();
    }

    //inicializa o palyer em uma posicao padrao
    public void posicaoInicial() {
        x = Game.WIDTH / 2 - 16;
        y = Game.HEIGHT - 36;
    }

    //reseta o player para os seus status primarios
    public void reset() {
        posicaoInicial();
        maxHp = 100;
        hp = maxHp;
        ataque = 25;
        speed = Gerais.XSPEED * 2;
    }

    //Existe um sistema de shopping onde é possivel comprar novas skins para o player.
    //Esse metodo é responsavel por realizar a troca de skins.
    //Todos as skins definidas estao construidas na classe configuracoes.Gerais e sao 
    //objetos da classe Inimigos. Como a unica coisa que precisa ser copiada sao os buffersimg, nao
    //ha problema em fazer o metodo desse modo. Para mim inicialmente nao fez sentido criar outros objetos de
    //player ja que existe apenas um player no jogo. Por esse motivo tambem nao existe uma caminho para img do
    //player passado por parametro no construtor do Player, como existe no de Inimigo, visto que a img inicial é sempre uma padrao
    public void mudarSprites(Inimigo inimigo) {
        atirou = false;
        bala.setStop(true);
        for (int i = 0; i < 3; i++) {
            imagens[i] = inimigo.getImagens()[i];
        }
        robusta = inimigo.isRobusto();
        if (robusta) {
            bala2 = new Bala(false, "balaPlayer");
            bala2.setSpeed(bala.getSpeed());
        } else {
            bala2 = null;
        }     
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprite, x, y, null);
        bala.render(g);
        if (robusta && bala2 != null) {
            bala2.render(g);
        }
    }

    //Atualiza o estado, animacoes e posicoes do player
    public void tick() {
        mover();
        animacoes();
        gerenciarBala();
        if (hp <= 0 && imgIndex == 2) {
            morto = true;
        }
    }

    //Metodo chamado pelo objeto de algum inimigo quando ela colide com o player.
    //Seu comportamento é semelhante ao metodo presente em inimigo
    public void levouDano(int dano) {
        hp -= dano;
        this.dano = true;
        contagemDano = 10;
        sprite = Gerais.pDamage.getImagens()[imgIndex];
        if (hp <= 0) {
            hp = maxHp;
            vidas--;
            if (vidas < 0) {
                hp = 0;
                explosao();
            }
        }
    }

    //Metodo que muda a posicao do player quando ele se move.
    //Os booleans right e left sao ativados pela classe principal do jogo Game quando
    //se aperta os respectivos botoes no teclado. 
    //Esse metodo tambem verifica se o player colidiu com a tela e nao permite que ele
    //ultrapasse as dimensoes da janela.
    private void mover() {
        if (right) {
            x += speed;
        }
        if (left) {
            x -= speed;
        }
        if (x < 0) {
            x = 0;
        }
        if (x + 16 > Game.WIDTH) {
            x = Game.WIDTH - 16;
        }
    }

    //Esse metodo verifica se o player atirou ou nao.
    //Caso ele tenha atirado é ativado o tick da bala ate que ela colida ou ultrapasse os
    //limites da janela.
    //Caso ele nao tenha atirado a bala acompanha o movimento do player, como se estivesse acoplada a nave
    public void gerenciarBala() {
        if (atirou) {
            if (bala.isStop()) {
                Som s = Gerais.somTiro.copia();
                s.play();
            }
            bala.setStop(false);
            bala.tick();
            if (robusta) {
                bala2.setStop(false);
                bala2.tick();
                if (bala.isStop() || bala2.isStop()) {
                    atirou = false;
                }
            } else {
                if (bala.isStop()) {
                    atirou = false;
                }
            }
        } else {
            if (!robusta) {
                bala.setX(this.x);
                switch (imgIndex) {
                    case 0:
                        bala.setY(y + 2);
                        break;
                    case 1:
                        bala.setY(y + 1);
                        break;
                    case 2:
                        bala.setY(y);
                        break;
                }
            } else {
                bala.setX(x - 5);
                bala2.setX(x + 5);
                bala.setY(y + 2);
                bala2.setY(y + 2);
            }
        }
    }

    //Responsavel pelas animacoes do player
    //Seu comportamento é semelhante ao metodo presente em inimigo
    public void animacoes() {
        contagemDano--;
        if (contagemDano <= 0) {
            dano = false;
        }
        if (frames >= maxFrames) {
            frames = 0;
            imgIndex++;
            if (imgIndex > 2) {
                imgIndex = 0;
            }
        }
        if (dano) {
            sprite = Gerais.pDamage.getImagens()[imgIndex];
        } else {
            sprite = imagens[imgIndex];
        }
        frames++;
    }
    
    //Metodo chamado caso a vida do player seja menor que 0
    //Seu comportamento é semelhante com o metodo presente em inimigo
    private void explosao() {
        for (int i = 0; i < 3; i++) {
            imagens[i] = Gerais.explosao.getImagens()[i];
        }
        imgIndex = 0;
        sprite = imagens[imgIndex];
        Som s = Gerais.somExplosao2.copia();
        s.play();
    }

    //--------------------------------------------Getters e Setters--------------------------------------------
    
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage[] getImagens() {
        return imagens;
    }

    public void setImagens(BufferedImage[] imagens) {
        this.imagens = imagens;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public int getMaxFrames() {
        return maxFrames;
    }

    public void setMaxFrames(int maxFrames) {
        this.maxFrames = maxFrames;
    }

    public int getImgIndex() {
        return imgIndex;
    }

    public void setImgIndex(int imgIndex) {
        this.imgIndex = imgIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isAtirou() {
        return atirou;
    }

    public void setAtirou(boolean atirou) {
        this.atirou = atirou;
    }

    public boolean isMorto() {
        return morto;
    }

    public void setMorto(boolean morto) {
        this.morto = morto;
    }

    public Bala getBala() {
        return bala;
    }

    public void setBala(Bala bala) {
        this.bala = bala;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp_max() {
        return maxHp;
    }

    public void setHp_max(int hp_max) {
        this.maxHp = hp_max;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getContagemDano() {
        return contagemDano;
    }

    public void setContagemDano(int contagemDano) {
        this.contagemDano = contagemDano;
    }

    public boolean isDano() {
        return dano;
    }

    public void setDano(boolean dano) {
        this.dano = dano;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public Bala getBala2() {
        return bala2;
    }

    public void setBala2(Bala bala2) {
        this.bala2 = bala2;
    }

    public boolean isRobusta() {
        return robusta;
    }

    public void setRobusta(boolean robusta) {
        this.robusta = robusta;
    }

}
