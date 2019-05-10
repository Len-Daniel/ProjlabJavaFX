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
public interface Element {
  
    /**
	 * 
	 * @param o
	 */
	void hitBy(Orangutan o);

	/**
	 * 
	 * @param p
	 */
	void hitBy(Panda p);

	/**
	 * 
	 * @param e
	 */
	void collideWith(Element e);

}
