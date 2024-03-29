package chessmaster;

import java.util.Vector;



public class Aleatorio extends IEstrategia {

   
    public Aleatorio () {
    }

   
    public Tablero jugar (Tablero actual) {
        int valor = 0;
        actual.GeneraradorAleatorio();
        Vector sucesores = actual.getSucesores();
        if(sucesores != null && sucesores.size()!= 0){
              valor = (int)(Math.random()*sucesores.size());
              return (Tablero)sucesores.get(valor);
        }
        
        return null;
    }

}

