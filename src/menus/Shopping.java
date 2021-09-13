/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import configuracoes.Gerais;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Game;
import main.Shop;
import main.Titulo;

public class Shopping {
    
    //Essa é a classe que dita o que sera mostrado pela janela
    //de configuracoes.
    //O comportamento dos metodos é semelhante aos do menus.Config com excecao de poucos
    //metodos que estao comentados abaixo

    private int compras = 0;
    private int width = Shop.WIDTH;
    private int height = Shop.HEIGHT;
    private Font sega, over;
    private String opcoes[], upgrades[], upgrades2[];
    private int upgrades3[];
    private int index = 0, indexUp, indexNaves;
    private int colorCursorMin = 120, colorCursorMax = 255, color = 120;
    private boolean changeColor, upsOn = false, navesOn = false, menuOn = true;

    public Shopping() {
        inicializarFontes();
        opcoes = new String[]{"Naves", "Upgrades", "Fechar"};
        upgrades = new String[]{"Ataque", "Velocidade", "Tiro", "HPMax", "Vidas"};
        upgrades2 = new String[]{"+3 ▶", "+0.3 ▶", "+0.3 ▶", "+10 ▶", "+1 ▶"};
        upgrades3 = new int[5];
    }

    public void render(Graphics g) {
        backGround(g);
        slots(g);
        if (index == 0) {
            opcaoNaves(g);
        }
        if (index == 1) {
            opcaoUps(g);
        }
    }

    public void tick() {
        colorCursor();
    }

    private void backGround(Graphics g) {
        //g.drawImage(Gerais.configs, 0, 0, null);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        g.drawRect(0, 0, 76, height);
        g.drawRect(0, 0, 76, 48);
        g.drawRect(0, 48, 76, 48);
        g.drawRect(0, 96, 76, 48);
    }

