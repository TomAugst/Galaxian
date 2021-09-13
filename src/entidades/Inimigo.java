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
import java.util.Random;
import configuracoes.Sprites;

/**
 *
 * @author tata__000
 */
public class Inimigo extends Sprites {
    
    //Classe responsavel pela criacao dos inimigosm seus status e suas imagens.
    //a variavel sprite armazena a img que sera renderizada pelo metodo render e a array
    //imagens é responsavel por armazenar as imagens que formam a animaçao do inimigo,
    //ao atingir um certo numero de frames o imgIndex da animacao é alterado e a variavel
    //sprite recebe uma nova imagem imagens[imgIndex]

    private BufferedImage sprite;
    private int x;
    private int y;
    private BufferedImage imagens[];
    private int frames, maxFrames, imgIndex, index, contagemDano;
    private String path;
    private Bala bala;
    private boolean atirou, morto, dano, levouDano;
    private int hp, maxHp;
    private boolean robusto = false;

    public Inimigo(String path, int index) {
        super(path);
        this.bala = new Bala(true, "balaInimigo");
        this.index = index;
        this.path = path;
        this.x = 0;
        this.y = 0;
        this.imgIndex = new Random().nextInt(3);
        this.maxFrames = 20;
        this.frames = 0;
        maxHp = 50;
        hp = maxHp;
        imagens = new BufferedImage[3];
        for (int i = 0; i < 3; i++) {
            imagens[i] = getSprite(i * 16, index * 16, 16, 16);
        }
    }

    public Inimigo clone() {
        Inimigo i = new Inimigo(path, index);
        return i;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprite, x, y, null);
        if (atirou) {
            bala.render(g);
        }
    }
    
    //Atualiza os estados do inimigo
    public void tick() {
        if (dano && imgIndex == 2) {
            morto = true;
        } else {
            animacao();
            atirar();
        }
    }
    
    //Metodo que verifica se o inimigo atirou ou nao
    //Para que o inimigo atire é preciso que ele esteja com a img
    //que representa o inimigo atirando e que a a porcentagem que representa
    //a chance do inimigo atirar seja atingida. Essa porcentagem é aumentada frequentemente
    //caso o modoCompetitivo seja ativado
    //Ele so pode atiar denovo caso a bala tenha colidido com o player ou caso ela tenha
    //ultrapassado o limite da janela do jogo
    private void atirar() {
        if (!atirou) {
            if (imgIndex == 2) {
                if (new Random().nextInt(100) < Gerais.FREQUENCIADETIRO) {
                    atirou = true;
                    Som s = Gerais.somTiro.copia();
                    s.play();
                    bala.setY(y + 16);
                    bala.setX(x);
                }
            }
        } else {
            bala.setStop(false);
            bala.tick();
            if (bala.isStop()) {
                bala.setY(0);
                bala.setX(0);
                atirou = false;
            }
        }

    }
    
    //metodo responsavel pela animacao do inimigo
    public void animacao() {
        contagemDano--;
        if (contagemDano <= 0) {
            levouDano = false;
        }
        if (frames >= maxFrames) {
            frames = 0;
            imgIndex++;
            if (imgIndex > 2) {
                imgIndex = 0;
            }
        }
        if (levouDano) {
            sprite = Gerais.iDamage.getImagens()[imgIndex];
        } else {
            sprite = imagens[imgIndex];
        }
        frames++;
    }
    
    //metodo chamado quando o inimigo levou dano. É chamado belo objeto bala presente no Player
    //Existe uma img presente no configuracoes.Gerais que é mostrada sempre que o inimigo leva dano
    //O tempo que essa img fica a mostrar é definido pela variavel contagemDano e so é mostrada caso a
    //variavel levouDano esteja ativada.
    //Caso o hp do inimigo seja <= 0, o metodo explosao é chamado.
    public void levouDano(int dano) {
        hp -= dano;
        levouDano = true;
        contagemDano = 10;
        sprite = Gerais.iDamage.getImagens()[imgIndex];
        if (hp <= 0) {
            explosao();
        }
    }
    
    //Metodo responsavel pela animacao de explosao no inimigo e ativar o boolean dano,
    //que indica que o inimigo esta explodindo. o metodo tick verifica se o inimigo
    //esta explodindo e depois que todas as imgs de explosao sao apresentadas (quando o imgIndex == 2)
    //o tick muda o estado do inimigo para morto.
    private void explosao() {
        for (int i = 0; i < 3; i++) {
            imagens[i] = Gerais.explosao.getImagens()[i];
        }
        imgIndex = 0;
        sprite = imagens[imgIndex];
        dano = true;
        Som s = Gerais.somExplosao2.copia();
        s.play();
    }
    
    //----------------------------------------Getters e Setters-----------------------------------------------------

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bala getBala() {
        return bala;
    }

    public void setBala(Bala bala) {
        this.bala = bala;
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

    public boolean isDano() {
        return dano;
    }

    public void setDano(boolean dano) {
        this.dano = dano;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getContagemDano() {
        return contagemDano;
    }

    public void setContagemDano(int contagemDano) {
        this.contagemDano = contagemDano;
    }

    public boolean isLevouDano() {
        return levouDano;
    }

    public void setLevouDano(boolean levouDano) {
        this.levouDano = levouDano;
    }

    public boolean isRobusto() {
        return robusto;
    }

    public void setRobusto(boolean robusto) {
        this.robusto = robusto;
    }

}
