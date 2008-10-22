package chessmaster;



public class Dama extends Ficha {


    public Dama (Posicion p) {
        super(p);
        this.setDama(true);
    }

    /**
     * Mueve al peon a la posicion recibida como parametro. Retorna false, si 
     * el movimiento no se puede realizar. Un movimiento no se podra realizar 
     * si se le exige al peon saltar mas de una casilla.
     */
    public boolean mover (Posicion pos) {
        if(!pos.esValida())
            return false;
        else
        {            
            this.setPos(pos);
            return true;
        }
    }

    /**
     * Retorna true, si la ficha this, puede comer a la ficha mandada como 
     * parametro.
     */
    
    public boolean puedeComer (Ficha f1) {
        return true;
    }

}

