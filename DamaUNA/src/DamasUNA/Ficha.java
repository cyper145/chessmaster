package DamasUNA;



public class Ficha implements Cloneable{


    private Posicion posActual;
    private Posicion posAnterior;
    private int ColorFicha=-1;

   

    /**
     *Guarda True en el caso que sea una ficha blanca. Y false, en el caso que 
     *sea una ficha negra.
     */
  
    public boolean esFichaBlanca;
    private boolean Dama=false;
   
    public Ficha (Posicion val) {
        this.posActual=val;
        this.posAnterior=this.posActual;
    }

    
     public void setColorFicha(int ColorFicha) {
        this.ColorFicha = ColorFicha;
        if(Math.abs(ColorFicha)==2)
            this.Dama=true;
        if(ColorFicha<0)
            this.esFichaBlanca=false;
         else
            this.esFichaBlanca=true;
        
            
    }

    public int getColorFicha() {
        return ColorFicha;
    }
    
    public Posicion getPos () {
        //Posicion p= new Posicion(posActual.fila, posActual.columna);
        return posActual;
    }

 
    public void setPos (Posicion val) {
        
        this.posActual=val;
       
    }

    public Posicion getPosAnterior() {
        return posAnterior;
    }

    public void actualizarPosVieja() {
        this.posAnterior = this.posActual.clone();
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
        this.ColorFicha=this.ColorFicha*2;
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
        
        return (this.posActual.esIgualA(peo) && this.esFichaBlanca== f.esFichaBlanca);
    }
    
    public String toString()
    {
        return this.posActual.toString();
    }
    

}

