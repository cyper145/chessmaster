package chessmaster;

import java.util.ArrayList; 
import java.util.logging.Level;
import java.util.logging.Logger;


public class Tablero {

    /**
     * Indica el turno a quien le toca jugar. Si el turno es de las blancas, el valor 
     * es true, si el turno es de las negras, el valor es false.
     */
    private boolean TurnoBlancas=true;

    /**
     * Como las instancias de esta clase van a ser los nodos de un arbol, esta 
     * variable indica el nivel en que se encuentra el nodo.
     */
    private int Nivel;

    /**
     * Es un vector de sucesores. Que indica todos los hijos que tiene este 
     * nodo.
     */
    private ArrayList<Tablero> Sucesores;


    private Ficha [][] Fichas;
    private int[][] Tabla;

    public Tablero(int[][] matTabla) {
        Fichas = new Ficha[8][8];
        this.Tabla = matTabla;
        for (int i = 0; i < matTabla.length; i++) {
            for (int j = 0; j < matTabla.length; j++) {
                //Si es positivo es Max, sino Min.
                if (((i + j) % 2) == 0) 
                {
                    if (matTabla[i][j] > 0) 
                    {
                        Ficha peo;
                        Posicion p = new Posicion(i, j);
                        //Si es 1 es peon, sino es Dama
                        if (matTabla[i][j] == 1) {
                            peo = new Peon(p);
                        } else {
                            peo = new Dama(p);
                        }
                        peo.esFichaBlanca = true;
                        Fichas[i][j]=peo;
                    } else if (matTabla[i][j] < 0) {
                        //Las fichas Min son negativas
                        Ficha peo;
                        Posicion p = new Posicion(i, j);
                        //Si es -1 es peon, sino es Dama
                        if (matTabla[i][j] == -1) {
                            peo = new Peon(p);
                        } else {
                            peo = new Dama(p);
                        }
                        peo.esFichaBlanca = false;
                        Fichas[i][j]=peo;
                    }
                }



            }
        }
    }

   
    public int getNivel () {
        return Nivel;
    }


    public void setNivel (int val) {
        this.Nivel = val;
    }

   
    public boolean getTurnoBlancas () {
        return this.TurnoBlancas;
    }
    
    /**
     * Devuelve el estado del juego en una matriz numerica donde los numeros positivos le
     * corresponde a las piezas de un jugador, y numeros negativos que corresponde a las
     * piezas del contrincante. El numero 1 es un peon, 2 es una dama y 0 una casilla vacia.
     * @return una Matriz de enteros que representa un estado.
     */
    public int[][] getTablero() {
        return this.Tabla.clone();
    }
    
    
  
    public void setTurnoBlancas (boolean val) {
        this.TurnoBlancas = val;
    }

    /**
     * Genera todos los posibles estados sucesores que puede tener un 
     * determinado estado, columna lo guarda en el vector Sucesores. Retorna false, 
     * si es que no se pueden generar nuevos sucesores.
     *
     */

    public boolean GenerarSucesores () {
        return true;
    }


    public ArrayList<Tablero> getSucesores () {
        return Sucesores;
    }

  
    public void setSucesores (ArrayList<Tablero> val) {
        this.Sucesores = val;
    }

    /**
     * Calcula un numero que se le asigna al estado dependiendo de la 
     * heuristica.
     * La heuristica es la suma ponderada del valor de la pieza (3 si es dama, 1 peon) por un peso
     * dependiento de la posicion que se encuentre la piesa.
     * Se les da mayor peso a las piesas que se encuentran en los extremos del trablaro.     
     */
     public double CalcularValorDeEstado () {
        
         double sumaPonderada=0;
         double sumaPesos=0;
         for (int i = 0; i < this.Fichas.length; i++) {
             for (int j = 0; j < Fichas.length; j++) {

                 if (((i + j) % 2) == 0) 
                 {
                    Ficha f1 = this.Fichas[i][j];

                    //Todas las fichas del que le toca jugar 
                     if (f1.esFichaBlanca == this.TurnoBlancas) 
                     {
                         int valorPiesa;
                         Posicion pos1 = f1.getPos();

                         if (f1.isDama()) {
                             valorPiesa = 3;
                         } else {
                             valorPiesa = 1;
                         }
                         try {
                             sumaPesos = sumaPesos + pos1.PesoPos();
                             sumaPonderada = sumaPonderada + valorPiesa * pos1.PesoPos();
                         } catch (Exception ex) {
                             Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
                         }


                     }
                 }
                 
             }

         }
         
         return (sumaPonderada/sumaPesos);
    }

    
     
