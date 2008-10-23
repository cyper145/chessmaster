package chessmaster;



public class Ficha implements Cloneable{


    private Posicion pos;

    /**
     *Guarda True en el caso que sea una ficha blanca. Y false, en el caso que 
     *sea una ficha negra.
     */
  
    public boolean esFichaBlanca;
    private boolean Dama=false;
   
    public Ficha (Posicion val) {
        this.pos=val;
    }

   
    public Posicion getPos () {
        //Posicion p= new Posicion(pos.fila, pos.columna);
        return pos;
    }

 
    public void setPos (Posicion val) {
       //this.pos.fila = val.fila;
       //this.pos.columna=val.columna;
        this.pos=val;
       
    }
       
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
    
    public boolean isDama() {
        return Dama;
    }

    public void setDama(boolean Dama) {
        this.Dama = Dama;
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


    

    public boolean esIgualA(Ficha f)
    {
        Posicion peo= f.getPos();
        
//        return (this.pos.esIgualA(peo) && this.esFichaBlanca== f.esFichaBlanca);
        return ( this.esFichaBlanca== f.esFichaBlanca);
    }
    

}

