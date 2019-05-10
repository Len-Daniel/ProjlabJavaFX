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

/**
 * 
 * Speciális csempe, aminek van élettartama. Ha az nullára csökken, akkor a 
 * csempe eltörik, és a rajta álló állat meghal.
 */
public class BreakableTile extends Tile{
    
    //Ha a csempe eléri a nullát, akkor eltörik a csempe. 
    private int lifePoints = 20; 

	/**
	 * Ütközteti az elemeket
	 * @param mvbl Az a Moveable objektum amelyik a mezőre szeretne jönni.
	 */
        @Override
	public void accept(Moveable mvbl) {
            if (this.getMoveable() == null){
                setMoveable(mvbl);
                loseLifePoint();
                mvbl.leave();
                mvbl.setTile(this);
                if(lifePoints < 1){
                    mvbl.fall();
                }
            } else {
                mvbl.collideWith(element);
            }
	}
        
        /**
         * Csökkenti a tile élettartamát. Ha eléri a nullát, akkor minden 
         * állat, ami rálép meghal. 
         */
        @Override
	public void loseLifePoint() {
            if (lifePoints > 0)
                lifePoints--;
            System.out.println("Lifepoints csökkent. Új érték: " + lifePoints);
	}
        
        //Ez a metódus beállítja a setLifePoints változó értékét a paraméterben megadott argumentumra
        public void setLifePoints(int lp){
            lifePoints = lp;
        }
}