    /**
     * Retorna true, si es el estado final que se encuentra en el nivel limite.
     * @param nivel Indica el nivel limite al que se puede llegar.
     * @return True si ya es el estado final, y false en caso contrario.
     */
    public boolean esEstadoFinal (int nivel) {
        return this.Nivel==nivel;
    }

    /**
     * Come todas las fichas del adversario que puede comer la Ficha pasada 
     * como parametro. Retorna true si existen mas fichas para comer o false si 
     * es que ya no existen fichas para comer.
     */
    public boolean ComerUnaFicha (Ficha comedora) {
        
        return true;
    }

    /**
     *Retorna true si se puede comer una ficha columna false si es que no fichas por 
     *comer. 
     */

    public boolean puedeComer(Ficha comedora) {
        
        Posicion pos= comedora.getPos();
        int valor=0;
        if(this.TurnoBlancas)
             valor=1;
        else
             valor=-1;
        
        //Ver si se puede comer a la derecha.
        Posicion posAux= new Posicion(pos.fila+(valor), pos.columna+(valor));
        if (comedora.isDama()) {
            for (int i = 0; i < 2; i++) {
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    while (posAux.esValida() && this.Fichas[posAux.fila][posAux.columna] == null) {
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                    }

                    if (posAux.esValida() && comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca) {
                        //Como no esta vacia, puede ser que tenemos algo para comer. 
                        //Vemos si hay casillas vacias contiguas a la pieza a comer.
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                        if (posAux.esValida()) {
                            if (this.Fichas[posAux.fila][posAux.columna] == null) {
                                return true;
                            }
                        }
                    }

                }
                //Ver si se puede comer a la izquierda.
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna - (valor);
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    while (posAux.esValida() && this.Fichas[posAux.fila][posAux.columna] == null) {
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna - (valor);
                    }

                    if (posAux.esValida() && comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca) {
                        //Como no esta vacia, tenemos algo por comer. 
                        //Vemos si hay casillas vacias contiguas a la pieza a comer.
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna - (valor);
                        if (posAux.esValida()) {
                            if (this.Fichas[posAux.fila][posAux.columna] == null) {
                                return true;
                            }
                        }
                    }

                }
                valor = valor * (-1);
            }
            return false;
        }else
        {
            
            if(posAux.esValida())
            {
                //Vemos si la casilla esta vacia.
                if(this.Fichas[posAux.fila][posAux.columna]==null)
                    return false;
                else if(comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca )
                {
                    //Como no esta vacia, puede ser que tenemos algo para comer. 
                    //Vemos si hay casillas vacias contiguas a la pieza a comer.
                    posAux.fila=posAux.fila+(valor);
                    posAux.columna=posAux.columna+(valor);
                    if(posAux.esValida())
                    {
                        if(this.Fichas[posAux.fila][posAux.columna]==null)
                            return true;
                    }
                }
                    
            }
            //Ver si se puede comer a la izquierda.
            posAux.fila= pos.fila+(valor);
            posAux.columna=pos.columna-(valor);
            if(posAux.esValida())
            {
                //Vemos si la casilla esta vacia.
                if(this.Fichas[posAux.fila][posAux.columna]==null)
                    return false;
                else if(comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca )
                {
                    //Como no esta vacia, tenemos algo por comer. 
                    //Vemos si hay casillas vacias contiguas a la pieza a comer.
                    posAux.fila=posAux.fila+(valor);
                    posAux.columna=posAux.columna-(valor);
                    if(posAux.esValida())
                    {
                        if(this.Fichas[posAux.fila][posAux.columna]==null)
                            return true;
                    }
                }
                    
            }
            return false;
        }
        
    }

   
    public Ficha [][] getFichas () {
        return Fichas;
    }

   
    public void setFichas (Ficha [][] val) {
        this.Fichas = val;
    }

/**
 * 
 * @param f1 ficha a ser borrada.
 * @return True si la pieza fue borrada con exito, y false si la pieza no pudo ser borrada.
 */
    public boolean BorrarFicha (Ficha f1) {
        if(this.Fichas[f1.getPos().fila][f1.getPos().columna]!=null)
        {
            this.Fichas[f1.getPos().fila][f1.getPos().columna]=null;
            return true;
        }
        return false;
    }

}

