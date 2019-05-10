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
public class Cupboard implements Element {
    
    //A szekrény "párja." Az a hely, ahová a moveable menni fog, ha ütközött a szekrénnyel.
    private Cupboard pair;
    //A csempe, amelyen a szekrény elhelyezkedik.
    private Tile tile;
    
    //alapértelmezett konstruktor
    public Cupboard(){}
    
    //Konstruktor, megadhatjuk neki, hogy melyik szekrény a párja
    public Cupboard(Cupboard pair){
        this.pair = pair;
    }
    
    //Konstruktor, megadhatjuk neki, hogy melyik csempén van
    public Cupboard(Tile tile){
        this.tile = tile;
    }
    
    //Ez a metódus beállítja a pair értékét a paraméterben megkapott értékre.
    void setPair(Cupboard p){
        pair = p;
    }
    
    //Ez a metódus visszaadja a pair attribútumot.
    public Cupboard getPair(){
        return pair;
    }
    
    //Meghívja a teleportTo függvényt az orangutánra, ha az a szekrénnyel ütközött.
    @Override
    public void hitBy(Orangutan o) {
        pair.teleportTo(o);
    }
    
    //Meghívja a teleportTo függvényt a pandára, ha az a szekrénnyel ütközött.
    @Override
    public void hitBy(Panda p) {
        pair.teleportTo(p);
    }
    
    //Áthelyezi a moveablet a szekrény szomszédos csempék egyikére.
    private void teleportTo(Moveable mvbl) {
        tile.placeMoveableOnNeighbor(mvbl);
        System.out.println("Moveable Teleportált");
    }
    
    //Visszaadja a csempét, amelyen a szekrény van.
    public Tile getTile(){
        return tile;
    }

    //Beállítja a tile attribútumot az argumentumként kapott tl-re.
    public void setTile(Tile tl){
        tile = tl;
    }
    
    //Szekrény nem ütközhet semmivel.
    @Override
    public void collideWith(Element e) { }
    
}
