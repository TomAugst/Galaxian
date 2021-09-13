/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import entidades.Inimigo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author tata__000
 */
public class DanoInimigo {
    
    //Essa classe é o sprite que é apresentado quando o inimigo recebe algum dano
    
    private int x;
    private int y;
    private Inimigo i;
    private int frames, maxFrames = 15;
    private boolean disposed;
    private String dano;
    private int tamanhoFonte = 8;

    public DanoInimigo(Inimigo i, String dano) {
        this.i = i;
        this.dano = dano;
    }
    
    protected void render(Graphics g){
        if(disposed)return;
        g.setColor(Color.GREEN);
        g.setFont(new Font("", Font.BOLD, tamanhoFonte));
        x = i.getX() + 4;
        y = i.getY() + 10;
        g.drawString(dano, x, y);
        tamanhoFonte += 0.4;
    }
    
    protected void tick(){
        if (frames >= maxFrames) {
            disposed = true;
            return;
        }
        frames++;
    }

    protected boolean isDisposed() {
        return disposed;
    }
    
    
    
    
    
    
    
}
