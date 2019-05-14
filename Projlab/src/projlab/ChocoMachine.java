package projlab;

/**
 * Csokigépet reprezentálandó osztály
 * @author Dani
 */
public class ChocoMachine implements Element, Steppable{
    /**
     * Tárolja, hogy a csokiautomata melyik csempén van.
     */
    private Tile tile;
    
    /**
     * True, ha a csokiautomata sípolt
     */
    private boolean piped = false;
    
    /**
     * Meghívja a csempére azt a függvényt, amely invertálja a szomszédos csempék piped változóját.
     */
    void invertPiped() {
        piped=!piped;
        tile.invertNeighborsPiped();
    }
    
    /**
    * Visszatért a piped attribútum értékével.
    * @return 
    */
    public boolean getPiped() {
            return this.piped;
    }
    
    /**
     * Véletlenszerűen meghívja az invertPiped() függvényt.
     */
    @Override
    public void step() {
        if(Math.random() < 0.4){
                invertPiped();
        }
    }
    
    /**
     * Nem történik semmi, ha csokiautomatába ütközik orangután.
     * @param o 
     */
    @Override
    public void hitBy(Orangutan o) { }
    
    /**
     * Nem történik semmi, ha csokiautomatába ütközik panda.
     * @param p 
     */
    @Override
    public void hitBy(Panda p) { }
    
    /**
     * Csokiautomata nem ütközhet semmibe.
     * @param e 
     */
    @Override
    public void collideWith(Element e) { }

    /**
     * Beállítja a tile attribútumot az argumentumként kapott tl-re.
     * @param tl 
     */
    public void setTile(Tile tl){
        tile = tl;
    }
    
    /**
     * Visszatér a csempével
     * @return 
     */
    public Tile getTile(){
        return tile;
    }
}
