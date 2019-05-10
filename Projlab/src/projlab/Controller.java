package projlab;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    
    private static boolean endGame;
    

    //Átalakítottam a sima listeket mapre, hogy név szerint lehessen keresni objektumokat
    //Pandák tárolása
    private static Map<String, Panda> pandas = new HashMap<String, Panda>();

    //A csempéket tárolja
    private static Map<String, Tile> tiles = new HashMap<String, Tile>();

    //Az orángutánokat tárolja
    private static Map<String, Orangutan> orangutans = new HashMap<String, Orangutan>(2);

    //Minden elemet tárolja
    private static Map<String, Element> elements = new HashMap<String, Element>();
    
    //Alapértelmezett konstruktor
    Controller(){}

    //visszaadja az orangután attribútumot
    //public Orangutan getOrangutan(){return orangutan;}
    
    //Elindítja a játékot.
    public void startGame() {
        /*
        Tile middle = new Tile();
        Tile bottom = new Tile();
        middle.setNeighborAt(2, bottom);
        bottom.setNeighborAt(0, middle);
        Tile right = new Tile();
        middle.setNeighborAt(1, right);
        right.setNeighborAt(3, middle);
        Tile left = new Tile();
        middle.setNeighborAt(3, left);
        left.setNeighborAt(1, middle);
        Armchair ac = new Armchair();
        Tile top = new Tile(ac);
        ac.setTile(top);
        Panda sp = new SleepyPanda();
        Controller.getInstance().addPanda(sp);
        right.setMoveable(sp);
        orangutan = new Orangutan();
        middle.setMoveable(orangutan);*/
        
        endGame = false;
        try{
            while(!endGame){
                if(pandas.isEmpty()) endGame();
                Thread.sleep(2000);
            }
        } catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
        System.out.println("Game Over!");
    }

    //Lezárja a játékot
    public static void endGame() {
        endGame = true;
    }
    
    
    //visszaadja az összes csempét
    public Map<String, Tile> getTiles(){
        return tiles;
    }

    //elvesz egy pandát a játékból
    public static void removePanda(Panda pnd) {
        pandas.remove(pnd);
    }

    /**
     * Hozzáad egy megfelelő objektumot ahhoz a listához, mihez tartozik
     *
     * Vannak bennne a kiíratások, amik csak teszteléshez kellettek
     *
     * @param tmp   Az átadandó objektum
     * @param name  Az átandó objektm neve
     */
    public void add(Object tmp, String name) {
        if (tmp instanceof Orangutan){
            orangutans.put(name, (Orangutan) tmp);
            System.out.println(orangutans.size() + " orangutans");
        } else if (tmp instanceof Panda){
            pandas.put(name, (Panda)tmp);
            System.out.println(pandas.size() + " pandas");
        } else if (tmp instanceof  Element){
            elements.put(name, (Element)tmp);
            System.out.println(elements.size() + " elements");
        } else if (tmp instanceof Tile){
            tiles.put(name, (Tile)tmp);
            System.out.println(tiles.size() + " tiles");
        }
    }

    /**
     * Keresés a listákban
     *
     * @param tmp   A kinyerendő objektum neve (kulcsa)
     * @return A megtalált objektum
     */
    public Object search(String tmp){
        if ((tiles.get(tmp)) != null){
            return tiles.get(tmp);
        } else if ((elements.get(tmp)) != null){
            return elements.get(tmp);
        } else if ((pandas.get(tmp)) != null){
             return pandas.get(tmp);
        } else if ((orangutans.get(tmp)) != null){
            return orangutans.get(tmp);
        }
        return null;
    }

    /**
     * Ezel lehet elemeket összekapcsolni
     *
     * @param first Kit akarunk összekötni
     * @param side  Ha csempéről van szó, akkor lehet neki szomszédirányt adni
     * @param second    Mivel akarjuk összekötni
     */
    public void connect(String first, int side, String second){
        Object mit = search(first);
        Object kivel = search(second);
        if (mit instanceof Panda && kivel instanceof Moveable){
            ((Panda)mit).setHeldByMoveable((Moveable)kivel);
        } else if (mit instanceof Panda && kivel instanceof Panda){
            ((Panda)mit).setHoldsPanda((Panda)kivel);
        } else if (mit instanceof Tile){
            ((Tile)mit).setNeighborAt(side, (Tile)kivel);
        } else if (mit instanceof Cupboard){
            ((Cupboard)mit).setPair((Cupboard)kivel);
        } else {
            System.err.println("Nem jĂł");
        }
    }

    /**
     * Ellehet helyezni egy objektumot a csempĂ©n
     *
     * @param first Az jelzi, hogy mit akarunk elhelyezni
     * @param second    Hogy min akarjuk elhelyezni
     */

    public void place(String first, String second){
        Object mit = search(first);
        Object min = search(second);
           if (mit instanceof Cupboard && min instanceof Tile){
            ((Tile)min).setElement((Element) mit);
            ((Cupboard)mit).setTile((Tile)min);
        }  else if (mit instanceof ChocoMachine && min instanceof Tile){
            ((Tile)min).setElement((Element) mit);
            ((ChocoMachine)mit).setTile((Tile)min);
        }  else if (mit instanceof GameMachine && min instanceof Tile){
            ((Tile)min).setElement((Element) mit);
            ((GameMachine)mit).setTile((Tile)min);
        } else if (mit instanceof Moveable){
            ((Tile)min).setMoveable((Moveable)mit);
            ((Moveable)mit).setTile((Tile)min);
        } else if (mit instanceof Element && min instanceof Tile){
            ((Tile)min).setElement((Element) mit);
            
        } else {
            System.err.println("Nem jĂł");
        }
    }

    /**
     * Ezzel lehet elkapni egy orangutánnal egy pandát
     *
     * @param orangutan Az orangutén neve
     * @param panda A panda neve
     */
    public void grab(String orangutan, String panda){
        Orangutan oTmp = orangutans.get(orangutan);
        oTmp.setHoldsPanda(pandas.get(panda));
    }

    /**
     * Bármilyen mozdítható elemet mozdít egy irányba
     *
     * @param moveable  A mozgatható objektum neve
     * @param idx   A mozgási irány
     */
    public void move(String moveable, int idx){
        Object tmp = search(moveable);
        ((Moveable)tmp).move(idx);
    }

    /**
     * Arra való, hogy sípoljon a csokiautómata
     *
     * @param chocoMachine  A csokiautómanta neve
     */
    public void pipe(String chocoMachine){
        Object tmp = elements.get(chocoMachine);
        ((ChocoMachine)tmp).invertPiped();
    }

    /**
     * Arra való, hogy csilingeljen a játékautómata
     *
     * @param gameMachine   A csokiautómata neve
     */
    public void jingle(String gameMachine){
        Object tmp = elements.get(gameMachine);
        ((GameMachine)tmp).invertJingled();
    }

    /**
     * A panda ugrása való függvény
     *
     * @param panda Az ugrani képes panda neve
     */
    public void jump(String panda){
        Object tmp = pandas.get(panda);
        ((PipingFearPanda) tmp).jump();
    }

    /**
     * Az orangután elengedi az összes pandáját
     *
     * @param orangutan Az orangután neve
     */
    public void letGo(String orangutan){
        Object tmp = orangutans.get(orangutan);
        ((Orangutan)tmp).letOff();
    }
}
