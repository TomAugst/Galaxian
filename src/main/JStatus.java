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
import menus.Status;

/**
 *
 * @author tata__000
 */
public class JStatus extends Canvas implements Runnable, KeyListener {

    //É a classe que apresenta a janela de status(dados sobre o players e os inimigos).
    public static JFrame janela;
    public static final int WIDTH = 20 * 16;
    public static final int HEIGHT = 14 * 16;
    public static final int ESCALA = 2;
    private Thread thread;
    private boolean isRunning = false;
    private BufferedImage image;
    private Status status;

    public JStatus() {
        //Game.player.setPontos(10000);
        status = new Status();
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * ESCALA, HEIGHT * ESCALA));
        inicializarJanela();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    private void inicializarJanela() {
        janela = new JFrame("Status");
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
        status.tick();
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
        status.render(g);
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
            case KeyEvent.VK_ESCAPE:
                Gerais.cancelar.play();
                Game.status = false;
                isRunning = false;
                janela.removeAll();
                janela.dispose();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {

    }

}
