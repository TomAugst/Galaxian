package entidades;

import configuracoes.Gerais;
import java.awt.Graphics;
import java.util.ArrayList;
import main.Game;

public class BlocoInimigos {
    
    //Essa classe gerencia todos os inimigos presentes no jogo, agrupando em bloco. Toda a movimentacao
    //dos inimigos é feita em conjunto. 
    //Ela tambem é responsavel por verificar se um inimigo foi morto e se sim, o retira do bloco.
    //Tambem é responsavel por verificar um dos meios do jogo chegar ao fim, que é caso o bloco de inimigos
    //consiga chegar ao fim da tela do jogo.

    public static ArrayList<Inimigo> inimigos = new ArrayList<>();
    private static int xInicial;
    private static int yInicial;
    private static int xSpeed = Gerais.XSPEED;
    public static int ySpeed = Gerais.YSPEED;
    private static boolean right;
    public static int ataque = 20, pontos = 200;
    public static boolean armaSecreta = false;

    public static void adicionar(Inimigo i) {
        inimigos.add(i);
    }

    public static void reset() {
        inimigos.clear();
        ySpeed = Gerais.YSPEED;
        ataque = 20;
        pontos = 200;
        armaSecreta = false;
    }
    
    //O bloco permite apenas 10 inimigos por linha, apos esse numero uma nova 
    //coluna é adicionada no bloco
    public static void posicionar() {
        right = true;
        yInicial = 0;
        xInicial = (Game.WIDTH - (inimigos.size() * 16)) / 2;
        if (inimigos.size() > 10) {
            int linhas = inimigos.size() / 10;
            int x = inimigos.size();
            int y = 10;
            int index = 0;
            for (int i = 0; i <= linhas; i++) {
                xInicial = (Game.WIDTH - (y * 16)) / 2;
                for (int j = 0; j < y; j++, index++) {
                    inimigos.get(index).setX(xInicial + (j * 16));
                    inimigos.get(index).setY(yInicial);
                }
                x -= 10;
                if (x < 10) {
                    y = x;
                }
                yInicial += 16;
            }
        } else {
            for (int i = 0; i < inimigos.size(); i++) {
                inimigos.get(i).setX(xInicial + (i * 16));
            }
        }
    }

    public static void tick() {
        lixo();
        mover();
        for (Inimigo i : inimigos) {
            i.tick();
        }
        
    }

    public static void render(Graphics g) {
        if (inimigos.isEmpty()) {
            return;
        }
        for (Inimigo i : inimigos) {
            i.render(g);
        }
        armaSecreta = verificarArmaSecreta();
    }

    private static void mover() {
        if (inimigos.isEmpty()) {
            return;
        }
        if (right) {
            for (Inimigo i : inimigos) {
                if (!i.isDano()) {
                    i.setX(i.getX() + xSpeed);
                }
            }
            if (colisaoComTelaRight()) {
                right = false;
                for (Inimigo i : inimigos) {
                    if (!i.isDano()) {
                        i.setY(i.getY() + ySpeed);
                    }
                }
            }
        } else {
            for (Inimigo i : inimigos) {
                if (!i.isDano()) {
                    i.setX(i.getX() - xSpeed);
                }
            }
            if (colisaoComTelaLeft()) {
                right = true;
                for (Inimigo i : inimigos) {
                    if (!i.isDano()) {
                        i.setY(i.getY() + ySpeed);
                    }
                }
            }
        }

    }

    private static boolean colisaoComTelaRight() {
        for (Inimigo i : inimigos) {
            if (i.getX() + 16 >= Game.WIDTH) {
                return true;
            }
        }
        return false;
    }

    private static boolean colisaoComTelaLeft() {
        for (Inimigo i : inimigos) {
            if (i.getX() <= 0) {
                return true;
            }
        }
        return false;
    }

    private static void lixo() {
        ArrayList<Inimigo> lixo = new ArrayList<>();
        for (Inimigo i : inimigos) {
            if (i.isMorto()) {
                lixo.add(i);
                Game.player.setPontos(Game.player.getPontos() + pontos);
            }
        }
        for (Inimigo i : lixo) {
            inimigos.remove(i);
        }
        lixo.clear();
    }
    
    private static boolean verificarArmaSecreta(){
         for (Inimigo i : inimigos) {
                if (i.getY() + 16 > Game.player.getY() + 16) {
                    return true;
                }
            }
        return false;
    }

}
