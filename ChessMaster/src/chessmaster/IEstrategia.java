package chessmaster;


/**
 *  Es una interfaz que define la estrategia que se va a utilizar para jugar 
 *  el juego.

 */

public abstract class IEstrategia {

    public static int numNodos;
    public abstract Tablero jugar (Tablero actual); 

}

