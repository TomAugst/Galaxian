package configuracoes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public abstract class Sprites {
    
    //Classe responsavel pela criacao de todas as imagens importadas para o jogo
    
    private BufferedImage sprite;

    public Sprites(String path) {
        try {
            sprite = ImageIO.read(getClass().getResource(path));
        } catch (IOException ex) {
            Logger.getLogger(Sprites.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage getSprite(int x, int y, int width, int height){
        return sprite.getSubimage(x, y, width, height);
    }  
    public abstract void render(Graphics g);   
}
