package menus;

import configuracoes.Gerais;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Configuracoes;
import main.Game;
import main.Titulo;

public class Config {

    //Essa é a classe que dita o que sera mostrado pela janela
    //de configuracoes.
    private int width = Configuracoes.WIDTH;
    private int height = Configuracoes.HEIGHT;
    //Sao as opcoes que estao presentes no menu lateral
    private String opcoes[];
    //Sao as opcoes de musicas apresentadas
    private String musicas[];
    //Sao as opcoes presentes no menu de Gerais
    private String opcaoesGerais[];
    //A possicao dos cursores de cada menu
    private int indexGerais = 0, indexMusicas = 0, indexNaves = 0, nNaves = Gerais.navesCompradas.size(), index = 0, onOff = 1;
    private Font sega, over;
    //Nºs para a animacao de cores dos cursores
    private int colorCursorMin = 120, colorCursorMax = 255, color = 120;
    private boolean changeColor, opcoesOn = true, playerOn = false, geraisOn = false;
    private boolean modoCompetivivo = Gerais.modoCompetitivo;
    
    public Config() {
        //this.musicas = new String[]{"◀ Musica 1 ▶", "◀ Musica 2 ▶"};
        musicas = new String[Gerais.mp3s.size()];
        for (int i = 0; i < musicas.length; i++) {
            String s = "◀ Musica ".concat(String.valueOf(i + 1).concat(" ▶"));
            musicas[i] = s;
        }
        this.opcoes = new String[]{"Gerais", "Player", "Fechar", "Sair"};
        opcaoesGerais = new String[]{"Modo Competitivo:", "Musica de Fundo:"};
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

    //O conteudo de cada menu lateral so é apresentado se o cursor estiver posicionado
    //na opcao que o conteudo pertence
    public void render(Graphics g) {
        backGround(g);
        slots(g);
        if (index == 0) {
            opcoesGerais(g);
        }
        if (index == 1) {
            opcoesPlayer(g);
        }
        if (index == 2) {
            opcaoFechar(g);
        }
        if (index == 3) {
            opcaoSair(g);
        }
    }

    //Responsavel pela animacao do cursor e o estado do boolean modoCompetitivo
    public void tick() {
        colorCursor();
        onoff();
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

    //Responsavel pelo desenho da base da janela
    private void backGround(Graphics g) {
        //g.drawImage(Gerais.configs, 0, 0, null);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        g.drawRect(0, 0, 76, height);
        g.drawRect(0, 0, 76, 48);
        g.drawRect(0, 48, 76, 48);
        g.drawRect(0, 96, 76, 48);
        g.drawRect(0, 96 + 48, 76, 48);
    }

    //Responsavel pelo desenho do conteudo do menu lateral
    private void slots(Graphics g) {
        sega = sega.deriveFont(Font.PLAIN, 8);
        g.setFont(sega);
        for (int i = 0; i < opcoes.length; i++) {
            int x = 15;
            g.setColor(Color.BLUE);
            if (index == i) {
                g.setColor(new Color(color, color, color));
                x += 15;
            }
            g.drawString(opcoes[i], x, (i * 48) + 28);
        }
    }

    //Responsavel pelo desenho do conteudo do menu Gerais
    //O boolean geraisOn determina que as animacoes de cursor so sera
    //mostrada quando o player confirmar que quer acessar o menu Gerais,
    //nao quando apenas ele esta selecionado no cursor
    private void opcoesGerais(Graphics g) {
        g.setFont(new Font("", Font.PLAIN, 10));
        int x = 101;
        int y = 48;
        for (int i = 0; i < opcaoesGerais.length; i++) {
            g.setColor(Color.BLACK);
            if (indexGerais == i && geraisOn) {
                g.setColor(new Color(color, color, color));
            }
            g.drawString(opcaoesGerais[i], x, (i * y) + 28);
            y = 24;
        }
        if (geraisOn) {
            if (modoCompetivivo) {
                g.setColor(new Color(colorCursorMax, colorCursorMax, colorCursorMax));
                g.drawString("ON", 207, 28);
                g.setColor(Color.BLACK);
                g.drawString("OFF", 229, 28);
            } else {
                g.setColor(Color.BLACK);
                g.drawString("ON", 207, 28);
                g.setColor(new Color(colorCursorMax, colorCursorMax, colorCursorMax));
                g.drawString("OFF", 229, 28);
            }
            g.setColor(new Color(colorCursorMax, colorCursorMax, colorCursorMax));
            g.drawString(musicas[indexMusicas], 207, 52);
            
        } else {
            g.setColor(Color.BLACK);
            g.drawString("ON", 207, 28);
            g.drawString("OFF", 229, 28);
            g.drawString(musicas[indexMusicas], 207, 52);
        }
        
    }
    
    private void onoff() {
        if (onOff == 0) {
            modoCompetivivo = true;
        } else {
            modoCompetivivo = false;
        }
    }

    //Chamado quando o se confirma as mudancas feitas no menu Gerais
    public void confirmarMudancasGerais() {
        Gerais.pararSons();
        if (onOff == 0) {
            Gerais.modoCompetitivo = true;
        } else {
            Gerais.modoCompetitivo = false;
        }
        Gerais.mp3Player.addToPlayList(Gerais.mp3s.get(indexMusicas));
        Gerais.mp3Player.play();
        //Gerais.allSons.get(indexMusicas).loop();
        geraisOn = false;
        opcoesOn = true;
    }

    //Chamado quando o se confirma as mudancas feitas no menu Player
    public void confirmarMudancaPlayer() {
        if (Gerais.RODANDO) {
            Game.player.mudarSprites(Gerais.navesCompradas.get(indexNaves));
        } else {
            Gerais.player.mudarSprites(Gerais.navesCompradas.get(indexNaves));
        }
        playerOn = false;
        opcoesOn = true;
    }

    //Responsavel pelo desenho do conteudo do menu player
    private void opcoesPlayer(Graphics g) {
        BufferedImage img;
        sega = sega.deriveFont(Font.PLAIN, 8);
        String s;
        if (Gerais.RODANDO) {
            img = Game.player.getImagens()[0];
        } else {
            img = Gerais.player.getImagens()[0];
        }
        s = "◀ " + String.valueOf(indexNaves + 1).concat(" / ").concat(String.valueOf(nNaves)) + " ▶";
        g.drawImage(img, (width - 76) / 2 + 76 - 30, 10, 50, 50, null);
        g.setFont(sega);
        g.setColor(Color.BLUE);
        g.drawString("Disponiveis", (width - 76) / 2 + 76 - 35, 90);
        g.setColor(Color.BLACK);
        g.drawImage(Gerais.navesCompradas.get(indexNaves).getImagens()[0], (width - 76) / 2 + 76 - 30, 100, 50, 50, null);
        g.drawRect((width - 76) / 2 + 76 - 30, 100, 50, 50);
        g.setFont(new Font("", Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        if (playerOn) {
            g.setColor(new Color(colorCursorMax, colorCursorMax, colorCursorMax));
        }
        g.drawString(s, (width - 76) / 2 + 55, 170);
    }

    //Responsavel pelo desenho do conteudo do menu fechar
    private void opcaoFechar(Graphics g) {
        int x = 101;
        int y = 48;
        String s = "Fechar janela de Configuracoes";
        g.setFont(new Font("", Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        g.drawString(s, x, 28);
    }

    //Responsavel pelo desenho do conteudo do menu sair
    private void opcaoSair(Graphics g) {
        int x = 101;
        int y = 48;
        String s;
        if (Gerais.RODANDO) {
            s = "Ir para o menu inicial";
        }else{
            s = "Sair do jogo";
        }
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("", Font.PLAIN, 10));
        g.drawString(s, x, 28);
    }
//--------------------------------------------------------Get Set------------------------------------------------

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
    
    public String[] getOpcoes() {
        return opcoes;
    }
    
    public void setOpcoes(String[] opcoes) {
        this.opcoes = opcoes;
    }
    
    public String[] getMusicas() {
        return musicas;
    }
    
    public void setMusicas(String[] musicas) {
        this.musicas = musicas;
    }
    
    public int getIndexGerais() {
        return indexGerais;
    }
    
    public void setIndexGerais(int indexGerais) {
        this.indexGerais = indexGerais;
    }
    
    public int getIndexMusicas() {
        return indexMusicas;
    }
    
    public void setIndexMusicas(int indexMusicas) {
        this.indexMusicas = indexMusicas;
    }
    
    public int getIndexNaves() {
        return indexNaves;
    }
    
    public void setIndexNaves(int indexNaves) {
        this.indexNaves = indexNaves;
    }
    
    public int getnNaves() {
        return nNaves;
    }
    
    public void setnNaves(int nNaves) {
        this.nNaves = nNaves;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
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
    
    public String[] getOpcaoesGerais() {
        return opcaoesGerais;
    }
    
    public void setOpcaoesGerais(String[] opcaoesGerais) {
        this.opcaoesGerais = opcaoesGerais;
    }
    
    public boolean isOpcoesOn() {
        return opcoesOn;
    }
    
    public void setOpcoesOn(boolean opcoesOn) {
        this.opcoesOn = opcoesOn;
    }
    
    public boolean isPlayerOn() {
        return playerOn;
    }
    
    public void setPlayerOn(boolean playerOn) {
        this.playerOn = playerOn;
    }
    
    public boolean isGeraisOn() {
        return geraisOn;
    }
    
    public void setGeraisOn(boolean geraisOn) {
        this.geraisOn = geraisOn;
    }
    
    public boolean isModoCompetivivo() {
        return modoCompetivivo;
    }
    
    public void setModoCompetivivo(boolean modoCompetivivo) {
        this.modoCompetivivo = modoCompetivivo;
    }
    
    public int getOnOff() {
        return onOff;
    }
    
    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }
    
}
