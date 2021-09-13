package configuracoes;

import java.applet.Applet;
import java.applet.AudioClip;

public class Som {
    
    //Classe responsavel por criar os sons utilizados no jogo.

    private AudioClip som;
    String nome;

    public Som(String nome) {
        som = Applet.newAudioClip(Som.class.getResource(nome));
        this.nome = nome;
    }
    
    public Som copia(){
        return new Som(nome);
    }

    public void play() {
        try {
            new Thread() {
                @Override
                public void run() {
                    som.play();
                }
            }.start();
        } catch (Throwable e) {
            System.exit(0);
        }
    }
    
    public void stop(){
        som.stop();
    }

    public void loop() {
        try {
            new Thread() {
                @Override
                public void run() {
                    som.loop();
                }
            }.start();
        } catch (Throwable e) {
            System.exit(0);
        }
    }
}
