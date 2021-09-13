package entidades;

import configuracoes.Gerais;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.Game;
import sprites.DanoInimigo;
import sprites.DanoPlayer;
import sprites.GerenciarSpritesDano;
import configuracoes.Sprites;

public class Bala extends Sprites {
    
    //Classe responsavel pelo tiro que as entidades do jogo dao. Opitei por utilizar uma so classe para a municao
    //do player e dos inimigos. O boolean inimigo verifica se a bala foi atirada pelo player ou pelo inimigo
    //O metodo tick é responsavel por atualizar a posicao da bala e por chamar o metodo que verifica se ouve colisao com
    //alguma das entidades. Tambem verifica se ela nao colidiu com ninguem e se esta fora do cenario. Quando qualquer 
    //um dos casos acontece, o boolean stop é ativado. A entidade que atirou le esse boolean, é ele que permite que ela
    //possa atirar denovo.
    //o metodo render é responsavel pela renderizacao da img da bala.

    private BufferedImage sprite;
    private int x;
    private int y;
    private double speed = Gerais.BALASPEED;
    private boolean inimigo, stop = true;

    public Bala(boolean inimigo, String s) {
        super("/recursos/" + s + ".png");

        sprite = getSprite(0, 0, 16, 3);
        this.inimigo = inimigo;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(sprite, x, y, null);
    }

    public void tick() {
        if (inimigo) {
            y += speed;
            if (y > Game.player.getY() + 16) {
                stop = true;
            }
        } else {
            y -= speed;
            if (y <= 0) {
                stop = true;
            }
        }
        colisao();
    }

    private void colisao() {
        Rectangle rect = new Rectangle(), rect2 = new Rectangle();
        rect.setBounds(x + 9, y, 1, 3);
        if (inimigo) {
            rect2.setBounds(Game.player.getX(), Game.player.getY(), 16, 16);
            if (calculoDeColisao(rect, rect2)) {
                stop = true;
                GerenciarSpritesDano.addDanoPlayer(new DanoPlayer(BlocoInimigos.ataque));
                Game.player.levouDano(BlocoInimigos.ataque);
            }
        } else {
            for (Inimigo i : BlocoInimigos.inimigos) {
                if (i.isDano()) {
                    continue;
                }
                rect2.setBounds(i.getX(), i.getY(), 16, 16);
                if (calculoDeColisao(rect, rect2)) {
                    stop = true;
                    GerenciarSpritesDano.addDanoInimigos(new DanoInimigo(i, String.valueOf(Game.player.getAtaque())));
                    i.levouDano(Game.player.getAtaque());
                }
            }

        }
    }
    
    private boolean calculoDeColisao(Rectangle rect1, Rectangle rect2){
        if (rect1.x + rect1.width > rect2.x && rect1.x + rect1.width < rect2.x + rect2.width + rect1.width) {
            if (inimigo) {
                if (rect1.y + rect1.height > rect2.y) {
                    return true;
                }
            }else{
                if (rect1.y < rect2.y + rect2.height) {
                    return true;
                }
            }
        }
        return false;
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isInimigo() {
        return inimigo;
    }

    public void setInimigo(boolean inimigo) {
        this.inimigo = inimigo;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

}