    private void slots(Graphics g) {
        sega = sega.deriveFont(Font.PLAIN, 8);
        g.setFont(sega);
        String s;
        for (int i = 0; i < 3; i++) {
            int x = 10;
            g.setColor(Color.BLUE);
            if (index == i) {
                g.setColor(new Color(color, color, color));
                x += 10;
            }
            g.drawString(opcoes[i], x, (i * 48) + 28);
        }
        s = "R$" + String.valueOf(Game.player.getPontos()) + ",00";
        g.setFont(new Font("", Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        g.drawString(s, 5, height - 10);

    }

    private void inicializarFontes() {
        try {
            sega = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/sega.ttf"));
            over = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/over.ttf"));
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Titulo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void opcaoUps(Graphics g) {
        String s;
        int valores[] = valores();
        g.setFont(new Font("", Font.PLAIN, 10));
        int x = 101;
        int y = 48;
        for (int i = 0; i < upgrades.length; i++) {
            g.setColor(Color.BLACK);
            if (indexUp == i && upsOn) {
                g.setColor(new Color(color, color, color));
            }
            g.drawString(upgrades[i], x, (i * y) + 28);
            g.drawString(upgrades2[i], x + 80, (i * y) + 28);
            g.drawString(String.valueOf(upgrades3[i]), x + 120, (i * y) + 28);
            g.drawString("R$" + String.valueOf(valores[i]), x + 140, (i * y) + 28);
            y = 24;
        }
        g.setColor(Color.BLACK);
        s = "Total: R$" + String.valueOf(compras) + ",00";
        double aux = s.length() * 0.75 * 10;
        g.drawString(s, width - (int) aux, Shop.HEIGHT - 10);
    }
    
    //Chamado na classe main.Shop.
    //Aumenta o numero de ups que quer comprar de uma unica opcao
    public void escolherUp() {
        int valores[] = valores(); 
        upgrades3[indexUp]++;      
        compras += valores[indexUp];
    }
    
    //Chamado na classe main.Shop.
    //Diminui o numero de ups que quer comprar de uma unica opcao
    public void retirarUp() {
        if (upgrades3[indexUp] > 0) {
            upgrades3[indexUp]--;
            int valores[] = valores();
            compras -= valores[indexUp];
        }
    }
    
    //Confirma a compra de ups feita, caso se tenho o numero de pontos necessarios
    public void comprarUps() {
        if (Game.player.getPontos() >= compras) {
            Game.player.setAtaque(Game.player.getAtaque() + (3 * upgrades3[0]));
            Game.player.setSpeed(Game.player.getSpeed() + (0.3 * upgrades3[1]));
            Game.player.getBala().setSpeed(Game.player.getBala().getSpeed() + (0.3 * upgrades3[2]));
            if (Game.player.isRobusta()) {
                Game.player.getBala2().setSpeed(Game.player.getBala().getSpeed());
            }
            Game.player.setHp_max(Game.player.getHp_max() + (10 * upgrades3[3]));
            Game.player.setVidas(Game.player.getVidas() + (upgrades3[4]));           
            for (int i = 0; i < upgrades3.length; i++) {
                upgrades3[i] = 0;
            }
            Game.player.setPontos(Game.player.getPontos() - compras);
            compras = 0;
        }
    }

    private void opcaoNaves(Graphics g) {
        int x = 76 + 30;
        int y = 28;
        g.setColor(Color.BLACK);
        for (int i = 0; i < 7; i++) {
            g.setFont(new Font("", Font.PLAIN, 10));
            g.drawImage(Gerais.naves.get(i).getImagens()[0], x, y, 40, 40, null);
            if (Gerais.navesCompradas.contains(Gerais.naves.get(i))) {
                g.drawImage(Gerais.background, x, y, 40, 40, null);
                g.setColor(Color.RED);
                g.drawString("Vendida!", x + 1, y + 50);
            } else {
                g.setColor(Color.BLACK);
                if (indexNaves == i && navesOn) {
                    g.setColor(new Color(colorCursorMax, colorCursorMax, colorCursorMax));
                }
                
                //As ultimas 4 naves sao aprimoradas, por isso seu preco é maior.
                if (i < 3) {
                    g.drawString("R$3500,00", x - 7, y + 50);
                } else {
                    g.drawString("R$10000,00", x - 7, y + 50);
                }

            }
            if (indexNaves == i && navesOn) {
                g.setColor(new Color(colorCursorMax, colorCursorMax, colorCursorMax));
            }
            g.drawRect(x, y, 40, 40);
            x += 70;
            if (i == 2 || i == 5) {
                x = 76 + 30;
                y += 60;
            }
        }
    }
    
    //As ultimas 4 naves sao aprimoradas, por isso seu preco é maior.
    public void comprarNave() {
        if (indexNaves < 3) {
            if (Game.player.getPontos() >= 3500) {
                if (!Gerais.navesCompradas.contains(Gerais.naves.get(indexNaves))) {
                    Game.player.setPontos(Game.player.getPontos() - 3500);
                    Gerais.navesCompradas.add(Gerais.naves.get(indexNaves));
                }
            }
        } else {
            if (Game.player.getPontos() >= 10000) {
                if (!Gerais.navesCompradas.contains(Gerais.naves.get(indexNaves))) {
                    Game.player.setPontos(Game.player.getPontos() - 10000);
                    Gerais.navesCompradas.add(Gerais.naves.get(indexNaves));
                }
            }
        }

    }
    
    //Toda vez que se faz a compra de algo o seu valor aumenta. Esse metodo define o preco atual.
    private int[] valores() {
        int valores[] = new int[5];
        double x;
        valores[0] = (Game.player.getAtaque() - 25) * 50 + 200 + upgrades3[0] * 50;
        x = Game.player.getSpeed() - (Gerais.XSPEED * 2);
        x /= 0.3;
        valores[1] = (int) x * 50 + 300 + upgrades3[1] * 50;
        x = Game.player.getBala().getSpeed() - Gerais.BALASPEED;
        x /= 0.3;
        valores[2] = (int) x * 65 + 300 + upgrades3[2] * 65;
        valores[3] = (Game.player.getMaxHp() - 100) * 25 + 200 + upgrades3[3] * 25;
        valores[4] = 1500;
        return valores;
    }

    private void colorCursor() {
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

    public int getCompras() {
        return compras;
    }

    public void setCompras(int compras) {
        this.compras = compras;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public String[] getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(String[] opcoes) {
        this.opcoes = opcoes;
    }

    public String[] getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(String[] upgrades) {
        this.upgrades = upgrades;
    }

    public String[] getUpgrades2() {
        return upgrades2;
    }

    public void setUpgrades2(String[] upgrades2) {
        this.upgrades2 = upgrades2;
    }

    public int[] getUpgrades3() {
        return upgrades3;
    }

    public void setUpgrades3(int[] upgrades3) {
        this.upgrades3 = upgrades3;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndexUp() {
        return indexUp;
    }

    public void setIndexUp(int indexUp) {
        this.indexUp = indexUp;
    }

    public int getIndexNaves() {
        return indexNaves;
    }

    public void setIndexNaves(int indexNaves) {
        this.indexNaves = indexNaves;
    }

    public int getColorCursorMin() {
        return colorCursorMin;
    }

    public void setColorCursorMin(int colorCursorMin) {
        this.colorCursorMin = colorCursorMin;
    }

    public int getColorCursorMax() {
        return colorCursorMax;
    }

    public void setColorCursorMax(int colorCursorMax) {
        this.colorCursorMax = colorCursorMax;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isChangeColor() {
        return changeColor;
    }

    public void setChangeColor(boolean changeColor) {
        this.changeColor = changeColor;
    }

    public boolean isUpsOn() {
        return upsOn;
    }

    public void setUpsOn(boolean upsOn) {
        this.upsOn = upsOn;
    }

    public boolean isNavesOn() {
        return navesOn;
    }

    public void setNavesOn(boolean navesOn) {
        this.navesOn = navesOn;
    }

    public boolean isMenuOn() {
        return menuOn;
    }

    public void setMenuOn(boolean menuOn) {
        this.menuOn = menuOn;
    }

}
