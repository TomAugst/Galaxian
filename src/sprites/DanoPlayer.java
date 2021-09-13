/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import main.Game;

public class DanoPlayer {
    
    //Essa classe é o sprite que é apresentado quando o player recebe algum dano
    //A principal e unica diferenca relevante em relacao a DanoInimigo esta no metodo tick(),
    //Que foi implementado para apresentar um efeito de parabola em relacao a posicao inicial do player

    private double x;
    private double y;
    private int direcao;
    private int frames = 0, maxFrames = 90;
    private double xAux = 0, yAux = -20;
    private int dano;
    private boolean disposed = false;
    private double tamanho = 15;
    private Color cor;

    public DanoPlayer(int dano) {
        x = Game.player.getX();
        y = Game.player.getY();
        this.dano = dano;
        direcao = new Random().nextInt(2);
        cor = Color.RED;
    }

    protected void tick() {
        frames++;
        tamanho -= 0.2;
        if (frames >= maxFrames) {
            disposed = true;
        }

        if (frames < maxFrames / 3 * 2) {
            if (direcao == 0) {
                xAux++;
            } else {
                xAux--;
            }
        } else {
            if (direcao == 0) {
                xAux += 0.5;
            } else {
                xAux -= 0.5;
            }
        }

        if (frames < maxFrames / 5) {
            y -= 2;
        } else if (frames < maxFrames / 5 * 3) {
            y += 2;
        } else if (frames < maxFrames / 5 * 4) {
            y--;
        } else {
            y++;
        }
    }

    protected void render(Graphics g) {
        if (disposed) {
            return;
        }
        g.setColor(cor);
        g.setFont(new Font("times new roman", Font.BOLD, (int) tamanho));
        g.drawString(String.valueOf(dano), (int) (xAux + x), (int) (yAux + y));
    }

    protected boolean isDisposed() {
        return disposed;
    }

}
