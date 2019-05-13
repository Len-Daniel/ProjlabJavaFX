/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projlab;

import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

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
    @FXML 
    private ImageView map; 
    @FXML
    private FlowPane container; 
    
    private List<Label> labels; 
    
    private TreeMap<String, Tile> tiles = new TreeMap<>();
    
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
    
    public Object type(String s){
        switch(s){
            case "jpanda":
                return new JingleFearPanda();
            case "ppanda":
                return new PipingFearPanda(); 
            case "spanda":
                return new SleepyPanda(); 
            case "orangutan":
                return new Orangutan();
            case "cupboard":
                return new Cupboard();
            case "gamemachine":
                return new GameMachine();
            case "chocomachine":
                return new ChocoMachine(); 
            case "armchair":
                return new Armchair();
            case "exit":
                return new Exit();
            default:
                return null;
        }
    }
    
    //Ide kéne a pálya felépítési, az elemek inicializálása.
    public void createMap(){
        try(BufferedReader nbr = new BufferedReader(new FileReader("TileNames.txt"));
            BufferedReader cbr = new BufferedReader(new FileReader("TileConnect.txt"));
            BufferedReader pbr = new BufferedReader(new FileReader("Placing.txt"));)
        {
            String line = nbr.readLine();
            while(line != null){
                String[] tile = line.split(",");
                if(tile[0].equals("B")) tiles.put(tile[1], new BreakableTile());
                else if (tile[0].equals("T")) tiles.put(tile[1], new Tile());
                line = nbr.readLine();
            }
            
            line = cbr.readLine();
            while(line != null){
                String[] names = line.split(",");
                tiles.get(names[0]).addNeighbor(tiles.get(names[2]));
                line = cbr.readLine();
            }
            
            line = pbr.readLine();
            while(line != null){
                String[] names = line.split(",");
                if (names.length < 3)
                    tiles.get(names[0].substring(1, names[0].length())).setElement(type(names[1]));
                else 
                    tiles.get(names[0].substring(1, names[0].length())).setElement(new Exit(tiles.get(names[2])));
                line = pbr.readLine();
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    //Itt lesz a fő loop, a random dolgok meg ilyesmi.
    public void update(){
        
    }
    
    private void loadSecond(ActionEvent e) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("InGame.fxml"));
        pGame.getChildren().setAll(pane);
    }
    
    //Ez fut le, ha megnyomod a bStart gombot
    @FXML
    private void startGame(ActionEvent event) throws IOException {
        loadSecond(event);
        createMap();
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
