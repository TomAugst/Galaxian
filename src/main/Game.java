package main;

import configuracoes.Gerais;
import entidades.BlocoInimigos;
import entidades.Inimigo;
import entidades.Player;
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
import menus.Status;
import sprites.Background;
import sprites.GerenciarSpritesDano;
import sprites.Hud;

/**
 *
 * @author tata__000
 */
public class Game extends Canvas implements Runnable, KeyListener {

    //É a classe principal do jogo.
    //Chama os ticks e renders de todas as classes fora dos pacotes configuracoes e main
    public static JFrame janela;
    public static final int WIDTH = 20 * 16;
    public static final int HEIGHT = 14 * 16;
    public static final int ESCALA = 3;
    public static int fase = 0;
    public static boolean shopping = false, configuracoes = false, status = false;
    public static boolean SAIU = false;
    private Thread thread;
    private boolean isRunning = false;
    private BufferedImage image;
    public static Player player = Gerais.player;
    private Background bg = new Background();
    private boolean pause, contagemRegreciva = true, fimDoJogo = false, gameOver = false;
    private Hud hud = new Hud(0, player.getY() + 16);
    private int segundos = 3, frames = 0, maxFrames = 60, framesIni = 0;
    private Font sega, over;
    private int tamanhoNumContagem = -15;

