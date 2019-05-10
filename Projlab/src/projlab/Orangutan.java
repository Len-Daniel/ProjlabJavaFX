package projlab;

import java.util.ArrayList;
import java.util.List;

public class Orangutan extends Moveable{
 
    private int points = 0;
    private int cancatch = 0;
    private List<Panda> pandas = new ArrayList<Panda>();

	/**
	 * 
	 * @param p A pontszám amit hozzá szeretnénk adni.
         * Ez a metódus a paraméterben megadott pontszámot hozzáadja a játékos pontszámához.
	 */
	public void addPoints(int p) {
		points += p;
		System.out.println(p + " pont hozzáadva, jelenlegi pontok: " + points);
	}

        /**
         * Ez a metódus kivezeti azokat a pandákat akiket az orangután 
        */
	public void leadOut() {
            
		if(holdsPanda != null){
                    addPoints(holdsPanda.count(0));
                    holdsPanda.ledOut();
                }
	}
        
        @Override
	public void fall() {
            Controller.endGame();
	    System.out.println("Orangután leesett.");
	}

	/**
	 * 
	 * @param pnd Az a panda, amelyik most fog sorba fűződni.
         * Ez a metódus az orangután sorához hozzáadja a paraméterben megkapott pnadát.
	 */
	public void add(Panda pnd) {
        if (pandas.size() == 0){
            pandas.add(pnd);
        } else {
            pnd.setHoldsPanda(pandas.get(pandas.size() - 1));
            pandas.add(pnd);
        }
	}
        
        //Azt az eseményt kezeli le, amikor orangután ütközik valami mással
    @Override
    public void collideWith(Element lmnt){
        lmnt.hitBy(this);
    }

    public void letOff(){
	    pandas.get(0).breakOut();
    }

    @Override
    public void hitBy(Orangutan o) {
        if(o.getCanCatch()==0)
            o.steal(this);
    }

    @Override
    public void hitBy(Panda p) {}
    
    /**
     * 
     * @param o 
     */
    public void steal(Orangutan o){
        
    }
    
    @Override
    public void move(int idx) {
        if (cancatch > 0)
            cancatch--;
        Tile oldTile = getTile();
        getTile().getNeighbor(idx).accept(this);
        if(holdsPanda != null) holdsPanda.follow(oldTile);
    }
    
    public int getCanCatch(){
        return cancatch;
    }
}
