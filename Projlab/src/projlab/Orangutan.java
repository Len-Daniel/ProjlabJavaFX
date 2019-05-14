package projlab;

import java.util.ArrayList;
import java.util.List;

public class Orangutan extends Moveable{
 
    private int points = 0;
    private int cancatch = 0;
    //private List<Panda> pandas = new ArrayList<Panda>();

	/**
	 * 
	 * @param p A pontszám amit hozzá szeretnénk adni.
         * Ez a metódus a paraméterben megadott pontszámot hozzáadja a játékos pontszámához.
	 */
	public void addPoints(int p) {
		points += p;
		System.out.println(p + " pont hozzáadva, jelenlegi pontok: " + points);
	}
        public int getPoints() {return points;}

        /**
         * Ez a metódus kivezeti azokat a pandákat akiket az orangután 
        */
	public void leadOut() {
            
		if(holdsPanda != null){
                    addPoints(holdsPanda.count(0));
                    holdsPanda.ledOut();
                    holdsPanda=null;
                }
	}
        
        @Override
	public void fall() {
	    System.out.println("Orangután leesett.");
	}

	/**
	 * 
	 * @param pnd Az a panda, amelyik most fog sorba fűződni.
         * Ez a metódus az orangután sorához hozzáadja a paraméterben megkapott pnadát.
	 */
	public void add(Panda pnd, Tile t) 
        {
            if (this.getHoldsPanda()!=null){
                this.getHoldsPanda().setHeldByMoveable(pnd);
                pnd.setHoldsPanda(this.getHoldsPanda());
            }
            this.setTile(t);
            setHoldsPanda(pnd);
	}
        
        //Azt az eseményt kezeli le, amikor orangután ütközik valami mással
    @Override
    public void collideWith(Element lmnt){
        lmnt.hitBy(this);
    }

    public void letOff(){
            if(holdsPanda != null){
             holdsPanda.breakOut();
             System.out.println("Pandak elengedve");
            }
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
