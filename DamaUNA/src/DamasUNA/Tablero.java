package DamasUNA;

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
    
    private Vector Sucesores= new Vector();
    /**
     * Indica la cantidad de piezas que se han comido en este estado,
     * esta variable es distinto de cero, solo si se ejercuto el metodo 
     * this.ComerMayorCant(comedora, cantidad, VectorDeFichasComidas);
     */
    public static int cantidadPiezasComidas;
    /**
     * Es un vector de objetos Tablero, que indica el estado donde se comieron fichas.
     * Este vector se llena luego  de ejecutar la funcion ComerMayorCant.
     */
    public static Vector estadoPiezasComidas = new Vector();
    
    /**
     * Es un vector de Ficha, que contiene todas las fichas que se comieron en este
     * estado.
     */
    public Vector VectorDeFichasComidas= new Vector();
    
    /**
     * Es una matiz de enteros que indica el estado del tablero.
     * Las alternativas posibles son 1,2,0,-1,-2. Los 1s son peones,
     * los 2 son damas, y el cero es un espacio vacio.
     */
    public int[][] Tabla;
    
    // Es la ficha que se movio.
    public Ficha fichaMovida;
    
    //Indica la el puntaje que tiene este estado.
    public double puntaje=0;
    
    /**
     * Estos valores se utilizan para la estrategia de poda Alfa-Beta.
     */
    public double alfa= Double.NEGATIVE_INFINITY;
    public double beta= Double.POSITIVE_INFINITY;
    
    /**
     * Constructores, la matriz es una matriz de enteros que indica el estado del tablero.
     * Las alternativas posibles son 1,2,0,-1,-2. Los 1s son peones,
     * los 2 son damas, y el cero es un espacio vacio.
     * @param matTabla
     */
    public Tablero(int[][] matTabla) {
        
        this.Tabla=matTabla;
        this.cantidadPiezasComidas=0;
        
    }

    private Tablero() {
        this.cantidadPiezasComidas=0;
        
    }

    /**
     * @return Retorna el nivel en el arbol MaxMin que se encuentra en este estado.
     */
    public int getNivel() {
        return Nivel;
    }

    public void setNivel(int val) {
        this.Nivel = val;
    }

    /**
     * 
     * @return True si es el turno de las blancas.
     */
    public boolean getTurnoBlancas() {
        return this.Turno;
    }

    /**
     * @return una Matriz de enteros que representa un estado.
     */
    public int[][] getTablero() {      
        return this.Tabla;
    }

    public void setTurnoBlancas(boolean val) {
        this.Turno = val;
    }
    public void CambiarTurno()
    {
        if(this.Turno)
            this.Turno=false;
        else
            this.Turno=true;
    }
   

    /**
     * Genera todos los posibles estados sucesores que puede tener un 
     * determinado estado, columna lo guarda en el vector Sucesores. Retorna false, 
     * si es que no se pueden generar nuevos sucesores.
     *
     */
    public void GenerarSucesores() 
    {
        int newLevel=this.Nivel+1;
        boolean esUnaComedora=false;
        for (int i = 0; i < this.Tabla.length; i++) {
            for (int j = 0; j < this.Tabla.length; j++) {
                if((i+j)%2==0 && this.Tabla[i][j]!=0)
                {
                    Ficha f1=new Ficha(new Posicion(i,j));
                    f1.setColorFicha(this.Tabla[i][j]);
                    Vector estadosMovidas;
                    
                    //Si la ficha es del turno creamos los sucesores.
                    if (f1.esFichaBlanca == this.Turno) 
                    {
                       
                             // Sucesores generados por comer piezas
                            this.cantidadPiezasComidas = 0;
                            this.estadoPiezasComidas.clear();//Limpio el vector estatico.
                            f1.actualizarPosVieja();
                            if (this.puedeComer(f1)) {
                                esUnaComedora = true;
                                this.Sucesores.clear();
                                this.ComerMayorCant(f1, 0, new Vector());
                                //Si hay algo de piezas comidas, lo agrego como sucesores.
                                for (int k = 0; k < this.estadoPiezasComidas.size(); k++) {
                                    Tablero t1 = (Tablero) this.estadoPiezasComidas.get(k);
                                    t1.Nivel = newLevel;// Le digo al estado en que nivel esta.
                                    this.Sucesores.add(t1);
                                }
                            } else if (!esUnaComedora) {
                                //Sucesores generados
                                estadosMovidas = this.MoverFicha(f1);


                                // Agregamos los estados generados en el vector sucesores.
                                for (int k = 0; k < estadosMovidas.size(); k++) {
                                    Tablero t1 = (Tablero) estadosMovidas.get(k);
                                    t1.Nivel = newLevel;// Le digo al estado en que nivel esta.
                                    this.Sucesores.add(t1);

                                }

                            }
                        
                        
                      
                    }
                    f1=null;
                        
                }
                
            }
            
        }
        
    }
    
    /**
     * 
     * @return Un objeto vector que contiene a todos los sucesores posibles del estado actual.
     */
    public Vector getSucesores() {
        return Sucesores;
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
       
        for (int i = 0; i < this.Tabla.length; i++) {
            for (int j = 0; j < this.Tabla.length; j++) {

                if (((i + j) % 2) == 0 && this.Tabla[i][j]!=0) {
                    

                    //Todas las fichas del que le toca jugar 
                    if ((this.Tabla[i][j]>0) == this.Turno) {
                        int valorPiesa;
                        Posicion pos1 = new Posicion(i,j);

                        if (Math.abs(this.Tabla[i][j])==2) {
                            valorPiesa = 3;
                        } else {
                            valorPiesa = 1;
                        }
                         sumaPonderada = sumaPonderada + valorPiesa * pos1.PesoPos();
                       
                    }
                }

            }

        }

        this.puntaje=this.cantidadFichasDelTurno()*sumaPonderada + sumaPonderada;
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
     *Retorna true si la ficha "comedora" puede comer alguna ficha. 
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
                    while (posAux.esValida() && this.Tabla[posAux.fila][posAux.columna] == 0) {
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                    }

                    if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0)) {
                        //Como no esta vacia, puede ser que tenemos algo para comer. 
                        //Vemos si hay casillas vacias contiguas a la pieza a comer.
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                        if (posAux.esValida()) {
                            if (this.Tabla[posAux.fila][posAux.columna] == 0) {
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
                    while (posAux.esValida() && this.Tabla[posAux.fila][posAux.columna] == 0) {
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna - (valor);
                    }

                    if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0)) {
                        //Como no esta vacia, tenemos algo por comer. 
                        //Vemos si hay casillas vacias contiguas a la pieza a comer.
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna - (valor);
                        if (posAux.esValida()) {
                            if (this.Tabla[posAux.fila][posAux.columna] == 0) {
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
                if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0)) {
                    //Como no esta vacia, puede ser que tenemos algo para comer. 
                    //Vemos si hay casillas vacias contiguas a la pieza a comer.
                    posAux.fila = posAux.fila + (valor);
                    posAux.columna = posAux.columna + (valor);
                    if (posAux.esValida()) {
                        if (this.Tabla[posAux.fila][posAux.columna] == 0) {
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
                if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0)) {
                    //Como no esta vacia, tenemos algo por comer. 
                    //Vemos si hay casillas vacias contiguas a la pieza a comer.
                    posAux.fila = posAux.fila + (valor);
                    posAux.columna = posAux.columna - (valor);
                    if (posAux.esValida()) {
                        if (this.Tabla[posAux.fila][posAux.columna] == 0) {
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
            t.Tabla=this.clonarTabla(this.Tabla);
            if (cantidad > this.cantidadPiezasComidas) {
                this.cantidadPiezasComidas = cantidad;
                
                this.estadoPiezasComidas.clear();
                t.cantidadPiezasComidas = cantidad;
                t.VectorDeFichasComidas = (Vector) fichasComidas.clone();
                
                t.fichaMovida=comedora.clonar();
                this.estadoPiezasComidas.add(t);
            }
            else if(cantidad !=0 && cantidad == this.cantidadPiezasComidas && !this.EsRepetidoComido(t))
            {
                t.VectorDeFichasComidas = (Vector) fichasComidas.clone();
                t.fichaMovida=comedora.clonar();
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
                        while (posAux.esValida() && this.Tabla[posAux.fila][posAux.columna] == 0) {
                            posAux.fila = posAux.fila + (valor);
                            posAux.columna = posAux.columna + (valor);
                        }

                        if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0)) {
                            //Como no esta vacia, puede ser que tenemos algo para comer. 
                            //Vemos si hay casillas vacias contiguas a la pieza a comer.
                            posAux.fila = posAux.fila + (valor);
                            posAux.columna = posAux.columna + (valor);
                            if (posAux.esValida()) {
                                if (this.Tabla[posAux.fila][posAux.columna] == 0) {
                                    //**** Borramos ficha comida y movemos la ficha movedora.*****
                                    
                                    //Guardamos la ficha comida.
                                    int intFicha = this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)];
                                    Ficha comida= new Ficha(new Posicion(posAux.fila - (valor),posAux.columna - (valor)));
                                    comida.setColorFicha(intFicha);
                                    
                                    this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)] = 0;

                                    this.Tabla[pos.fila][pos.columna] = 0;
                                    this.Tabla[posAux.fila][posAux.columna]=comedora.getColorFicha();
                                    comedora.mover(posAux.clone());
                                    
                                    this.Tabla[posAux.fila][posAux.columna] = comedora.getColorFicha();
                                    
                                    fichasComidas.add(comida);
                                    this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                    fichasComidas.remove(fichasComidas.size()-1);

                                    //Desasemos lo que hicimos.
                                    this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)] = comida.getColorFicha();
                                    comedora.mover(pos);
                                    this.Tabla[pos.fila][pos.columna] = comedora.getColorFicha();
                                    this.Tabla[posAux.fila][posAux.columna] = 0;
                                }
                            }
                        }

                    }
                    //Ver si se puede comer a la izquierda.
                    posAux.fila = pos.fila + (valor);
                    posAux.columna = pos.columna - (valor);
                    if (posAux.esValida()) {
                        
                        while (posAux.esValida() && this.Tabla[posAux.fila][posAux.columna] == 0) {
                            posAux.fila = posAux.fila + (valor);
                            posAux.columna = posAux.columna - (valor);
                        }

                        if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0)) {
                            //Como no esta vacia, puede ser que tenemos algo para comer. 
                            //Vemos si hay casillas vacias contiguas a la pieza a comer.
                            posAux.fila = posAux.fila + (valor);
                            posAux.columna = posAux.columna - (valor);
                            if (posAux.esValida()) 
                            {
                                if (this.Tabla[posAux.fila][posAux.columna] == 0) {
                                    //**** Borramos ficha comida y movemos la ficha movedora.*****
                                    
                                    //Guardamos la ficha comida.
                                    int intFicha = this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)];
                                    Ficha comida= new Ficha(new Posicion(posAux.fila - (valor),posAux.columna + (valor)));
                                    comida.setColorFicha(intFicha);
                                    
                                    this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)] = 0;

                                    this.Tabla[pos.fila][pos.columna] = 0;
                                    this.Tabla[posAux.fila][posAux.columna]=comedora.getColorFicha();
                                    comedora.mover(posAux.clone());
                                    
                                    this.Tabla[posAux.fila][posAux.columna] = comedora.getColorFicha();
                                    
                                    fichasComidas.add(comida);
                                    this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                    fichasComidas.remove(fichasComidas.size()-1);

                                    //Desasemos lo que hicimos.
                                    this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)] = comida.getColorFicha();
                                    comedora.mover(pos);
                                    this.Tabla[pos.fila][pos.columna] = comedora.getColorFicha();
                                    this.Tabla[posAux.fila][posAux.columna] = 0;
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
                     if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0))
                    {
                        //Como no esta vacia, puede ser que tenemos algo para comer. 
                        //Vemos si hay casillas vacias contiguas a la pieza a comer.
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                        if (posAux.esValida()) {
                            if (this.Tabla[posAux.fila][posAux.columna] == 0) {
                                    //**** Borramos ficha comida y movemos la ficha movedora.*****
                                    
                                    //Guardamos la ficha comida.
                                    int intFicha = this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)];
                                    Ficha comida= new Ficha(new Posicion(posAux.fila - (valor),posAux.columna - (valor)));
                                    comida.setColorFicha(intFicha);
                                    
                                    this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)] = 0;

                                    this.Tabla[pos.fila][pos.columna] = 0;
                                    this.Tabla[posAux.fila][posAux.columna]=comedora.getColorFicha();
                                    comedora.mover(posAux.clone());
                                    
                                    this.Tabla[posAux.fila][posAux.columna] = comedora.getColorFicha();
                                    
                                    fichasComidas.add(comida);
                                    this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                    fichasComidas.remove(fichasComidas.size()-1);

                                    //Desasemos lo que hicimos.
                                    this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)] = comida.getColorFicha();
                                    comedora.mover(pos);
                                    this.Tabla[pos.fila][pos.columna] = comedora.getColorFicha();
                                    this.Tabla[posAux.fila][posAux.columna] = 0;
                                }
                        
                        

                        }
                    }

                }
                //Ver si se puede comer a la izquierda.
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna - (valor);
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                   if (posAux.esValida() && (this.Tabla[posAux.fila][posAux.columna]!=0) 
                           && (comedora.getColorFicha()>0) == (this.Tabla[posAux.fila][posAux.columna]<0)){
                            //Como no esta vacia, puede ser que tenemos algo para comer. 
                            //Vemos si hay casillas vacias contiguas a la pieza a comer.
                            posAux.fila = posAux.fila + (valor);
                            posAux.columna = posAux.columna - (valor);
                            if (posAux.esValida()) {
                                if (this.Tabla[posAux.fila][posAux.columna] == 0) {
                                    //**** Borramos ficha comida y movemos la ficha movedora.*****
                                    
                                    //Guardamos la ficha comida.
                                    int intFicha = this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)];
                                    Ficha comida= new Ficha(new Posicion(posAux.fila - (valor),posAux.columna + (valor)));
                                    comida.setColorFicha(intFicha);
                                    
                                    this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)] = 0;

                                    this.Tabla[pos.fila][pos.columna] = 0;
                                    this.Tabla[posAux.fila][posAux.columna]=comedora.getColorFicha();
                                    comedora.mover(posAux.clone());
                                    
                                    this.Tabla[posAux.fila][posAux.columna] = comedora.getColorFicha();
                                    
                                    fichasComidas.add(comida);
                                    this.ComerMayorCant(comedora, cantidad + 1, fichasComidas);
                                    fichasComidas.remove(fichasComidas.size()-1);

                                    //Desasemos lo que hicimos.
                                    this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)] = comida.getColorFicha();
                                    comedora.mover(pos);
                                    this.Tabla[pos.fila][pos.columna] = comedora.getColorFicha();
                                    this.Tabla[posAux.fila][posAux.columna] = 0;
                                }
                            }
                        }

                }

            }
        }

    }

    /**
     * 
     * @param t
     * @return True si el estado "t" es igual al estado actual.
     */

    public boolean esIgualA(Tablero t)
    {
        for (int i = 0; i < this.Tabla.length; i++) {
            for (int j = 0; j < this.Tabla.length; j++) {
                if((i+j)%2==0 )
                {
                    if(this.Tabla[i][j]!=0 && t.Tabla[i][j]!=0
                         && !(this.Tabla[i][j]==t.Tabla[i][j]))
                         return false;
                    else if(this.Tabla[i][j]!=0 && t.Tabla[i][j]==0 ||
                            this.Tabla[i][j]==0 && t.Tabla[i][j]!=0)
                        return false;
                }
                
                    
            }
            
        }
        return true;
    }
    
    /**
     * 
     * @return Un cadena para imprimir en pantalla. 
     */
    public String toString() {
        String cadena = "";
        for (int i = 0; i < this.Tabla.length; i++) {
            for (int j = 0; j < this.Tabla.length; j++) {
                if ((i + j) % 2 == 0) {
                    if (this.Tabla[i][j] != 0) {
                        if (Math.abs(this.Tabla[i][j])==2) {
                            if(this.Tabla[i][j]<0)
                                cadena = cadena + this.Tabla[i][j]+" ";
                            else
                                cadena = cadena +" "+ this.Tabla[i][j]+" ";
                        } else {
                            if(this.Tabla[i][j]<0)
                                cadena = cadena + this.Tabla[i][j]+" ";
                            else
                                cadena = cadena +" "+ this.Tabla[i][j]+" ";
                            
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
    * Para clonar el estado actual.
    * @return
    */
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
    /**
     * Retorna un vector de Tablero que son todos los estados posibles donde la ficha movedora
     * se puede mover.
     * @param movedora
     * @return
     */
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
        if (movedora.isDama()) 
        {
            for (int i = 0; i < 2; i++) {
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    while (posAux.esValida() && this.Tabla[posAux.fila][posAux.columna] == 0) {
                        if (this.Tabla[posAux.fila][posAux.columna] == 0) 
                        {
                            // Borramos ficha comida y movemos la ficha movedora.                               
                            this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)] = 0;
                            movedora.mover(posAux.clone());
                            this.Tabla[posAux.fila][posAux.columna] = movedora.getColorFicha();
                            //Guardar estado.   
                            Tablero table = new Tablero();
                            table.Tabla=this.clonarTabla(this.Tabla);
                            table.fichaMovida=movedora.clonar();
                            sucesores.add(table);
                            
                           
                        }
                        
                        
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna + (valor);
                      
                    }
                     movedora.mover(pos);
                     this.Tabla[pos.fila][pos.columna] = movedora.getColorFicha();
                     this.Tabla[posAux.fila-(valor)][posAux.columna-(valor)] = 0;
                     
                }
               
               
                //Ver si se puede comer a la izquierda.
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna - (valor);
                if (posAux.esValida()) {
                    //Vemos si la casilla esta vacia.
                    while (posAux.esValida() && this.Tabla[posAux.fila][posAux.columna] == 0) {
                        if (this.Tabla[posAux.fila][posAux.columna] == 0) {
                            // Borramos ficha comida y movemos la ficha movedora.                               
                            this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)] = 0;
                            movedora.mover(posAux.clone());
                            this.Tabla[posAux.fila][posAux.columna] = movedora.getColorFicha();
                            //Guardar estado.   
                            Tablero table = new Tablero();
                            table.fichaMovida=movedora.clonar();
                            table.Tabla=this.clonarTabla(this.Tabla);
                            sucesores.add(table);
                            
                        }
                        posAux.fila = posAux.fila + (valor);
                        posAux.columna = posAux.columna - (valor);
                    }
                    movedora.mover(pos);
                    this.Tabla[pos.fila][pos.columna] = movedora.getColorFicha();
                    this.Tabla[posAux.fila-(valor)][posAux.columna+(valor)] = 0;
                }
                
                valor = valor * (-1);
                posAux.fila = pos.fila + (valor);
                posAux.columna = pos.columna + (valor);
            }

        } else 
        {

            if (posAux.esValida()) 
            {
                //Vemos si la casilla esta vacia.                    
                if (this.Tabla[posAux.fila][posAux.columna] == 0) 
                {
                    // Borramos ficha comida y movemos la ficha movedora.                               
                    this.Tabla[posAux.fila - (valor)][posAux.columna - (valor)] = 0;
                    movedora.mover(posAux.clone());
                    this.Tabla[posAux.fila][posAux.columna] = movedora.getColorFicha();
                    
                    //Preparo el estado para luego guardarlo.   
                    Tablero table = new Tablero();
                    table.Tabla=this.clonarTabla(this.Tabla);
                    table.fichaMovida=movedora.clonar();
                    sucesores.add(table);
                    
                    //Le retorno a la movedora en su posicion inicial.
                    movedora.mover(pos);
                    this.Tabla[pos.fila][pos.columna] = movedora.getColorFicha();
                    this.Tabla[posAux.fila][posAux.columna] = 0;
                }
            }
            //Ver si se puede mover a la izquierda.
            posAux.fila = pos.fila + (valor);
            posAux.columna = pos.columna - (valor);
            if (posAux.esValida()) {
                //Vemos si la casilla esta vacia.
                if (this.Tabla[posAux.fila][posAux.columna] == 0) {
                    // Borramos ficha comida y movemos la ficha movedora.                               
                    this.Tabla[posAux.fila - (valor)][posAux.columna + (valor)] = 0;
                    movedora.mover(posAux.clone());
                    this.Tabla[posAux.fila][posAux.columna] = movedora.getColorFicha();
                    //Guardar estado.   
                    Tablero table = new Tablero();
                    table.Tabla=this.clonarTabla(this.Tabla);
                    table.fichaMovida=movedora.clonar();
                    sucesores.add(table);

                    movedora.mover(pos);
                    this.Tabla[pos.fila][pos.columna] = movedora.getColorFicha();
                    this.Tabla[posAux.fila][posAux.columna] = 0;
                }
            }

        }
        return sucesores;
    }
    /**
     * 
     * @param valor
     * @return Un vector de Tablero que indica todos los sucesores que tiene puntaje igual a valor.
     */
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

    /**
     * Verifica si algun peon en este estado puede ser coronado como reina.
     */
     public void coronarPeon() {

        for (int j = 0; j < 8; j++) {
            if ((j + 0) % 2 == 0) {
                if (this.Tabla[0][j] != 0 && this.Tabla[0][j]< 0) {
                    Tabla[0][j]=-2;
                }
            }
            if ((j + 7) % 2 == 0) {
                if (this.Tabla[7][j] != 0 && this.Tabla[7][j]> 0) {
                    Tabla[7][j]=2;
                }
            }
        }
    }
     
    /**
     * Verifica la cantidad de fichas blancas o negras que existen en tablero.
     * @return la cantidad de fichas que tiene al que juega en este estado.
     */
    public int cantidadFichasDelTurno() {
        int con = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j + i) % 2 == 0) {
                    if (this.Tabla[i][j] != 0 && (this.Tabla[i][j]>0) == this.Turno) {
                        con++;
                    }
                }
            }
        }
        return con;

    }
    /**
     * Retorna una tabla de enteros identica a la tabla que representa este estado.
     * @param t1
     * @return
     */
    public int[][]clonarTabla(int[][]t1){
        int[][] clon= new int[8][8];
        for (int i = 0; i < t1.length; i++) {
            for (int j = 0; j < t1.length; j++) {
                clon[i][j]=t1[i][j];
                
            }
            
        }
        return clon;
    }

   public int cantidadTotalPiezas()
   {
       int con = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j + i) % 2 == 0) {
                    if (this.Tabla[i][j] != 0) {
                        con++;
                    }
                }
            }
        }
        return con;
   }
            
}
