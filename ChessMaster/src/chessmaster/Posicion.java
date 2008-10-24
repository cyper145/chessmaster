package chessmaster;



public class Posicion {


    public int fila;
    public int columna;

    
    public Posicion (int x, int y) {
        this.fila=x;
        this.columna=y;
    }

    
    public void setPos(Posicion p)
    {
        this.fila=p.fila;
        this.columna=p.columna;
    }

    public boolean esIgualA (Posicion p) {
       
        return (this.fila==p.fila && this.columna==p.columna);
    }

    /**
     *Verifica la validez de una posicion. Una posicion es valida si se 
     *encuentra dentro de las posiciones que puede ocupar una ficha en un 
     *tablero 8 fila 8.
     */
    
    public boolean esValida () {
        if(this.fila < 0 || this.fila > 7 )
            return false;
        else if(this.columna < 0 || this.columna > 7 )
            return false;
        else if(((this.fila+this.columna)%2)!=0)
            return false;
        else 
            return true;
    }
    /**
     * Sirve para darle un peso a cada posicion, si la posicion se encuentra
     * en el recuadro mas grande se le da el mayor valor que es 4, y luego
     * disminuye a medida que disminuye el recuadro.
     * @return el peso dependiendo de la posicion.
     */
    public int PesoPos() 
    {
        if(this.fila==0 || this.columna==0 || fila== 7 || columna== 7)
            return 4;
        else if(fila==1 || columna==1 || fila== 6 || columna== 6)
            return 3;
        else if(fila==2 || columna==2 || fila== 5 || columna== 5)
            return 2;
        else if(fila==3 || columna==3 || fila== 4 || columna== 4)
            return 1;
        else
            return 0;
        
        
    }
    public Posicion clone()
    {
        Posicion p= new Posicion(this.fila, this.columna);
        return p;
    }
    
    public String toString()
    {
        return "( "+this.fila+","+this.columna+" )";
    }
    

}

