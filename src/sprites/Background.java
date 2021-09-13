/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import configuracoes.Gerais;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author tata__000
 */
public class Background{
    
    //Classe pequena que gerencia a animacao do background do jogo
    //A animacao foi feita da mesma forma que na do player e inimigo

    BufferedImage background;
    BufferedImage fundos[] = new BufferedImage[3];
    private int frames, maxFrames = 30, index;
    
    public Background() {
        fundos[0] = Gerais.fundo1;
        fundos[1] = Gerais.fundo2;
        fundos[2] = Gerais.fundo3;      
    }

    public void render(Graphics g) {
        g.drawImage(background, 0, 0, null);
    }
    
    public void tick(){
        frames++;
        if (frames >= maxFrames) {
            index++;
            if (index > 2) {
                index = 0;
            }
            frames = 0;
        }
        background = fundos[index];
    }
    
}
