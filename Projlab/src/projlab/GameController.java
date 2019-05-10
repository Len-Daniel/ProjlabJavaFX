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
                case LEFT:
                    break;
                case RIGHT:
                    break;
                case ENTER:
                    break;
                case A:
                    break;
                case D:
                    break;    
                case SPACE:
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
