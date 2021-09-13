/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import entidades.BlocoInimigos;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import main.Game;

public class Hud {
    
    //Essa classe apresenta informacoes sobre o player.
    //Ela apresenta o seu hp, numero de vidas, a fase em que o jogador esta,
    //o numnero de inimigos presentes e o numero de pontos

    private final int x;
    private final int y;
    private final int widthBar = 50;
    private final int heightBar = 5;
    private int hp, maxHp;

    public Hud(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        background(g);
        hpBar(g);
        textos(g);
    }

    private void background(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, Game.WIDTH, 20);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, Game.WIDTH - 1, 20 - 1);
    }

    private void hpBar(Graphics g) {
        hp = Game.player.getHp();
        maxHp = Game.player.getHp_max();
        g.setColor(Color.BLACK);
        g.fillRect(x + 20, y + 5, widthBar, heightBar);
        double perc = (double) hp / (double) maxHp * 100;
        double widthBar2 = perc / 100 * (double) widthBar;
        if (perc > 80) {
            g.setColor(Color.GREEN);
        } else if (perc > 50 && perc <= 80) {
            g.setColor(Color.YELLOW);
        } else if (perc > 20 && perc <= 50) {
            g.setColor(Color.ORANGE);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x + 20, y + 5, (int) widthBar2, heightBar);
    }

    private void textos(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("", Font.BOLD, 5));
        g.drawString("HP: ", x + 10, y + 10);        
        g.setFont(new Font("", Font.BOLD, 8));
        for (int i = 0; i < Game.player.getVidas(); i++) {
            g.drawString("❤", x + 10 + (i * 8), y + 18);
        }
        g.drawString("FASE: " + String.valueOf(Game.fase + 1), x + 100, y + 12);
        g.drawString("PONTOS: " + String.valueOf(Game.player.getPontos()), x + 170, y + 12);
        g.drawString("INIMIGOS: " + String.valueOf(BlocoInimigos.inimigos.size()), x + 250, y + 12);
    }

}
