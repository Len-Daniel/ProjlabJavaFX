package projlab;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ricsi
 */
public class SleepyPanda extends Panda{
    
    private boolean immune = false;
    private int immuneTime = 0;

	/**
	 * 
	 * @param status: a beállítandó status értéke
	 */
	public void setStatus(boolean status) {
            immune = status;
            if(status) { //ha immunissá válik, egyből beállítjuk annak az ideját is
                immuneTime = 3;
            }
	}
        
    /**
     *SleepyPanda step metódusa, amely csökkenti az immunitás idejét,
     * elveszi az immunitását,
     * és ha a panda nincs sorban, akkor véletlenszerű irányba lép
     */
    @Override
	public void step() {
            if(immuneTime>0) { //először csökkentjük az immunitás idejét
                immuneTime--;
                if(immuneTime==0) { //ha 0 lesz, akkor elvesszük az immunitást
                    this.setStatus(false);
                }
            }
            if(getFree()==true) { //ha szabad a panda, akkor lép
                move(pickRandomNeighbor());
            }
            checkChair();
	}
        
        /**
         * 
         * @param t: a csempe, ahova lépnie kell a pandának
         */
        @Override
        public void follow(Tile t) {
            Tile oldTile = getTile();
            t.accept(this);
            if(getHoldsPanda()!=null) {   //ha az adott pandát követi egy másik, akkor neki is meghívjuk a követési metódusát
                getHoldsPanda().follow(oldTile);
            }
            checkChair();
	}
                
        //megnézi, hogy van-e szomszédos és üres fotel
        public void checkChair() {
            if(getTile().getFreeNeighborChair()!=null && !immune) {   //ha van szomszédos üres fotel és nem immunis a panda, akkor beleül
                Armchair arm = getTile().getFreeNeighborChair();
                if (arm != null){
                    arm.gyereRam(this);
                    setFree(false);
                }
                System.out.println("Beleül");
            }
        }
}
