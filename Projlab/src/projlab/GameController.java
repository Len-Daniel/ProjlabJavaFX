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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    
    private double cnt=0.002;
    
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
    @FXML
    private Text gameOverText;

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
                        System.out.println("tick");
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
            List<Integer> bt = new ArrayList<Integer>();
            Integer[] data = {3,9,18,24,34};
            bt = Arrays.asList(data);
            while(line!=null) {
                String name = "Tile"+Integer.toString(idx);
                if (bt.contains(idx))
                    name = "BTile"+Integer.toString(idx);
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
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    //Itt lesz a fő loop, a random dolgok meg ilyesmi.
    public void update(){
        String str; 
        String tmp;
        Text txt;
        
        for (String key : armchairs.keySet())
        {
            armchairs.get(key).step();
            tmp = getKey(tiles, armchairs.get(key).getTile());
            txt = texts.get(tmp);
            if (armchairs.get(key).isFree()==false){
                txt.setFill(Color.YELLOW);
            }
            else{
                txt.setFill(Color.BLACK);
            }
        }
        
        for (String key : pandas.keySet())
        {
            tmp = getKey(tiles, pandas.get(key).getTile());
            if(tmp != null){
                //System.out.println(tmp);
                texts.get(tmp).setText("");
                pandas.get(key).step();
                tmp = getKey(tiles, pandas.get(key).getTile());
                txt = texts.get(tmp);
                str = getKey(pandas, pandas.get(key));
                txt.setText(str);
            } else {
                pandas.get(key).step();
            }
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
        
        /*for (String key : armchairs.keySet())
        {
            armchairs.get(key).step();
        }*/
    }
    
    public void gameOver(){
        timer.stop();
        if(o1.getTile() == null){
            gameOverText.setText("1. játékos leesett, 2. játékos a győztes!");
        }
        else if(o2.getTile() == null){
            gameOverText.setText("2. játékos leesett, 1. játékos a győztes!");
        }
        else if(o1.getPoints() == o2.getPoints()){
            gameOverText.setText("Döntetlen!");
        }
        else if(o1.getPoints() > o2.getPoints()){
            gameOverText.setText("1. játékos a győztes!");
        }
        else
            gameOverText.setText("2. játékos a győztes!");
    }
    
    //Ez fut le, ha megnyomod a bStart gombot
    @FXML
    private void startGame(ActionEvent event) throws IOException {
        bStart.setVisible(false); //A pálya elemeit visible-re állítjuk
        mapPic.setVisible(true);
        pl1Text.setVisible(true);
        pl2Text.setVisible(true);
        player1Points.setVisible(true);
        player2Points.setVisible(true);
        createMap(); //A pála felépítése, változók inicializálása
        
        //A kiválasztott csempék kezdeti pozíciójának beállítása, grafikus megjelenítése
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
        
        //A játékosok inputjainek lekezelése
        pGame.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W: //1. játékos elengedi a pandáit
                    o1.letOff();
                    break;
                case A: //1. játékos a jelenlegi kiválaszott csempének a baloldali szomszédját választja ki
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(false);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO1)).setText("");
                    selectedTileO1 = o1.getTile().getLeftNeighborOf(selectedTileO1);
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
                    break;
                case D: //1. játékos a jelenlegi kiválaszott csempének a jobb oldali szomszédját választja ki
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(false);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO1)).setText("");
                    selectedTileO1 = o1.getTile().getRightNeighborOf(selectedTileO1);
                    Text ta = texts.get(getKey(tiles, selectedTileO1));
                    texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
                    if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
                    break;    
                case SPACE: //1. játékos megpróbál átmozogni a kiválasztott csempére
                    Tile previousTileo1 = o1.getTile();
                    Tile prevTileo2 = o2.getTile();
                    boolean o2wasHolding = false;
                    boolean o1wasntHolding = false;
                    if(o1.getHoldsPanda() == null){
                        o1wasntHolding = true;
                    }
                    if(o2.getHoldsPanda() != null){
                        o2wasHolding = true;
                    }
                    if(o1.getHoldsPanda() != null){
                        Panda heldo1 = o1.getHoldsPanda();
                        while(heldo1 != null){
                            texts.get(getKey(tiles, heldo1.getTile())).setText("");
                            heldo1 = heldo1.getHoldsPanda();
                        }
                    }
                    o1.move(o1.getTile().getNeighborIndex(selectedTileO1));
                    
                    if(o1.getTile() == null){
                        gameOver();
                    }
                    Tile newTileo1 = o1.getTile();
                    if(previousTileo1 != newTileo1 && o1.getTile() != null){
                        texts.get(getKey(tiles, selectedTileO1)).setUnderline(false);
                        texts.get(getKey(tiles, newTileo1)).setText("O1");
                        if(o1.getHoldsPanda() == null){
                            texts.get(getKey(tiles, previousTileo1)).setText("");
                        }
                        if(newTileo1 == prevTileo2 && o2wasHolding == true && o1wasntHolding == true){
                                texts.get(getKey(tiles, previousTileo1)).setText("O2");
                                selectedTileO2 = o2.getTile().getNeighbor(0);
                                if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
                                    texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
                                texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
                        }
                        else{
                            Panda heldo1 = o1.getHoldsPanda();
                            while(heldo1 != null){
                                texts.get(getKey(tiles, heldo1.getTile())).setText(getKey(pandas, heldo1));
                                heldo1 = heldo1.getHoldsPanda();
                            }
                        }
                        selectedTileO1 = o1.getTile().getNeighbor(0);
                        if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
                            texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
                        texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
                    }
                    if(o1.getHoldsPanda() != null && previousTileo1 == newTileo1 && o1.getTile() != null){
                        Panda heldo1 = o1.getHoldsPanda();
                        while(heldo1 != null){
                            texts.get(getKey(tiles, heldo1.getTile())).setText(getKey(pandas, heldo1));
                            heldo1 = heldo1.getHoldsPanda();
                        }
                    }
                    if(Integer.parseInt(player1Points.getText()) != o1.getPoints() && o1.getTile() != null){
                        pandas.entrySet().removeIf(p -> p.getValue().getTile() == null);
                        player1Points.setText(Integer.toString(o1.getPoints()));
                        if(pandas.size() == 0)
                            gameOver();
                    }  
                    break;
                case UP: //2. játékos elengedi a pandáit
                    o2.letOff();
                    break;
                case LEFT: //2. játékos a jelenlegi kiválaszott csempének a baloldali szomszédját választja ki
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(false);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO2)).setText("");
                    selectedTileO2 = o2.getTile().getLeftNeighborOf(selectedTileO2);
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
                    break;
                case RIGHT: //2. játékos a jelenlegi kiválaszott csempének a jobboldali szomszédját választja ki
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(false);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "*selected*")
                        texts.get(getKey(tiles, selectedTileO2)).setText("");
                    selectedTileO2 = o2.getTile().getRightNeighborOf(selectedTileO2);
                    texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
                    if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
                        texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
                    break;
                case ENTER: //2. játékos megpróbál átmozogni a kiválasztott csempére
                    Tile previousTileo2 = o2.getTile();
                    Tile prevTileo1 = o1.getTile();
                    boolean o1wasHolding = false;
                    boolean o2wasntHolding = false;
                    if(o2.getHoldsPanda() == null){
                        o2wasntHolding = true;
                    }
                    if(o1.getHoldsPanda() != null){
                        o1wasHolding = true;
                    }
                    if(o2.getHoldsPanda() != null){
                        Panda heldo2 = o2.getHoldsPanda();
                        while(heldo2 != null){
                            texts.get(getKey(tiles, heldo2.getTile())).setText("");
                            heldo2 = heldo2.getHoldsPanda();
                        }
                    }
                    o2.move(o2.getTile().getNeighborIndex(selectedTileO2));
                    if(o2.getTile() == null){
                        gameOver();
                    }
                    Tile newTileo2 = o2.getTile();
                    if(previousTileo2 != newTileo2 && o2.getTile() != null){
                        texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(false);
                        texts.get(getKey(tiles, newTileo2)).setText("O2");
                        if(o2.getHoldsPanda() == null){
                            texts.get(getKey(tiles, previousTileo2)).setText("");
                        }
                        if(newTileo2 == prevTileo1 && o1wasHolding == true && o2wasntHolding == true){
                                texts.get(getKey(tiles, previousTileo2)).setText("O1");
                                
                                selectedTileO1 = o1.getTile().getNeighbor(0);
                                if(texts.get(getKey(tiles, selectedTileO1)).getText() == "")
                                    texts.get(getKey(tiles, selectedTileO1)).setText("*selected*");
                                texts.get(getKey(tiles, selectedTileO1)).setUnderline(true);
                        }
                        else{
                            Panda heldo2 = o2.getHoldsPanda();
                            while(heldo2 != null){
                                texts.get(getKey(tiles, heldo2.getTile())).setText(getKey(pandas, heldo2));
                                heldo2 = heldo2.getHoldsPanda();
                            }
                        }
                        selectedTileO2 = o2.getTile().getNeighbor(0);
                        if(texts.get(getKey(tiles, selectedTileO2)).getText() == "")
                            texts.get(getKey(tiles, selectedTileO2)).setText("*selected*");
                        texts.get(getKey(tiles, selectedTileO2)).setStrikethrough(true);
                    }
                    if(o2.getHoldsPanda() != null && previousTileo2 == newTileo2 && o2.getTile() != null){
                        Panda heldo2 = o2.getHoldsPanda();
                        while(heldo2 != null){
                            texts.get(getKey(tiles, heldo2.getTile())).setText(getKey(pandas, heldo2));
                            heldo2 = heldo2.getHoldsPanda();
                        }
                    }
                    if(Integer.parseInt(player2Points.getText()) != o2.getPoints() && o2.getTile() != null){
                        player2Points.setText(Integer.toString(o2.getPoints()));
                        pandas.entrySet().removeIf(p -> p.getValue().getTile() == null);
                        if(pandas.size() == 0)
                            gameOver();
                    }
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
