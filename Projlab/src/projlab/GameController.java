/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projlab;

import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.util.Map.Entry;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author lengy
 */
public class GameController implements Initializable {
    
    private double cnt=0.015;
    
    private double t = 0;
    
    //1. játékos, A, D és space gombbal irányítjuk
    private Orangutan o1;
    
    //2. játékos, bal nyíl, jobb nyíl, és enter gombbal irányítjuk
    private Orangutan o2;
    
    //1. játékos pillanatnyilag kiválasztott csempéje. Ide próbál átlépni az o1, ha a játékos megnyomja a space gombot.
    private Tile selectedTileO1;
    
    //2. játékos pillanatnyilag kiválasztott csempéje. Ide próbál átlépni az o2, ha a játékos megnyomja az enter gombot.
    private Tile selectedTileO2;
    
    @FXML
    private AnchorPane pGame;
    @FXML 
    private ImageView mapPic; 
    @FXML
    private Button bStart;
    @FXML
    private Text player1Points;
    @FXML
    private Text player2Points;
    @FXML
    private Text pl1Text;
    @FXML
    private Text pl2Text;

    private Map<String, Panda> pandas = new HashMap<String, Panda>();  
    private Map<String, Cupboard> cupboards = new HashMap<String, Cupboard>();
    private Map<String, GameMachine> gamemachines = new HashMap<String, GameMachine>();
    private Map<String, ChocoMachine> chocomachines = new HashMap<String, ChocoMachine>(); 
    private Map<String, Armchair> armchairs = new HashMap<String, Armchair>(); 
    
    private Exit ext;
    
    private TreeMap<String, Tile> tiles = new TreeMap<String, Tile>();
    private Map<String, Text> texts = new HashMap<String, Text>();
    
    AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now){
                t+=cnt;
                    if(t>1) {
                        update();
                        t=0;
                    }
            }
        };
   
    
    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }//példa: String keyString = getKey(tiles, vmilyenTile);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}  
    
    public Object type(String s, Tile t){
        int i = 0; 
        switch(s){
            case "jpanda":
                JingleFearPanda jfpanda = new JingleFearPanda();
                jfpanda.setTile(t);
                pandas.put(rakdmanbele(pandas, "JFP"), jfpanda);
                felrak("JFP", t);
                return jfpanda;
            case "ppanda":
                PipingFearPanda pfpanda = new PipingFearPanda(); 
                pfpanda.setTile(t);
                pandas.put(rakdmanbele(pandas, "PFP"), pfpanda);
                felrak("PFP", t);
                return pfpanda; 
            case "spanda":
                SleepyPanda spanda = new SleepyPanda(); 
                spanda.setTile(t);
                pandas.put(rakdmanbele(pandas, "SP"), spanda);
                felrak("SP", t);
                return spanda; 
            case "cupboard":
                Cupboard cupboard = new Cupboard(); 
                cupboard.setTile(t);
                cupboards.put(rakdmanbele(cupboards, "CP"), cupboard);
                felrak("CP", t);
                return cupboard;
            case "gamemachine":
                GameMachine gamemachine = new GameMachine(); 
                gamemachine.setTile(t);
                gamemachines.put(rakdmanbele(gamemachines, "GM"), gamemachine); 
                felrak("GM", t);
                return gamemachine;
            case "chocomachine":
                ChocoMachine chocomachine = new ChocoMachine(); 
                chocomachine.setTile(t);
                chocomachines.put(rakdmanbele(chocomachines, "CM"), chocomachine);
                felrak("CM", t);
                return chocomachine; 
            case "armchair":
                Armchair armchair = new Armchair();
                armchair.setTile(t);
                armchairs.put(rakdmanbele(armchairs, "AC"), armchair);
                felrak("AC", t);
                return armchair;
            case "exit":
                return new Exit();
            default:
                return null;
        }
    }
    
    //hibás a cucc
    public String isIn(Map<String, ?> m, String s, int i){
        if (m.containsKey(s+i)){
            i++;
            isIn(m, s+i, i);
        }
        return s+1;
    }
    
    public String rakdmanbele(Map<String, ?> m, String s) {
        String asd = s+Integer.toString(m.size()+1);
        return asd;
    }
    public void felrak(String s, Tile t) {
        String ks = getKey(tiles, t);
        texts.get(ks).setText(s);
    }
    
    //Ide kéne a pálya felépítési, az elemek inicializálása.
    public void createMap(){
        try{BufferedReader nbr = new BufferedReader(new FileReader("TileNames.txt"));
            BufferedReader cbr = new BufferedReader(new FileReader("TileConnect.txt"));
            BufferedReader tbr = new BufferedReader(new FileReader("TextConnect.txt"));
            BufferedReader pbr = new BufferedReader(new FileReader("Placing.txt"));
            
            String line = nbr.readLine();
                
            while(line != null){
                if(line.charAt(0)=='B') tiles.put(line, new BreakableTile());
                else if (line.charAt(0)=='T') tiles.put(line, new Tile());
                line = nbr.readLine();
            }
            
            line = cbr.readLine();
            while(line != null){
                String[] names = line.split(",");
                tiles.get(names[0]).addNeighbor(tiles.get(names[1]));
                line = cbr.readLine();
            }
            line = tbr.readLine();
            int idx = 1;
            while(line!=null) {
                String name = "Tile"+Integer.toString(idx);
                String[] koord = line.split(" ");
                texts.put(name, new Text(Integer.parseInt(koord[0]), Integer.parseInt(koord[1]),""));
                pGame.getChildren().add(texts.get(name));
                idx++;
                line = tbr.readLine();
            }
            
            line = pbr.readLine();
            while(line != null){
                String[] names = line.split(",");
                if(names[1].equals("orangutan")) {
                    if(o1==null) {
                        o1 = new Orangutan();
                        tiles.get(names[0]).setElement(o1);
                        String keyString = getKey(tiles, tiles.get(names[0]));
                        texts.get(keyString).setText("O1");
                        o1.setTile(tiles.get(names[0]));
                    }
                    else {
                        o2 = new Orangutan();
                        tiles.get(names[0]).setElement(o2);
                        String keyString = getKey(tiles, tiles.get(names[0]));
                        texts.get(keyString).setText("O2");
                        o2.setTile(tiles.get(names[0]));
                    }
                }
                else if (names.length < 3)
                    tiles.get(names[0]).setElement(type(names[1], tiles.get(names[0])));
                
                else
                {
                    ext = new Exit(tiles.get(names[2]));
                    felrak("Exit", tiles.get(names[0]));
                    tiles.get(names[0]).setElement(ext);
                }
                line = pbr.readLine();
            }
                cupboards.get("CP1").setPair(cupboards.get("CP2"));
                cupboards.get("CP2").setPair(cupboards.get("CP1"));
            System.out.println("asd");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    //Itt lesz a fő loop, a random dolgok meg ilyesmi.
    public void update(){
        String str; 
        String tmp;
        Text txt;
        for (String key : pandas.keySet())
        {
            tmp = getKey(tiles, pandas.get(key).getTile());
            System.out.println(tmp);
            texts.get(tmp).setText("");
            pandas.get(key).step();
            tmp = getKey(tiles, pandas.get(key).getTile());
            txt = texts.get(tmp);
            str = getKey(pandas, pandas.get(key));
            txt.setText(str);
        }
        
        for (String key : gamemachines.keySet())
        {
            gamemachines.get(key).step();
            tmp = getKey(tiles, gamemachines.get(key).getTile());
            txt = texts.get(tmp);
            if (gamemachines.get(key).getJingled()){
                txt.setFill(Color.RED);
            }
            else{
                txt.setFill(Color.BLACK);
            }   
        }
        
        for (String key : chocomachines.keySet())
        {
            chocomachines.get(key).step();
            tmp = getKey(tiles, chocomachines.get(key).getTile());
            txt = texts.get(tmp);
            if (chocomachines.get(key).getPiped()){
                txt.setFill(Color.RED);
            }
            else{
                txt.setFill(Color.BLACK);
            }
        }
        
        for (String key : armchairs.keySet())
        {
            armchairs.get(key).step();
        }
    }
    
    //Ez fut le, ha megnyomod a bStart gombot
    @FXML
    private void startGame(ActionEvent event) throws IOException {
        bStart.setVisible(false);
        mapPic.setVisible(true);
        pl1Text.setVisible(true);
        pl2Text.setVisible(true);
        player1Points.setVisible(true);
        player1Points.setVisible(true);
        createMap();
        selectedTileO1 = o1.getTile().getNeighbor(0);
        texts.get(getKey(tiles, o1.getTile())).setText("O1");
        if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
            texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
        texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
        selectedTileO2 = o2.getTile().getNeighbor(0);
        texts.get(getKey(tiles, o2.getTile())).setText("O2");
        if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
            texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
        texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
        
        pGame.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(false);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO1)).setText("");
                    selectedTileO1 = o1.getTile().getLeftNeighborOf(selectedTileO1);
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
                    break;
                case D:
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(false);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO1)).setText("");
                    selectedTileO1 = o1.getTile().getRightNeighborOf(selectedTileO1);
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
                    break;    
                case SPACE:
                    Tile previousTileo1 = o1.getTile();
                    o1.move(o1.getTile().getNeighborIndex(selectedTileO1));
                    Tile newTileo1 = o1.getTile();
                    if(previousTileo1 != newTileo1){
                        texts.get(getKey(tiles, selectedTileO1)).setUnderline(false);
                        texts.get(getKey(tiles, newTileo1)).setText("O1");
                        if(o1.getHoldsPanda() == null){
                            texts.get(getKey(tiles, previousTileo1)).setText("");
                        }
                        else{
                            Panda heldo1 = o1.getHoldsPanda();
                            while(heldo1 != null){
                                newTileo1 = previousTileo1;
                                previousTileo1 = heldo1.getTile();
                                texts.get(getKey(tiles, newTileo1)).setText(getKey(pandas, heldo1));
                                texts.get(getKey(tiles, previousTileo1)).setText("");
                                heldo1 = heldo1.getHoldsPanda();
                            }
                        }
                        selectedTileO1 = o1.getTile().getNeighbor(0);
                        if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
                            texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
                        texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
                    }
                    break;
                case LEFT:
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(false);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO2)).setText("");
                    selectedTileO2 = o2.getTile().getLeftNeighborOf(selectedTileO2);
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
                    break;
                case RIGHT:
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(false);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO2)).setText("");
                    selectedTileO2 = o2.getTile().getRightNeighborOf(selectedTileO2);
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
                    break;
                case ENTER:
                    Tile previousTileo2 = o2.getTile();
                    o2.move(o2.getTile().getNeighborIndex(selectedTileO2));
                    Tile newTileo2 = o2.getTile();
                    if(previousTileo2 != newTileo2){
                        texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(false);
                        texts.get(getKey(tiles, newTileo2)).setText("O2");
                        if(o2.getHoldsPanda() == null){
                            texts.get(getKey(tiles, previousTileo2)).setText("");
                        }
                        else{
                            Panda heldo2 = o2.getHoldsPanda();
                            while(heldo2 != null){
                                newTileo2 = previousTileo2;
                                previousTileo2 = heldo2.getTile();
                                texts.get(getKey(tiles, newTileo2)).setText(getKey(pandas, heldo2));
                                texts.get(getKey(tiles, previousTileo2)).setText("");
                                heldo2 = heldo2.getHoldsPanda();
                            }
                        }
                        selectedTileO2 = o2.getTile().getNeighbor(0);
                        if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
                            texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
                        texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
                    }
                    break;
                case ESCAPE:
                    System.exit(0);
                    break;
            }
        });
        
        //Elindítja a timert, megy az update loop
        //timer.start();
    }
    
}
