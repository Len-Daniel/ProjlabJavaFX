/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projlab;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author lengy
 */
public class GameController implements Initializable {
    
    //1. játékos, A, D és space gombbal irányítjuk
    private Orangutan o1;
    
    //2. játékos, bal nyíl, jobb nyíl, és enter gombbal irányítjuk
    private Orangutan o2;
    
    //1. játékos pillanatnyilag kiválasztott csempéje. Ide próbál átlépni az o1, ha a játékos megnyomja a space gombot.
    private Tile selectedTileO1;
    
    //2. játékos pillanatnyilag kiválasztott csempéje. Ide próbál átlépni az o2, ha a játékos megnyomja az enter gombot.
    private Tile selectedTileO2;
    
    @FXML
    private Button bStart;
    @FXML
    private AnchorPane pGame;
    @FXML
    private Label lStart;
    
    AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now){
                    update();
            }
        };
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}  
    
    //Ide kéne a pálya felépítési, az elemek inicializálása.
    public void createMap(){
        
    }
    
    //Itt lesz a fő loop, a random dolgok meg ilyesmi.
    public void update(){
    
    }
    
    //Ez fut le, ha megnyomod a bStart gombot
    @FXML
    private void startGame(ActionEvent event) {
        bStart.setVisible(false);
        lStart.setVisible(true);
        pGame.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    selectedTileO1 = o1.getTile().getLeftNeighborOf(selectedTileO1);
                    break;
                case D:
                    selectedTileO1 = o1.getTile().getLeftNeighborOf(selectedTileO1);
                    break;    
                case SPACE:
                    o1.move(o1.getTile().getNeighborIndex(selectedTileO1));
                    selectedTileO1 = o1.getTile().getNeighbor(0);
                    break;
                case LEFT:
                    selectedTileO2 = o2.getTile().getLeftNeighborOf(selectedTileO2);
                    break;
                case RIGHT:
                    selectedTileO2 = o2.getTile().getLeftNeighborOf(selectedTileO2);
                    break;
                case ENTER:
                    o2.move(o2.getTile().getNeighborIndex(selectedTileO2));
                    selectedTileO1 = o2.getTile().getNeighbor(0);
                    break;
                case ESCAPE:
                    System.exit(0);
                    break;
            }
        });
        
        //Elindítja a timert, megy az update loop
        timer.start();
    }
    
}
