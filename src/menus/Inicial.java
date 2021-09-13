/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import configuracoes.Gerais;
import entidades.Inimigo;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Titulo;

/**
 *
 * @author tata__000
 */
public class Inicial {
    
    //Essa é a classe que dita o que sera mostrado pela janela
    //de titulo.

    private Inimigo p1, p2, p3, p4;
    private ArrayList<Inimigo> naves = new ArrayList<>();
    private int speeds[] = new int[4];
    private boolean voar, up = false, down = false, changeColor;
    private int frames, maxFrames, framesVoar, maxFramesVoar, indexCursor = 2;
    private int colorCursorMin = 120, colorCursorMax = 255, color = 120;
    private Font sega, over;

    public Inicial() {
        naves.add(new Inimigo("/recursos/players.png", 0));
        naves.add(new Inimigo("/recursos/players.png", 1));
        naves.add(new Inimigo("/recursos/players.png", 2));
        naves.add(new Inimigo("/recursos/players.png", 3));
        posicionar();
        inicializarFontes();
    }

    private void inicializarFontes() {
        try {
            sega = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/sega.ttf"));
            over = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/over.ttf"));
        } catch (FontFormatException ex) {
            Logger.getLogger(Titulo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Titulo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Posicao inicial das naves na animacao da janela
    
    private void posicionar() {
        frames = 0;
        maxFrames = 30;
        maxFramesVoar = 60;
        framesVoar = 0;
        voar = false;
        for (int i = 0; i < 4; i++) {
            naves.get(i).setX(i * 96 + 8);
            naves.get(i).setY(Titulo.HEIGHT);
            speeds[i] = new Random().nextInt(3) + 3;
        }
    }
    
    //Movimenta as naves.
    //A idade é criar um tipo de corrida. Inicialmente as nasves estao para as suas posicoes na linha
    //de largada (!voar). Quando da o sinal de partida (voar = true) elas se movimentam de acordo com
    //uma velocidade aleatoria previamente estabelecida no metodo posicionar()
    private void mover() {
        if (!voar) {
            frames++;
            for (Inimigo i : naves) {
                i.setY(i.getY() - 1);
            }
            if (frames >= maxFrames) {
                voar = true;
            }
        } else {
            framesVoar++;
            if (framesVoar >= maxFramesVoar) {
                for (int i = 0; i < 4; i++) {
                    naves.get(i).setY(naves.get(i).getY() - speeds[i]);
                }
            }
        }
    }

    //Quando a ultima nave(ultimo colocado) ultrapaca as dimencoes da tela, a corrida recomeca.
    private void reset() {
        boolean verificar = true;
        for (Inimigo i : naves) {
            if (!(i.getY() < -16)) {
                verificar = false;
            }
        }
        if (verificar) {
            posicionar();
        }
    }
    
    //Atualiza as posicoes das naves e suas animacoes.
    public void tick() {
        colorCursor();
        for (Inimigo i : naves) {
            i.animacao();
        }
        mover();
        reset();
    }
    
    //Animacao da cor do Cursor na selecao de opcoes na tela inicial
    private void colorCursor(){
        if (changeColor) {
            color += 5;
            if (color >= colorCursorMax) {
                changeColor = false;
            }
        } else {
            if (color <= colorCursorMin) {
                changeColor = true;
            }
            color -= 5;
        }
        
    }

    public void render(Graphics g) {
        for (Inimigo i : naves) {
            i.render(g);
        }
        g.drawImage(Gerais.imgTitulo, 0, 0, null);
        menus(g);
    }
       
    //Desenha as opcoes do menu inicial. Quando o cursor esta posicionado no index de alguma opcao ela é 
    //levemente deslocada para a direita.
    //O deslocamento é maior quando a opcao é "Sair" (indexCursor == 0)
    private void menus(Graphics g) {
        String s[] = {"Sair", "Configuracoes", "Novo Jogo"};
        sega = sega.deriveFont(Font.PLAIN, 8);
        g.setFont(sega);      
        double x, y;
        for (int i = 0; i < 3; i++) {
            g.setColor(Color.BLUE);
            x = (320 / 2) - (6 * 13 / 2);
            y = Titulo.HEIGHT - (i * 8 + 15 + 10 * i);
            if (indexCursor == i) {
                if (indexCursor == 0) {
                    x = (320 / 2) - (6 * s[i].length() / 2);
                }else{
                    x += 15;
                }
                
                g.setColor(new Color(color, color, color));
            }         
            
            g.drawString(s[i], (int)x, (int)y);
//            if (indexCursor == i) {
//                g.setColor(new Color(color, color, color));
//                g.drawRect((int)x - 2, (int)y - 10, s[i].length() * 6, 12);
//            }
        }
    }

    public Inimigo getP1() {
        return p1;
    }

    public void setP1(Inimigo p1) {
        this.p1 = p1;
    }

    public Inimigo getP2() {
        return p2;
    }

    public void setP2(Inimigo p2) {
        this.p2 = p2;
    }

    public Inimigo getP3() {
        return p3;
    }

    public void setP3(Inimigo p3) {
        this.p3 = p3;
    }

    public Inimigo getP4() {
        return p4;
    }

    public void setP4(Inimigo p4) {
        this.p4 = p4;
    }

    public ArrayList<Inimigo> getNaves() {
        return naves;
    }

    public void setNaves(ArrayList<Inimigo> naves) {
        this.naves = naves;
    }

    public int[] getSpeeds() {
        return speeds;
    }

    public void setSpeeds(int[] speeds) {
        this.speeds = speeds;
    }

    public boolean isVoar() {
        return voar;
    }

    public void setVoar(boolean voar) {
        this.voar = voar;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
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

    public int getFramesVoar() {
        return framesVoar;
    }

    public void setFramesVoar(int framesVoar) {
        this.framesVoar = framesVoar;
    }

    public int getMaxFramesVoar() {
        return maxFramesVoar;
    }

    public void setMaxFramesVoar(int maxFramesVoar) {
        this.maxFramesVoar = maxFramesVoar;
    }

    public int getIndexCursor() {
        return indexCursor;
    }

    public void setIndexCursor(int indexCursor) {
        this.indexCursor = indexCursor;
    }

    public Font getSega() {
        return sega;
    }

    public void setSega(Font sega) {
        this.sega = sega;
    }

    public Font getOver() {
        return over;
    }

    public void setOver(Font over) {
        this.over = over;
    }
    
    
}
