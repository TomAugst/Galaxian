/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import configuracoes.Gerais;
import configuracoes.Som;
import entidades.BlocoInimigos;
import entidades.Player;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import sprites.Background;

/**
 *
 * @author tata__000
 */
public class Titulo extends Canvas implements Runnable, KeyListener {

    //É a classe que apresenta a janela de titulo.
    public static JFrame janela;
    public static final int WIDTH = 20 * 16;
    public static final int HEIGHT = 14 * 16;
    public static final int ESCALA = 3;
    private Thread thread;
    private boolean isRunning = false;
    private BufferedImage image;
    private Background bg = new Background();
    private menus.Inicial menu;
    //private Som s;

    public Titulo() {
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * ESCALA, HEIGHT * ESCALA));
        inicializarJanela();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        menu = new menus.Inicial();
        Gerais.adicionarSons();
        Gerais.mp3Player.addToPlayList(Gerais.mp3s.get(0));
        Gerais.mp3Player.play();
        Gerais.gerarNaves();
        Gerais.player = new Player();
        BlocoInimigos.reset();
        Gerais.FREQUENCIADETIRO = 1;
        Gerais.RODANDO = false;
    }

    private void inicializarJanela() {
        janela = new JFrame("Galaxian");
        janela.add(this);
        janela.setResizable(false);
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tick() {
        bg.tick();
        menu.tick();
    }

    private void render() {
        if (!isRunning) {
            return;
        }
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //-------------------------------
        //g.drawImage(Gerais.background, 0, 0, null);      
        bg.render(g);
        menu.render(g);
        //-------------------------------
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * ESCALA, HEIGHT * ESCALA, null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double contagemDeFrames = 60.0;
        double ns = 1000000000 / contagemDeFrames;
        double delta = 0;
        int fps = 0;
        double timer = System.currentTimeMillis();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                fps++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                //System.out.println("FPS: " + fps);
                fps = 0;
                timer = System.currentTimeMillis();
            }
        }
        stop();
    }

    @Override
    public void keyTyped(KeyEvent key) {

    }

    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                Gerais.cursor.play();
                menu.setIndexCursor(menu.getIndexCursor() + 1);
                if (menu.getIndexCursor() > 2) {
                    menu.setIndexCursor(0);
                }
                break;
            case KeyEvent.VK_DOWN:
                Gerais.cursor.play();
                menu.setIndexCursor(menu.getIndexCursor() - 1);
                if (menu.getIndexCursor() < 0) {
                    menu.setIndexCursor(2);
                }
                break;
            case KeyEvent.VK_ENTER:
                switch (menu.getIndexCursor()) {
                    case 2:
                        Gerais.pararSons();
                        Gerais.confirmar.play();
                        isRunning = false;
                        janela.removeAll();
                        janela.dispose();
                        Game game = new Game();
                        game.start();
                        break;
                    case 1:
                        Gerais.confirmar.play();
                        Configuracoes config = new Configuracoes();
                        config.start();
                        break;
                    case 0:
                        Gerais.confirmar.play();
                        isRunning = false;
                        janela.dispose();
                        System.exit(0);
                        break;
                }

                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {

    }

}
