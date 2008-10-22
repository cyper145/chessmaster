package chessmaster;


/**
 *Una dama se diferencia del peon en los lugares que se puede mover. 
 *Una dama puede saltar varias casillas de una vez. Un peon solo puede 
 *saltar una casilla a la vez.
 *Un peon solo puede avanzar, no puede retroceder. Una dama puede avanzar o 
 *retroceder.
 */

public class Peon extends Ficha {


    public Peon (Posicion p) {
        super(p);
    }

    /**
     *    Mueve al peon a la posicion recibida como parametro. Retorna false, si 
     *    el movimiento no se puede realizar. Un movimiento no se podra realizar 
     *    si se le exige al peon saltar mas de una casilla.
     */

    public boolean mover (Posicion pos) {
        if(!pos.esValida())
            return false;
        else
        {
            Posicion p1= this.getPos();
            
            this.setPos(pos);
            return true;
        }
        
    }

   /**
     *    Retorna true, si la ficha this, puede comer a la ficha mandada como 
     *    parametro.
     */
    
    public boolean puedeComer (Ficha f1) {
        return true;
    }

}

