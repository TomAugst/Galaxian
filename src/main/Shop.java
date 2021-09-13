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
import menus.Shopping;

/**
 *
 * @author tata__000
 */
public class Shop extends Canvas implements Runnable, KeyListener {
    
    //É a classe que apresenta a janela de upgrades/compras.

    public static JFrame janela;
    public static final int WIDTH = 20 * 16;
    public static final int HEIGHT = 14 * 16;
    public static final int ESCALA = 2;
    private Thread thread;
    private boolean isRunning = false;
    private BufferedImage image;
    private Shopping shop;

    public Shop() {
        //Game.player.setPontos(10000);
        shop = new Shopping();
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * ESCALA, HEIGHT * ESCALA));
        inicializarJanela();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    private void inicializarJanela() {
        janela = new JFrame("Shopping");
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
        shop.tick();
    }

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
        shop.render(g);
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
        Som s;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                s = Gerais.cursor.copia();
                s.play();
                if (shop.isMenuOn()) {
                    shop.setIndex(shop.getIndex() - 1);
                    if (shop.getIndex() < 0) {
                        shop.setIndex(shop.getOpcoes().length - 1);
                    }
                }
                if (shop.isUpsOn()) {
                    shop.setIndexUp(shop.getIndexUp() - 1);
                    if (shop.getIndexUp() < 0) {
                        shop.setIndexUp(shop.getUpgrades().length - 1);
                    }
                }
                if (shop.isNavesOn()) {
                    shop.setIndexNaves(shop.getIndexNaves() - 3);
                    if (shop.getIndexNaves() < 0) {
                        shop.setIndexNaves(Gerais.naves.size() - 1);
                    }
                }
                break;
            case KeyEvent.VK_DOWN:
                s = Gerais.cursor.copia();
                s.play();
                if (shop.isMenuOn()) {
                    shop.setIndex(shop.getIndex() + 1);
                    if (shop.getIndex() == shop.getOpcoes().length) {
                        shop.setIndex(0);
                    }
                }
                if (shop.isUpsOn()) {
                    shop.setIndexUp(shop.getIndexUp() + 1);
                    if (shop.getIndexUp() == shop.getUpgrades().length) {
                        shop.setIndexUp(0);
                    }
                }
                if (shop.isNavesOn()) {
                    shop.setIndexNaves(shop.getIndexNaves() + 3);
                    if (shop.getIndexNaves() >= Gerais.naves.size()) {
                        shop.setIndexNaves(0);
                    }
                }
                break;
            case KeyEvent.VK_ENTER:
                s = Gerais.confirmar.copia();
                s.play();
                if (shop.isMenuOn()) {
                    if (shop.getIndex() == 0) {
                        shop.setNavesOn(true);
                        shop.setMenuOn(false);
                        return;
                    }
                    if (shop.getIndex() == 1) {
                        shop.setUpsOn(true);
                        shop.setMenuOn(false);
                        return;
                    }
                    if (shop.getIndex() == 2) {
                        Game.shopping = false;
                        isRunning = false;
                        janela.removeAll();
                        janela.dispose();
                    }
                }
                if (shop.isNavesOn()) {
                    shop.comprarNave();
                }
                if (shop.isUpsOn()) {
                    shop.comprarUps();
                }
                break;
            case KeyEvent.VK_RIGHT:
                s = Gerais.cursor.copia();
                s.play();
                if (shop.isNavesOn()) {
                    shop.setIndexNaves(shop.getIndexNaves() + 1);
                    if (shop.getIndexNaves() >= Gerais.naves.size()) {
                        shop.setIndexNaves(0);
                    }
                }
                if (shop.isUpsOn()) {
                    shop.escolherUp();
                }
                break;
            case KeyEvent.VK_LEFT:
                s = Gerais.cursor.copia();
                s.play();
                if (shop.isNavesOn()) {
                    shop.setIndexNaves(shop.getIndexNaves() - 1);
                    if (shop.getIndexNaves() < 0) {
                        shop.setIndexNaves(Gerais.naves.size() - 1);
                    }
                }
                if (shop.isUpsOn()) {
                    shop.retirarUp();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                s = Gerais.cancelar.copia();
                s.play();
                shop.setMenuOn(true);
                shop.setNavesOn(false);
                shop.setUpsOn(false);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {

    }

}
