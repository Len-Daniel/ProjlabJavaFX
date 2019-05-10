package projlab;


import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ricsi
 */
public abstract class Panda extends Moveable implements Steppable{

    private Moveable heldByMoveable;
    private boolean free = true;

	// a lánc felbontásához használt metódus
        public void breakOut() {
            if(getHoldsPanda()!=null) {   //megkeressük azt a pandát, aki már nem fogja senki kezét
                getHoldsPanda().breakOut();
            }
            heldByMoveable.setHoldsPanda(null);    //az előttünk lévő elengedi a mi kezünket
            setHeldByMoveable(null);    //elengedjük az előttünk lévő kezét
	}

	/**
	 * 
	 * @param o: az orangután, ami ütközik az adott pandával
	 */
        @Override
	public void hitBy(Orangutan o) {
            o.add(this);
            setFree(false);
            
	}
        
        @Override
        public void hitBy(Panda pnd){}

	/**
	 * 
	 * @param e
	 */
        @Override
	public void collideWith(Element e) {
            e.hitBy(this);
	}

	/** a panda követi az előtte lévőt, abstract, mivel minden panda típus lépése végén más esemény hívódik meg
	 * 
	 * @param t: az a csempre, amire a pandának lépnie kell majd
	 */
	public abstract void follow(Tile t);

	//lép a panda, abstract, mivel minden panda típus lépése végén más esemény hívódik meg
        @Override
        public abstract void step();

        /**
         * 
         * Ez a metódus az, ami kivezeti a pandát a pályáról
         */
        public void ledOut() {
            if(getHoldsPanda() != null) {   //végigmegyünk az egész láncon, végignövelve az i értékét
                getHoldsPanda().ledOut();
            }
            getTile().remove();   //eltávolítjuk a csempéről a pandát
            Controller.removePanda(this);   //majd a játékból is
            System.out.println("Panda kivezetve.");
	}

	/**
	 * 
	 * @param m: az a mozgó egység, amelyik a panda kezét fogja fogni és így vezetni is azt
	 */
	public void setHeldByMoveable(Moveable m) {
            heldByMoveable = m;
	}

	//lezuhan a panda
        @Override
        public void fall() {
            if(free==false) { breakOut();}   //ha a pandát vezették, akkor felbomlik a mögötte lévő sor
            getTile().remove();   //eltávolítjuk a csempéről
            Controller.removePanda(this);   //majd a játékból is
            System.out.println("A Panda leesett");
	}
        /**
         * 
         * @param f 
         */
        public void setFree(boolean f) {free = f;}
        
        //Ez a metódus visszaadja azt, hogy a panda szabad-e.
        public boolean getFree() {return free;}
        
        //a lehetséges szomszédok közül választ egyet véletlenszerűen
        public int pickRandomNeighbor() {
            Random r = new Random();
            int n = r.nextInt(getTile().getNeighborCount());   //lekérdezzük, hogy a csempének, amin a panda áll, hány szomszédja van
            return n;
        }
}
