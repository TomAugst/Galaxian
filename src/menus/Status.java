/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import configuracoes.Gerais;
import entidades.BlocoInimigos;
import entidades.Inimigo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import main.Game;
import main.JStatus;

/**
 *
 * @author tata__000
 */
public class Status {

    private String status[];
    private int statusPlayer[], statusInimigos[];
    private int index, frames, maxFrames = 30;
    private int width = JStatus.WIDTH;
    private int height = JStatus.HEIGHT;
    private Inimigo inimigo;
    //private BufferedImage inimigoImg;

    public Status() {
        status = new String[]{"Ataque", "Velocidade", "Tiro", "HPMax", "Vidas", "Pontos"};
        statusPlayer = new int[]{Game.player.getAtaque(), (int)Game.player.getSpeed(), 
            (int)Game.player.getBala().getSpeed(), Game.player.getHp_max(), Game.player.getVidas(), 0};
        statusInimigos = new int[]{BlocoInimigos.ataque,BlocoInimigos.ySpeed, Gerais.BALASPEED, 
            BlocoInimigos.inimigos.get(0).getMaxHp(), 0, BlocoInimigos.pontos};
        System.out.println(statusInimigos.length);
        switch (new Random().nextInt(3)) {
            case 1:
                inimigo = Gerais.inimigo1;
                break;
            case 2:
                inimigo = Gerais.inimigo2;
                break;
            default:
                inimigo = Gerais.inimigo3;
                break;
        }
    }

    public void render(Graphics g) {
        backGround(g);
        player(g);
        inimigo(g);
    }

    public void tick() {
        frames++;
        if (frames > maxFrames) {
            index++;
            frames = 0;
            if (index > 2) {
                index = 0;
            }
        }
    }

    private void backGround(Graphics g) {
        int x = 15;
        int y = 25;
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setFont(new Font("", Font.PLAIN, 10));
        for (int i = 0; i < status.length; i++) {
            g.drawString(status[i], x, (i * y) + 80);
        }
    }

    private void player(Graphics g) {
        int x = 140;
        int y = 25;
        g.drawImage(Game.player.getImagens()[index], 120, -2, 50, 50, null);
        for (int i = 0; i < statusPlayer.length; i++) {
            g.setColor(Color.BLUE);
            String s = String.valueOf(statusPlayer[i]);
            if (i == 1) {
                s = "◀ ".concat(s).concat(" ▶");
            }
            if (i == statusPlayer.length - 1) {
                g.setColor(Color.BLACK);
                s = "////";
            }
            g.drawString(s, x, (i * y) + 80);
        }
    }

    private void inimigo(Graphics g) {
        int x = 270;
        int y = 25;
        g.drawImage(inimigo.getImagens()[index], 250, 3, 50, 50, null);
        g.setColor(Color.BLUE);
        for (int i = 0; i < statusInimigos.length; i++) {
            g.setColor(Color.BLUE);
            String s = String.valueOf(statusInimigos[i]);
            if (i == 1) {
                s = "↕ ".concat(s).concat(" ↕");
            }
            if (i == statusPlayer.length - 2) {
                g.setColor(Color.BLACK);
                s = "////";
            }
            g.drawString(s, x, (i * y) + 80);
        }
    }

}
