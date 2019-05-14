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
    /**
     * Játék vége
     */
    public static void endGame() {
        endGame = true;
    }
    
    /**
     * Összes csempe visszaadása
     * @return 
     */
    public Map<String, Tile> getTiles(){
        return tiles;
    }

    /**
     * Panda elvétele
     * @param pnd 
     */
    public static void removePanda(Panda pnd) {
        pandas.remove(pnd);
    }
    
    /**
     * Orángután hozzáadása
     * @param o
     * @param name 
     */
    public void add(Orangutan o, String name){
        orangutans.put(name, o);
    }
    
    /**
     * Panda hozzáadása
     * @param p
     * @param name 
     */
    public void add(Panda p, String name){
        pandas.put(name, p);
    }

    /**
     * Elem hozzáadása
     * @param e
     * @param name 
     */
    public void add(Element e, String name){
        elements.put(name, e);
    }
    
    /**
     * Tile hozzáadása
     * @param t
     * @param name 
     */
    public void add(Tile t, String name){
        tiles.put(name, t);
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
    * Mozgó elem elhelyezése a csempén
    * @param p
    * @param tile 
    */
    public void place(Moveable p, Tile tile){
        tile.setMoveable(p);
    }
    
    /**
     * Elem elhelyezése a csempén 
     * @param p
     * @param tile 
     */
    public void place(Element p, Tile tile){
        tile.setElement(p);
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