    public Game() {
        player = Gerais.player;
        GerenciarSpritesDano.reset();
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * ESCALA, HEIGHT * ESCALA));
        inicializarJanela();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        //Gerais.inicializarInimigos(Gerais.nInimigosBase);
        try {
            sega = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/sega.ttf"));
            over = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fontes/over.ttf"));
        } catch (FontFormatException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        Gerais.RODANDO = true;
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

    //Esse metodo é chamado sempre que o jogo é inicializado ou quando comeca uma nova fase
    private void restart() {
        if (!BlocoInimigos.inimigos.isEmpty()) {
            int num = Gerais.nInimigosBase + fase * 2 - BlocoInimigos.inimigos.size();
            player.setPontos(player.getPontos() - num * BlocoInimigos.pontos);
        }
        player.posicaoInicial();
        BlocoInimigos.inimigos.clear();
        Gerais.inicializarInimigos(Gerais.nInimigosBase + fase * 2);
        for (Inimigo i : BlocoInimigos.inimigos) {
            i.setMaxHp(i.getMaxHp() + 2 * fase);
            i.setHp(i.getMaxHp());
        }
        player.setHp(player.getHp_max());
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

    //Chama o metodo tick de todos os elementos presentes no jogo.
    //Existem varios comportamentos no jogo ativados por booleans.
    //Alguns ticks so sao chamados caso algum boolean esteja ativado/desativado
    //Tambem verifica se o jogador venceu a fase (boolean fimDoJogo) ou se o jogo
    //chegou ao fim (boolean gameOver)
    //Contagem regreciva, quando ativada, permite a realizacao da contagem antes de 
    //cada fase e no inicio do jogo
    private void tick() {
        if (!gameOver) {
            if (!contagemRegreciva) {
                if (!pause && !configuracoes && !shopping && !status) {
                    bg.tick();
                    if (!fimDoJogo) {
                        player.tick();
                        BlocoInimigos.tick();
                        GerenciarSpritesDano.tick();
                        hud.tick();
                        verificarFim();
                        if (player.isMorto() || BlocoInimigos.armaSecreta) {
                            gameOver = true;
                            frames = 0;
                        }
                        if (SAIU) {
                            gameOver();
                        }
                    } else {
                        animacaoVitoria();
                    }
                }
            } else {
                bg.tick();
                player.tick();
                hud.tick();
                fazerContagem();
            }
        } else {
            bg.tick();
            frames++;
            if (frames > maxFrames * 5) {
                gameOver();
            }
        }
    }

    //Renderiza as imgs que serao apresentadas na janela
    private void render() {
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
        if (!gameOver) {
            bg.render(g);
            player.render(g);
            BlocoInimigos.render(g);
            hud.render(g);
            GerenciarSpritesDano.render(g);
            if (pause) {
                g.drawImage(Gerais.fundoPause, 0, 0, null);
            }
            if (contagemRegreciva) {
                g.drawImage(Gerais.background, 0, 0, null);
                if (framesIni >= maxFrames) {
                    desenharContagem(g);
                }
            }
        } else {
            bg.render(g);
            g.drawImage(Gerais.gameover, 0, 0, null);
        }
        //-------------------------------
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * ESCALA, HEIGHT * ESCALA, null);
        bs.show();
    }

    //A contagem inicializar exatamente apos o fim da fase e no inicio do jogo
    //ficaria muito rapido e bem estranho. Para resolver coloquei um atraso de x frames (if (framesIni >= maxFrames))
    //Quando se passa o atraso a contagem é realizada de fato.
    private void fazerContagem() {
        framesIni++;
        if (framesIni >= maxFrames) {
            if (frames >= maxFrames) {
                frames = 0;
                segundos--;
                if (segundos >= 0) {
                    Gerais.contagem.copia().play();
                } else {
                    Gerais.confirmar.copia().play();
                }
                tamanhoNumContagem = -15; //Tamanho inicial da fonte que escreve os numeros
                if (segundos < 0) {
                    contagemRegreciva = false;
                    segundos = 3;
                    frames = 0;
                    framesIni = 0;
                    restart();
                }
            }
            frames++;
        }
    }

    //Verifica se o player conseguiu verncer a fase.
    //Toda vez que o player passa de fase a dificuldade aumenta de forma gradativa.
    //Consequentemente aumenta os pontos que voce ganha quando mata um inimigo
    private void verificarFim() {
        if (BlocoInimigos.inimigos.isEmpty()) {
            fimDoJogo = true;
            fase++;
            BlocoInimigos.ataque += 3;
            BlocoInimigos.pontos += 30;
            BlocoInimigos.ySpeed++;
            if (Gerais.modoCompetitivo) {
                if (Gerais.FREQUENCIADETIRO < 3) {
                    Gerais.FREQUENCIADETIRO += 0.2;
                }
                BlocoInimigos.pontos += 15;
            } else {
                Gerais.FREQUENCIADETIRO = 1;
            }
        }
    }

    //Uma animacao simples quando o player consegue vencer alguma fase
    //Quando ela termina o boolean contagemRegreciva é chamado, o que permite que a contagem comece
    private void animacaoVitoria() {
        framesIni++;
        player.animacoes();
        if (framesIni >= maxFrames) {
            player.setY(player.getY() - 3);
            player.gerenciarBala();
            if (player.getY() <= -16) {
                fimDoJogo = false;
                contagemRegreciva = true;
                //player.posicaoInicial();
                framesIni = 0;
            }
        }

    }

    //Esse metodo desenha o segundo em que a contagem esta.
    private void desenharContagem(Graphics g) {
        over = over.deriveFont(Font.PLAIN, tamanhoNumContagem);
        int x = Game.WIDTH / 2 - 26;
        int y = Game.HEIGHT / 2 + (45 / 3);
        g.setFont(over);
        if (tamanhoNumContagem >= 0) {
            g.drawString(String.valueOf(segundos), x, y);
        }
        tamanhoNumContagem++;
    }

    //Metodo chamado quando se encerra o jogo. É chamado o menu de titulo
    private void gameOver() {
        isRunning = false;
        janela.removeAll();
        janela.dispose();
        Titulo titulo = new Titulo();
        titulo.start();
        stop();
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

    ////Metodo que verifica quando alguma tecla do teclado foi apertada.
    //Permite a movimentacao do player e chamar os menus de paus, config e compra.
    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                player.setRight(true);
                break;
            case KeyEvent.VK_LEFT:
                player.setLeft(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setAtirou(true);
                break;
            case KeyEvent.VK_P:
                if (!pause) {
                    pause = true;
                } else {
                    pause = false;
                }
                break;
            case KeyEvent.VK_R:
                if (!pause && !shopping && !contagemRegreciva && !configuracoes && !status) {
                    contagemRegreciva = true;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if (!pause && !shopping && !contagemRegreciva && !configuracoes && !status) {
                    configuracoes = true;
                    Configuracoes c = new Configuracoes();
                    c.start();
                }
                break;
            case KeyEvent.VK_S:
                if (!pause && !shopping && !contagemRegreciva && !configuracoes && !status) {
                    shopping = true;
                    Shop s = new Shop();
                    s.start();
                }
                break;
            case KeyEvent.VK_C:
                if (!pause && !shopping && !contagemRegreciva && !configuracoes && !status) {
                    status = true;
                    JStatus s = new JStatus();
                    s.start();
                }
                break;
        }
    }

    //Metodo que verifica quando alguma tecla do teclado foi solta.
    @Override
    public void keyReleased(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                player.setRight(false);
                break;
            case KeyEvent.VK_LEFT:
                player.setLeft(false);
                break;
        }
    }

}
