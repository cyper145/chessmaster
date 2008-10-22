package chessmaster;



public abstract class Ficha implements Cloneable{


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
        return pos;
    }

 
    public void setPos (Posicion val) {
        this.pos = val;
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
     *  <html>
     *    <head>
     *  
     *    </head>
     *    <body>
     *      <p style="margin-top: 0">
     *        Mueve al peon a la posicion recibida como parametro. Retorna false, si 
     *        el movimiento no se puede realizar. Un movimiento no se podra realizar 
     *        si se le exige al peon saltar mas de una casilla.
     *      </p>
     *    </body>
     *  </html>
     */
  
    public abstract boolean mover (Posicion pos);


    

    public boolean esIgualA(Ficha f)
    {
        Posicion peo= f.getPos();
        return this.pos.esIgualA(peo);
    }
    

   

    /**
     *  <html>
     *    <head>
     *  
     *    </head>
     *    <body>
     *      <p style="margin-top: 0">
     *        Retorna true, si la ficha this, puede comer a la ficha mandada como 
     *        parametro.
     *      </p>
     *    </body>
     *  </html>
     */
   
    public abstract boolean puedeComer (Ficha f1);

}

