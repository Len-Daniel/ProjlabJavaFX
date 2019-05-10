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
public class PipingFearPanda extends Panda{
    
        //a panda ha megijed ugrik egyet
        public void jump() {
            if(getTile().getPiped()) {   //megnézzük, hogy az adott csempe bármelyik szomszédja sípolt-e
                getTile().loseLifePoint();   //ha igen és törékeny csempén állunk, akkor csökkentjük a csempre életpontjait
                System.out.println("PipingFearPanda ugrott.");
            }
	}

        //Ez a metódus lépteti a pandát
    @Override
	public void step() {
            if(getFree()) {   //megnézzük, hogy szabad-e a panda, ha igen akkor léptetjük
                move(pickRandomNeighbor());
            }
            jump();
	}

        //Ez a metódus követi a pandát kézen fogó moveable-t a paraméterként megadott mezőre
    @Override
        public void follow(Tile t) {
            t.accept(this);
            if(getHoldsPanda()!=null) {   //ha az adott pandát követi egy másik, akkor neki is meghívjuk a követési metódusát
                getHoldsPanda().follow(t);
            }
            jump();
        }
}
