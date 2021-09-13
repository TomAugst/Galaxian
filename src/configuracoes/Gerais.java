package configuracoes;

import entidades.BlocoInimigos;
import entidades.Inimigo;
import entidades.Player;
import jaco.mp3.player.MP3Player;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Gerais {

    //Essa classe é responsavel por inicializar os valores basicos dos status do player
    //e dos inimigos, assim como o acesso as imagens e sons usados no jogo. A ideia era
    //criar uma especie de banco de dados.
    public static boolean RODANDO = false;

    public static final Inimigo inimigo1 = new Inimigo("/recursos/inimigos.png", 0);
    public static final Inimigo inimigo2 = new Inimigo("/recursos/inimigos.png", 1);
    public static final Inimigo inimigo3 = new Inimigo("/recursos/inimigos.png", 2);
    public static final Inimigo explosao = new Inimigo("/recursos/inimigos.png", 3);
    public static final Inimigo iDamage = new Inimigo("/recursos/inimigosDano.png", 0);
    public static final Inimigo pDamage = new Inimigo("/recursos/playersDano.png", 0);
    public static int PONTOS = 200;
    public static int nInimigosBase = 4;
    public static double FREQUENCIADETIRO = 1;
    public static boolean modoCompetitivo = false;
    public static Player player = new Player();
    //------------------------------------------------------------------------
    public static ArrayList<Inimigo> naves = new ArrayList<>();
    public static ArrayList<Inimigo> navesCompradas = new ArrayList<>();
    //------------------------------------------------------------------------
    public static final int XSPEED = 1;
    public static final int YSPEED = 1;
    public static final int BALASPEED = 5;
    //------------------------------------------------------------------------
    public static MP3Player mp3Player = new MP3Player();
    public static final Som somTiro = new Som("/sons/shot.wav");
    public static final Som somExplosao2 = new Som("/sons/Fire2.wav");
    public static final Som cursor = new Som("/sons/Cursor3.wav");
    public static final Som contagem = new Som("/sons/Cursor4.wav");
    public static final Som confirmar = new Som("/sons/Decision3.wav");
    public static final Som cancelar = new Som("/sons/Cancel1.wav");
    public static ArrayList<File> mp3s = new ArrayList<>();
    //------------------------------------------------------------------------
    public static final BufferedImage fundo1 = new Sprites("/recursos/fundo1.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    public static final BufferedImage fundo2 = new Sprites("/recursos/fundo2.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    public static final BufferedImage fundo3 = new Sprites("/recursos/fundo3.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    public static final BufferedImage fundoPause = new Sprites("/recursos/pause.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    public static final BufferedImage imgTitulo = new Sprites("/recursos/imgTitulo.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    public static final BufferedImage background = new Sprites("/recursos/background.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    public static final BufferedImage configs = new Sprites("/recursos/configs.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    public static final BufferedImage gameover = new Sprites("/recursos/gameover.png") {
        @Override
        public void render(Graphics g) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }.getSprite(0, 0, 320, 224);
    //------------------------------------------------------------------------

    public static void inicializarInimigos(int x) {
        for (int i = 0; i < x; i++) {
            switch (new Random().nextInt(3)) {
                case 0:
                    BlocoInimigos.adicionar(inimigo1.clone());
                    break;
                case 1:
                    BlocoInimigos.adicionar(inimigo2.clone());
                    break;
                default:
                    BlocoInimigos.adicionar(inimigo3.clone());
                    break;
            }
        }
        BlocoInimigos.posicionar();
    }

    public static void gerarNaves() {
        naves.clear();
        navesCompradas.clear();
        for (int i = 1; i <= 3; i++) {
            naves.add(new Inimigo("/recursos/players.png", i));
        }
        for (int i = 0; i < 4; i++) {
            Inimigo inimin = new Inimigo("/recursos/players2.png", i);
            inimin.setRobusto(true);
            naves.add(inimin);
        }
        navesCompradas.add(new Inimigo("/recursos/players.png", 0));
    }

    public static void adicionarSons() {
        mp3s.clear();
        mp3s.add(new File("C:/Trabalho Pratico Galaxian/src/sons/Opening3.mp3"));
        for (int i = 0; i < 10; i++) {
            String s = "C:/Trabalho Pratico Galaxian/src/sons/Battle".concat(String.valueOf(i + 1)).concat(".mp3");
            //allSons.add(new Som(s));
            mp3s.add(new File(s));
        }
        mp3Player.setRepeat(true);
    }

    public static void pararSons() {
        mp3Player.stop();
        mp3Player = new MP3Player();
        mp3Player.setRepeat(true);
    }

    public Gerais() {

    }

}
