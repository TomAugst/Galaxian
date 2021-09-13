/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import configuracoes.Gerais;
import configuracoes.Som;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import menus.Config;

/**
 *
 * @author tata__000
 */
public class Configuracoes extends Canvas implements Runnable, KeyListener {

    //Essa é a classe que genrecia a janela de configuracoes.
    public static JFrame janela;
    public static final int WIDTH = 20 * 16;
    public static final int HEIGHT = 14 * 16;
    public static final int ESCALA = 2; //Todas as dimensoes serao multiplicadas por esse numero. É o que da o tom de arcade para o jogo
    private Thread thread;
    private boolean isRunning = false;
    //image é responsavel por apresentar tudo o conteudo que deve ser mostrado na janela
    private BufferedImage image;
    private Font sega, over;
    private Config config = new Config(); //Objeto da classe que dita o que deve ser desenhado na janela.

    public Configuracoes() {
        addKeyListener(this); //Permite que o teclado seja utilizado
        setPreferredSize(new Dimension(WIDTH * ESCALA, HEIGHT * ESCALA));
        inicializarJanela();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        try {
            sega = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/sega.ttf"));
            over = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/over.ttf"));
        } catch (FontFormatException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void inicializarJanela() {
        janela = new JFrame("Configuracoes");
        janela.add(this);
        janela.setResizable(false);
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
    }

    //Inicializa o loop da janela.
    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    //encerra o loop da janela caso seja necessario.
    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Atualiza o que sera mostrado na janela
    private void tick() {
        config.tick();
    }

    //Renderiza as imgs que serao apresentadas na janela
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(150, 150, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //-------------------------------
        config.render(g);
        //-------------------------------
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * ESCALA, HEIGHT * ESCALA, null);
        bs.show();
    }

    //---------------------------------------Metodo pertencente a Runnable-----------------------------------
    //Aqui esta presente o loop que atualiza da janela a cada 60 frames
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double contagemDeFrames = 60.0;
        double ns = 1000000000 / contagemDeFrames;
        double delta = 0;
        //int fps = 0;
        double timer = System.currentTimeMillis();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                //fps++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                //System.out.println("FPS: " + fps);
                //fps = 0;
                timer = System.currentTimeMillis();
            }
        }
        stop();
    }

    //--------------------------------------Metodos pertencentes a KeyListener----------------------------------
    @Override
    public void keyTyped(KeyEvent key) {

    }

    //Muda a posicao dos cursores presentes em menus.Config
    //Chama os metodos que realizam alguma alteracao que o jogador quis fazer.
    //Metodo que verifica quando alguma tecla do teclado foi apertada.
    @Override
    public void keyPressed(KeyEvent key) {
        Som s;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                s = Gerais.cursor.copia();
                s.play();
                if (config.isOpcoesOn()) {
                    config.setIndex(config.getIndex() - 1);
                    if (config.getIndex() < 0) {
                        config.setIndex(3);
                    }
                }
                if (config.isGeraisOn()) {
                    config.setIndexGerais(config.getIndexGerais() - 1);
                    if (config.getIndexGerais() < 0) {
                        config.setIndexGerais(1);
                    }
                }
                break;
            case KeyEvent.VK_DOWN:
                s = Gerais.cursor.copia();
                s.play();
                if (config.isOpcoesOn()) {
                    config.setIndex(config.getIndex() + 1);
                    if (config.getIndex() > 3) {
                        config.setIndex(0);
                    }
                }
                if (config.isGeraisOn()) {
                    config.setIndexGerais(config.getIndexGerais() + 1);
                    if (config.getIndexGerais() > 1) {
                        config.setIndexGerais(0);
                    }
                }
                break;

            case KeyEvent.VK_ENTER:
                s = Gerais.confirmar.copia();
                s.play();
                if (config.isOpcoesOn()) {
                    if (config.getIndex() == 0) {
                        config.setOpcoesOn(false);
                        config.setPlayerOn(false);
                        config.setGeraisOn(true);
                        return;
                    }
                    if (config.getIndex() == 1) {
                        config.setOpcoesOn(false);
                        config.setPlayerOn(true);
                        config.setGeraisOn(false);
                        return;
                    }
                    if (config.getIndex() == 2) {
                        if (Gerais.RODANDO) {
                            Game.configuracoes = false;
                        }
                        isRunning = false;
                        janela.removeAll();
                        janela.dispose();
                    }
                    if (config.getIndex() == 3) {
                        if (Gerais.RODANDO) {
                            Game.configuracoes = false;
                            Game.SAIU = true;
                            isRunning = false;
                            janela.removeAll();
                            janela.dispose();
                        } else {
                            System.exit(0);
                        }

                    }
                }
                if (config.isGeraisOn()) {
                    config.confirmarMudancasGerais();
                    return;
                }
                if (config.isPlayerOn()) {
                    config.confirmarMudancaPlayer();
                    return;
                }
                break;

            case KeyEvent.VK_LEFT:
                s = Gerais.cursor.copia();
                s.play();
                if (config.isGeraisOn()) {
                    if (config.getIndexGerais() == 0) {
                        config.setOnOff(config.getOnOff() - 1);
                        if (config.getOnOff() < 0) {
                            config.setOnOff(1);
                        }
                    }

                    if (config.getIndexGerais() == 1) {
                        config.setIndexMusicas(config.getIndexMusicas() - 1);
                        if (config.getIndexMusicas() < 0) {
                            config.setIndexMusicas(config.getMusicas().length - 1);
                        }
                    }

                }
                if (config.isPlayerOn()) {
                    config.setIndexNaves(config.getIndexNaves() - 1);
                    if (config.getIndexNaves() < 0) {
                        config.setIndexNaves(config.getnNaves() - 1);
                    }
                }
                break;
            case KeyEvent.VK_RIGHT:
                s = Gerais.cursor.copia();
                s.play();
                if (config.isGeraisOn()) {
                    if (config.getIndexGerais() == 0) {
                        config.setOnOff(config.getOnOff() + 1);
                        if (config.getOnOff() > 1) {
                            config.setOnOff(0);
                        }
                    }

                    if (config.getIndexGerais() == 1) {
                        config.setIndexMusicas(config.getIndexMusicas() + 1);
                        if (config.getIndexMusicas() == config.getMusicas().length) {
                            config.setIndexMusicas(0);
                        }
                    }
                }
                if (config.isPlayerOn()) {
                    config.setIndexNaves(config.getIndexNaves() + 1);
                    if (config.getIndexNaves() == config.getnNaves()) {
                        config.setIndexNaves(0);
                    }
                }
                break;
            case KeyEvent.VK_ESCAPE:
                s = Gerais.cancelar.copia();
                s.play();
                if (!config.isOpcoesOn()) {
                    config.setGeraisOn(false);
                    config.setPlayerOn(false);
                    config.setOpcoesOn(true);
                    return;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {

    }

}
