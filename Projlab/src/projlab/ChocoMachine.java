package projlab;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dani
 */
public class ChocoMachine implements Element, Steppable{
    
    //Tárolja, hogy a csokiautomata melyik csempén van.
    private Tile tile;
    
    //True, ha a csokiautomata sípolt
    private boolean piped;
    
    //Meghívja a csempére azt a függvényt, amely invertálja a szomszédos csempék piped változóját.
    void invertPiped() {
        tile.invertNeighborsPiped();
    }
    
    /**
    * 
    * @return 
    * Visszatért a piped attribútum értékével.
    */
    public boolean getPiped() {
            return this.piped;
    }
    
    //Véletlenszerűen meghívja az invertPiped() függvényt.
    @Override
    public void step() {
        if(Math.random() < 0.4){
                invertPiped();
        }
    }
    
    //Nem történik semmi, ha csokiautomatába ütközik orangután.
    @Override
    public void hitBy(Orangutan o) { }
    
    //Nem történik semmi, ha csokiautomatába ütközik panda.
    @Override
    public void hitBy(Panda p) { }
    
    //Csokiautomata nem ütközhet semmibe.
    @Override
    public void collideWith(Element e) { }

    //Beállítja a tile attribútumot az argumentumként kapott tl-re.
    public void setTile(Tile tl){
        tile = tl;
    }
    
    public Tile getTile(){
        return tile;
    }
}
