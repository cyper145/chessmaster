package chessmaster;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablero implements Cloneable {

    /**
     * Indica el turno a quien le toca jugar. Si el turno es de las blancas, el valor 
     * es true, si el turno es de las negras, el valor es false.
     */
    private boolean Turno;
    /**
     * Como las instancias de esta clase van a ser los nodos de un arbol, esta 
     * variable indica el nivel en que se encuentra el nodo.
     */
    private int Nivel=0;
    /**
     * Es un vector de sucesores. Que indica todos los hijos que tiene este 
     * nodo.
     */
    private Vector<Tablero> Sucesores= new Vector();
    public static int cantidadPiezasComidas = 0;
    public static Vector estadoPiezasComidas = new Vector();
    public Vector VectorDeComidas= new Vector();
    private Ficha[][] Fichas;
    private int[][] Tabla;
    public Ficha fichaMovida;// Es la ficha que se movio.
    public double puntaje=0;
    
    public double alfa= Double.NEGATIVE_INFINITY;
    public double beta= Double.POSITIVE_INFINITY;
    public Tablero(int[][] matTabla) {
        Fichas = new Ficha[8][8];
        this.Tabla = matTabla;
        for (int i = 0; i < matTabla.length; i++) {
            for (int j = 0; j < matTabla.length; j++) {
                //Si es positivo es Max, sino Min.
                if (((i + j) % 2) == 0) {
                    if (matTabla[i][j] > 0) {
                        Posicion p = new Posicion(i, j);
                        Ficha peo=new Ficha(p);
                        //Si es 1 es peon, sino es Dama
                        if (matTabla[i][j] == 1) {
                            peo.setDama(false);
                        } else {
                            peo.setDama(true);
                        }
                        peo.esFichaBlanca = true;
                        Fichas[i][j] = peo;
                       
                    } else if (matTabla[i][j] < 0) {
                        //Las fichas Min son negativas
                        Posicion p = new Posicion(i, j);
                        Ficha peo=new Ficha(p);
                        //Si es -1 es peon, sino es Dama
                        if (matTabla[i][j] == -1) {
                            peo.setDama(false);
                        } else {
                            peo.setDama(true);
                        }
                        peo.esFichaBlanca = false;
                        Fichas[i][j] = peo;
                        
                    }
                }



            }
        }
    }

    private Tablero() {
    }

    public int getNivel() {
        return Nivel;
    }

    public void setNivel(int val) {
        this.Nivel = val;
    }

    public boolean getTurnoBlancas() {
        return this.Turno;
    }

    /**
     * Devuelve el estado del juego en una matriz numerica donde los numeros positivos le
     * corresponde a las piezas de un jugador, y numeros negativos que corresponde a las
     * piezas del contrincante. El numero 1 es un peon, 2 es una dama y 0 una casilla vacia.
     * @return una Matriz de enteros que representa un estado.
     */
    public int[][] getTablero() {
        int [][] tabla = new int[8][8];        
        for (int i = 0; i < this.Fichas.length; i++) {
            for (int j = 0; j < this.Fichas.length; j++) {
                //Si es positivo es Max, sino Min.
                if (((i + j) % 2) == 0 && Fichas[i][j] != null) {
                    if (Fichas[i][j].esFichaBlanca) {                        
                        //Si es 1 es peon, sino es Dama
                        if (Fichas[i][j].isDama()) {
                            tabla[i][j] = 2;
                        } else {
                            tabla[i][j] = 1;
                        }                        
                    }
                    if (!Fichas[i][j].esFichaBlanca) {
                        //Las fichas Min son negativas                        
                        if (Fichas[i][j].isDama()) {
                            tabla[i][j] = -2;
                        } else {
                            tabla[i][j] = -1;
                        }
                    }
                }
            }
        }
        return tabla;
    }

    public void setTurnoBlancas(boolean val) {
        this.Turno = val;
    }
    public void GenerarSucesores()
    {
        //Con false hacemos que primero coma si puede comer y luego se mueva si se puede mover.
        //Si era true, seria al reves.
        this.GenerarSucesores(false);
    }

    /**
     * Genera todos los posibles estados sucesores que puede tener un 
     * determinado estado, columna lo guarda en el vector Sucesores. Retorna false, 
     * si es que no se pueden generar nuevos sucesores.
     *
     */
    public void GenerarSucesores(boolean esMin) 
    {
        int newLevel=this.Nivel+1;
        for (int i = 0; i < Fichas.length; i++) {
            for (int j = 0; j < Fichas.length; j++) {
                if((i+j)%2==0 && this.Fichas[i][j]!=null)
                {
                    Ficha f1=this.Fichas[i][j];
                    Vector estadosMovidas;
                    
                    //Si la ficha es del turno creamos los sucesores.
                    if(f1.esFichaBlanca==this.Turno)
                    {
                        
                        // Sucesores generados por comer piezas
                        this.cantidadPiezasComidas=0;
                        this.estadoPiezasComidas.clear();//Limpio el vector estatico.
                        f1.actualizarPosVieja();
                        if(this.puedeComer(f1))
                            this.ComerMayorCant(f1, 0, new Vector());
                        //Sucesores generados
                        f1.actualizarPosVieja();
                        estadosMovidas= this.MoverFicha(f1);
                        
                        if(esMin)
                        {
                            // Agregamos los estados generados en el vector sucesores.
                            for (int k = 0; k < estadosMovidas.size(); k++) {
                                Tablero t1 = (Tablero)estadosMovidas.get(k);
                                t1.Nivel=newLevel;// Le digo al estado en que nivel esta.
                                this.Sucesores.add(t1);
                            
                            }
                            //Si hay algo de piezas comidas, lo agrego como sucesores.
                            for (int k = 0; k < this.estadoPiezasComidas.size(); k++) {
                                Tablero t1= (Tablero)this.estadoPiezasComidas.get(k);
                                t1.Nivel=newLevel;// Le digo al estado en que nivel esta.
                                this.Sucesores.add(t1);
                            }
                        }else{
                            //Si hay algo de piezas comidas, lo agrego como sucesores.
                            for (int k = 0; k < this.estadoPiezasComidas.size(); k++) {
                                Tablero t1= (Tablero)this.estadoPiezasComidas.get(k);
                                t1.Nivel=newLevel;// Le digo al estado en que nivel esta.
                                this.Sucesores.add(t1);
                            }
                            // Agregamos los estados generados en el vector sucesores.
                            for (int k = 0; k < estadosMovidas.size(); k++) {
                                Tablero t1 = (Tablero)estadosMovidas.get(k);
                                t1.Nivel=newLevel;// Le digo al estado en que nivel esta.
                                this.Sucesores.add(t1);
                            
                            }
                            
                        }
                        
                        
                        
                        
                       
                            
                    }
                        
                }
                
            }
            
        }
        
    }

    public Vector<Tablero> getSucesores() {
        return Sucesores;
    }

    public void setSucesores(Vector<Tablero> val) {
        this.Sucesores = val;
    }

    

    /**
     * Calcula un numero que se le asigna al estado dependiendo de la 
     * heuristica.
     * La heuristica es la suma ponderada del valor de la pieza (3 si es dama, 1 peon) por un peso
     * dependiento de la posicion que se encuentre la piesa.
     * Se les da mayor peso a las piesas que se encuentran en los extremos del trablaro.     
     */
    public void CalcularValorDeEstado() {

        double sumaPonderada = 0;
       
        for (int i = 0; i < this.Fichas.length; i++) {
            for (int j = 0; j < this.Fichas.length; j++) {

                if (((i + j) % 2) == 0 && this.Fichas[i][j]!=null) {
                    Ficha f1 = this.Fichas[i][j];

                    //Todas las fichas del que le toca jugar 
                    if (f1.esFichaBlanca == this.Turno) {
                        int valorPiesa;
                        Posicion pos1 = f1.getPos();

                        if (f1.isDama()) {
                            valorPiesa = 3;
                        } else {
                            valorPiesa = 1;
                        }
                                               
                            sumaPonderada = sumaPonderada + valorPiesa * pos1.PesoPos();
                        


                    }
                }

            }

        }

        this.puntaje=this.cantidadPiezasComidas+sumaPonderada ;
    }

   

    
    /**
     * Verifica si el estado t2 ya existe dentro del vector de estados de piezas comedoras.
     * @param t2 Estado a comparar.
     * @return True si ya es un estado repetido, y false en caso contrario.
     */
    public boolean EsRepetidoComido(Tablero t2)
    {
        for (int i = 0; i < this.estadoPiezasComidas.size(); i++) {
            Tablero t1= (Tablero)this.estadoPiezasComidas.get(i);
            if(t2.esIgualA(t1))
                return true;
        }
        return false;
    }

    /**
     *Retorna true si se puede comer una ficha columna false si es que no fichas por 
     *comer. 
     */
    public boolean puedeComer(Ficha comedora) {

        Posicion pos = comedora.getPos();
        int valor = 0;
        if (this.Turno) {
            valor = 1;
        } else {
            valor = -1;        //Ver si se puede comer a la derecha.
        }
        Posicion posAux = new Posicion(pos.fila + (valor), pos.columna + (valor));
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
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna + (valor);
            }
            return false;
        } else {

            if (posAux.esValida()) {
                //Vemos si la casilla esta vacia.
                if (this.Fichas[posAux.fila][posAux.columna] != null && comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca) {
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
                if (this.Fichas[posAux.fila][posAux.columna] != null && comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca) {
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
            return false;
        }

    }

    /**
     * Guarda en un vector estatico todos los estados que tengan la mayor cantidad de piezas comidas.
     * No existen estados repetidos.
     * @param comedora Pieza que queremos que coma piezas.
     * @param cantidad Cantidad de piezas que se ha comido.
     */
    @SuppressWarnings("static-access")
    public void ComerMayorCant(Ficha comedora, int cantidad, Vector <Ficha> fichasComidas) {
        
        if (!this.puedeComer(comedora)) {
            Tablero t=new Tablero();
            t.setFichas(this.clonarFichas());
            if (cantidad > this.cantidadPiezasComidas) {
                this.cantidadPiezasComidas = cantidad;
                this.estadoPiezasComidas.clear();
                t.VectorDeComidas = (Vector) fichasComidas.clone();
                
                t.fichaMovida=(Ficha)comedora.clone();
                this.estadoPiezasComidas.add(t);
            }
            else if(cantidad !=0 && cantidad == this.cantidadPiezasComidas && !this.EsRepetidoComido(t))
            {
                t.VectorDeComidas = (Vector) fichasComidas.clone();
                t.fichaMovida=(Ficha)comedora.clone();
                this.estadoPiezasComidas.add(t);
            }     
            
        } else {
            Posicion pos = comedora.getPos();
            int valor = 0;
            if (this.Turno) {
                valor = 1;
            } else {
                valor = -1;            //Ver si se puede comer a la derecha.
            }
            Posicion posAux = new Posicion(pos.fila + (valor), pos.columna + (valor));
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
                                    // Borramos ficha comida y movemos la ficha movedora.
                                    Ficha comida = this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)];
                                    this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)] = null;

                                    this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = null;
                                    comedora.mover(posAux.clone());
                                    this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;
                                    
                                    fichasComidas.add(comida);
                                    this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                    fichasComidas.remove(fichasComidas.size()-1);

                                    //Desasemos lo que hicimos.
                                    this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)] = comida;
                                    comedora.mover(pos);
                                    this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;
                                    this.Fichas[posAux.fila][posAux.columna] = null;
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
                                    // Borramos ficha comida y movemos la ficha movedora.
                                    Ficha comida = this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)];
                                    this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)] = null;

                                    this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = null;
                                    comedora.mover(posAux.clone());
                                    this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;

                                    fichasComidas.add(comida);
                                    this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                    fichasComidas.remove(fichasComidas.size()-1);

                                    //Desasemos lo que hicimos.
                                    this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)] = comida;
                                    comedora.mover(pos);
                                    this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;
                                    this.Fichas[posAux.fila][posAux.columna] = null;
                                }
                            }
                        }

                    }
                    valor = valor * (-1);
                    posAux.fila = pos.fila + (valor);
                    posAux.columna = pos.columna + (valor);
                }

            } else {

                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    if (this.Fichas[posAux.fila][posAux.columna] != null && comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca) {
                        //Como no esta vacia, puede ser que tenemos algo para comer. 
                        //Vemos si hay casillas vacias contiguas a la pieza a comer.
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                        if (posAux.esValida()) {
                            if (this.Fichas[posAux.fila][posAux.columna] == null) {
                                // Borramos ficha comida y movemos la ficha movedora.
                                Ficha comida = this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)];
                                this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)] = null;

                                this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = null;
                                comedora.mover(posAux.clone());
                                this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;

                                fichasComidas.add(comida);
                                this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                fichasComidas.remove(fichasComidas.size()-1);

                                //Desasemos lo que hicimos.
                                this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)] = comida;
                                comedora.mover(pos);
                                this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;
                                this.Fichas[posAux.fila][posAux.columna] = null;


                            }

                        }
                    }

                }
                //Ver si se puede comer a la izquierda.
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna - (valor);
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    if (this.Fichas[posAux.fila][posAux.columna] != null && comedora.esFichaBlanca != this.Fichas[posAux.fila][posAux.columna].esFichaBlanca) {
                        //Como no esta vacia, tenemos algo por comer. 
                        //Vemos si hay casillas vacias contiguas a la pieza a comer.
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna - (valor);
                        if (posAux.esValida()) {
                            if (this.Fichas[posAux.fila][posAux.columna] == null) {
                                // Borramos ficha comida y movemos la ficha movedora.
                                Ficha comida = this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)];
                                this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)] = null;

                                this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = null;
                                comedora.mover(posAux.clone());
                                this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;

                                fichasComidas.add(comida);
                                this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                fichasComidas.remove(fichasComidas.size()-1);

                                //Desasemos lo que hicimos.
                                this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)] = comida;
                                comedora.mover(pos);
                                this.Fichas[comedora.getPos().fila][comedora.getPos().columna] = comedora;
                                this.Fichas[posAux.fila][posAux.columna] = null;
                            }
                        }
                    }

                }

            }
        }

    }

    public Ficha[][] getFichas() {
        return Fichas;
    }

    public Ficha getFicha(int i, int j) {
        return Fichas[i][j];
    }

    public boolean esIgualA(Tablero t)
    {
        for (int i = 0; i < Fichas.length; i++) {
            for (int j = 0; j < Fichas.length; j++) {
                if((i+j)%2==0 )
                {
                    if(this.Fichas[i][j]!=null && t.Fichas[i][j]!=null
                         && !this.Fichas[i][j].esIgualA(t.Fichas[i][j]))
                         return false;
                    else if(this.Fichas[i][j]!=null && t.Fichas[i][j]==null ||
                            this.Fichas[i][j]==null && t.Fichas[i][j]!=null)
                        return false;
                }
                
                    
            }
            
        }
        return true;
    }
    public void setFichas(Ficha[][] val) {
        this.Fichas = val;
    }

    public Ficha[][] clonarFichas() {
        Ficha[][] clones = new Ficha[8][8];
        for (int i = 0; i < Fichas.length; i++) {
            for (int j = 0; j < Fichas.length; j++) {
                if (this.Fichas[i][j] != null) {
                    clones[i][j] = (Ficha) this.Fichas[i][j].clone();
                    clones[i][j].setPos(this.Fichas[i][j].getPos().clone());
                }
            }

        }
       
        return clones;
    }

    public String toString() {
        String cadena = "";
        for (int i = 0; i < Fichas.length; i++) {
            for (int j = 0; j < Fichas.length; j++) {
                if ((i + j) % 2 == 0) {
                    if (Fichas[i][j] != null) {
                        if (Fichas[i][j].isDama()) {
                            if (Fichas[i][j].esFichaBlanca) {
                                cadena = cadena + " 2 ";
                            } else {
                                cadena = cadena + "-2 ";
                            }
                        } else {
                            if (Fichas[i][j].esFichaBlanca) {
                                cadena = cadena + " 1 ";
                            } else {
                                cadena = cadena + "-1 ";
                            }
                        }
                    } else {
                        cadena = cadena + " 0 ";
                    }
                } else {
                    cadena = cadena + " B ";
                }
            }
            cadena = cadena + "\n";
        }
        return cadena;
    }

    /**
     * 
     * @param f1 ficha a ser borrada.
     * @return True si la pieza fue borrada con exito, y false si la pieza no pudo ser borrada.
     */
    public boolean BorrarFicha(Ficha f1) {
        if (this.Fichas[f1.getPos().fila][f1.getPos().columna] != null) {
            this.Fichas[f1.getPos().fila][f1.getPos().columna] = null;
            return true;
        }
        return false;
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

    public Vector MoverFicha(Ficha movedora) {
        Vector sucesores = new Vector();
        Posicion pos = movedora.getPos();
        int valor = 0;
        if (this.Turno) {
            valor = 1;
        } else {
            valor = -1;            //Ver si se puede comer a la derecha.
        }
        Posicion posAux = new Posicion(pos.fila + (valor), pos.columna + (valor));
        if (movedora.isDama()) {
            for (int i = 0; i < 2; i++) {
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    while (posAux.esValida() && this.Fichas[posAux.fila][posAux.columna] == null) {
                        if (this.Fichas[posAux.fila][posAux.columna] == null) {
                            // Borramos ficha comida y movemos la ficha movedora.                               
                            this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)] = null;
                            movedora.mover(posAux.clone());
                            this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                            //Guardar estado.   
                            Tablero table = new Tablero();
                            table.setFichas(this.clonarFichas());
                            table.fichaMovida=movedora;
                            sucesores.add(table);

                        }
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                    }

                }
                movedora.mover(pos);
                this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                this.Fichas[posAux.fila-(valor)][posAux.columna-(valor)] = null;
                //Ver si se puede comer a la izquierda.
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna - (valor);
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    while (posAux.esValida() && this.Fichas[posAux.fila][posAux.columna] == null) {
                        if (this.Fichas[posAux.fila][posAux.columna] == null) {
                            // Borramos ficha comida y movemos la ficha movedora.                               
                            this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)] = null;
                            movedora.mover(posAux.clone());
                            this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                            //Guardar estado.   
                            Tablero table = new Tablero();
                            table.fichaMovida=movedora;
                            table.setFichas(this.clonarFichas());
                            sucesores.add(table);
                            table=null;
                        }
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna - (valor);
                    }
                }
                movedora.mover(pos);
                this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                this.Fichas[posAux.fila-(valor)][posAux.columna+(valor)] = null;
                valor = valor * (-1);
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna + (valor);
            }

        } else {

            if (posAux.esValida()) {
                //Vemos si la casilla esta vacia.                    
                if (this.Fichas[posAux.fila][posAux.columna] == null) {
                    // Borramos ficha comida y movemos la ficha movedora.                               
                    this.Fichas[posAux.fila - (valor)][posAux.columna - (valor)] = null;
                    movedora.mover(posAux.clone());
                    this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                    //Guardar estado.   
                    Tablero table = new Tablero();
                    table.setFichas(this.clonarFichas());
                    table.fichaMovida=movedora;
                    sucesores.add(table);

                    movedora.mover(pos);
                    this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                    this.Fichas[posAux.fila][posAux.columna] = null;
                }
            }
            //Ver si se puede mover a la izquierda.
            posAux.fila = pos.fila + (valor);
            posAux.columna = pos.columna - (valor);
            if (posAux.esValida()) {
                //Vemos si la casilla esta vacia.
                if (this.Fichas[posAux.fila][posAux.columna] == null) {
                    // Borramos ficha comida y movemos la ficha movedora.                               
                    this.Fichas[posAux.fila - (valor)][posAux.columna + (valor)] = null;
                    movedora.mover(posAux.clone());
                    this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                    //Guardar estado.   
                    Tablero table = new Tablero();
                    table.setFichas(this.clonarFichas());
                    table.fichaMovida=movedora;
                    sucesores.add(table);

                    movedora.mover(pos);
                    this.Fichas[movedora.getPos().fila][movedora.getPos().columna] = movedora;
                    this.Fichas[posAux.fila][posAux.columna] = null;
                }
            }

        }
        return sucesores;
    }
    public Vector getSucesores(double valor)
    {
        Vector buscados= new Vector();
        for (int i = 0; i < this.Sucesores.size(); i++) {
            Tablero t1 = (Tablero) this.Sucesores.get(i);
            if(t1.puntaje==valor)
                buscados.add(t1);
        }
        return buscados;
    }

    
     public void coronarPeon() {

        for (int j = 0; j < 8; j++) {
            if ((j + 0) % 2 == 0) {
                if (Fichas[0][j] != null && !Fichas[0][j].esFichaBlanca) {
                    Fichas[0][j].setDama(true);
                }
            }
            if ((j + 7) % 2 == 0) {
                if (Fichas[7][j] != null && Fichas[7][j].esFichaBlanca) {
                    Fichas[7][j].setDama(true);
                }
            }
        }
    }
    
    public int tieneFichasEnTablero() {
        int con = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j + i) % 2 == 0) {
                    if (Fichas[i][j] != null && Fichas[i][j].esFichaBlanca == this.Turno) {
                        con++;
                    }
                    if (Fichas[i][j] != null && Fichas[i][j].esFichaBlanca == this.Turno) {
                        con++;
                    }
                }
            }
        }
        return con;

    }
}
