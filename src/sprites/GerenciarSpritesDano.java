/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import java.awt.Graphics;
import java.util.ArrayList;

public class GerenciarSpritesDano {
    
    //Essa classe possui a mesma ideia que bloco de inimigos. 
    //ela gerencia todos os sprites de dano que precisam ser apresentados na tela.
    //Ela chama o tick de cada sprite (os atualiza) e chama os metodos de renderizacao de cada um
    //Tambem e responsavel dela removacao dos sprites que nao devem ser mais apresentados. (Por padrao cada sprite tem duracao de 90 frames)
    //Os sprites sao adicioados nas listas abaixos quando o inimigo/player leva dano
    //Isso ocorre dentro do metodo colisao() da classe entidades.Bala.
    
    private static ArrayList<DanoInimigo> danoInimigos = new ArrayList<>();
    private static ArrayList<DanoPlayer> danoPlayers = new ArrayList<>();

    public static void addDanoInimigos(DanoInimigo d) {
        danoInimigos.add(d);
    }
    
    public static void addDanoPlayer(DanoPlayer d) {
        danoPlayers.add(d);
    }
    
    public static void reset(){
        danoInimigos.clear();
        danoPlayers.clear();
    }

    private static void deletar() {
        ArrayList<DanoInimigo> lixo1 = new ArrayList<>();
        ArrayList<DanoPlayer> lixo2 = new ArrayList<>();
        if (!danoInimigos.isEmpty()) {
            for (DanoInimigo d : danoInimigos) {
                if (d.isDisposed()) {
                    lixo1.add(d);
                }
            }
            for (DanoInimigo d : lixo1) {
                danoInimigos.remove(d);
            }
            lixo1.clear();
        }
        if (!danoPlayers.isEmpty()) {
            for (DanoPlayer d : danoPlayers) {
                if (d.isDisposed()) {
                    lixo2.add(d);
                }
            }
            for (DanoPlayer d : lixo2) {
                danoInimigos.remove(d);
            }
            lixo2.clear();
        }
    }

    public static void tick() {
        deletar();
        if (!danoInimigos.isEmpty()) {
            for (DanoInimigo d : danoInimigos) {
                d.tick();
            }
        }
        if (!danoPlayers.isEmpty()) {
            for (DanoPlayer d : danoPlayers) {
                d.tick();
            }
        }
    }

    public static void render(Graphics g) {
        if (!danoInimigos.isEmpty()) {
            for (DanoInimigo d : danoInimigos) {
                d.render(g);
            }
        }
        if (!danoPlayers.isEmpty()) {
            for (DanoPlayer d : danoPlayers) {
                d.render(g);
            }
        }
    }

}
