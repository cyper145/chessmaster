/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ASP_NR;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Esta clase representa a un Analizador Sintactico Predictivo y contiene
 * todos los metodos necesarios
 * que un ASP necesitaria para su proceso de analisis.
 * @author Usuario
 */
public class ASP {

    /**
     * Vector de String que contiene todos los terminales.
     */
    public Vector terminales;

    /**
     * Vector que contiene objetos No_terminales
     */
    public Hashtable no_terminales;

    /**
     * Variable que guardara el Simbolo Inicial
     */
    public String simboloInicial = "";

    /**
     * Variable que guardara la derivacion correspondiente de la entrada
     * si es que pertenece al lenguaje.
     */
    public String derivacion = "";//
    
    /**
     * Nos indicara si la Gramatica es ambigua o no.
     */
    public boolean esAmbiguo=false;

    /**
     * Nos indica si es que se pudo obtener una derivacion,
     * es decir si la Cadena de entrada pertenece al Lenguaje.
     */
    public boolean solucion=true;

    /**
     * Constructor de la clase que inicializa las variables necesarias.
     */
    public ASP() {
        this.terminales = new Vector();
        this.no_terminales = new Hashtable();
    }

    /**
     * Este metodo puebla la Tabla Hash de los No-Terminales. Esta Tabla en si representa a la Gramatica de
     * entrada. Tambien se setea el vector de Terminales, que se utiliza para la construccion de la Tabla
     * del ASP.
     * @param producciones Este vector contiene la gramatica ingresada.
     */
    public void setTablaHash(Vector producciones) {
        Iterator iterProduc = producciones.iterator();
        boolean primeraVez = true;
        //Creamos los objetos NoTerminal.
        while (iterProduc.hasNext()) {
            String p = (String) iterProduc.next();
            StringTokenizer token = new StringTokenizer(p, ":");
            //La clave de la Tabla Hash viene a ser el Lado Izquierdo, es decir, el simbolo del No-Terminal.
            String ladoIzquierdo = token.nextToken().trim();
            String ladoDerecho = token.nextToken().trim();

            //La primera linea de la Gramatica contiene al Simbolo Inicial por defecto.
            if (primeraVez) {
                this.simboloInicial = ladoIzquierdo;
                primeraVez = false;
            }

            NoTerminal nT = new NoTerminal(ladoIzquierdo);
            nT.setProducciones(ladoDerecho);
            //Si es que el No-Terminal ya no existe se lo crea y se inserta en la Tabla Hash, en otro caso,
            //se agrega una produccion mas a su vector de producciones.
            if (this.no_terminales.containsKey(ladoIzquierdo)) {
                NoTerminal aux = (NoTerminal) this.no_terminales.get(ladoIzquierdo);
                aux.setProducciones(ladoDerecho);
            } else {
                this.no_terminales.put(ladoIzquierdo, nT);
            }


        }
        
        //Llenamos el vector de Terminales.
        iterProduc = producciones.iterator();
        while (iterProduc.hasNext()) {
            String p = (String) iterProduc.next();
            StringTokenizer token = new StringTokenizer(p, ":");

            token.nextToken();// Sacamos el lado izquierdo
            String ladoDerecho = token.nextToken().trim(); //Sacamos el lado derecho.
            StringTokenizer tokDer = new StringTokenizer(ladoDerecho, " ");//Sacamos los elementos del lado derecho.
            while (tokDer.hasMoreTokens()) {
                String clave = tokDer.nextToken();
                //Si la clave no es un No-Terminal y no es vacio entra en el vector de Terminales.
                //Ya que terminales se utiliza para el creado de la Tabla ASP, el Vacio no forma parte del mismo.
                if (!this.no_terminales.containsKey(clave) && !clave.equals("@")) {
                    this.agregar(this.terminales, clave);
                }
            }
        }
        //Agregamos $ al final de la lista de Terminales.
        this.agregar(this.terminales, "$");
        //Eliminamos el vacio de los terminales.
        this.tieneVacio(this.terminales);
    }

    /**
     * Este metodo calcula los Conjuntos Primero de todos los No-Terminales. Se recorre la lista de
     * No-Terminales y utilizando el metodo recursivo private void primero(Vector terminales, String X)
     * se halla su conjunto primero.
     */
    public void CalcularPrimeros() {
        //Extraemos el vector de claves que contiene la tabla Hash.
        Enumeration enuM = this.no_terminales.keys();

        //Se itera sobre los No-Terminales.
        while (enuM.hasMoreElements()) {
            //Extraemos el No-Terminal correspondiente.
            Vector conjuntoPrimero = new Vector();
            String key = (String) enuM.nextElement();
            NoTerminal aux = (NoTerminal) this.no_terminales.get(key);
            //Llamamos al metodo correspondiente.
            this.primero(conjuntoPrimero, key);
            //Seteamos el Conjunto Primero del No-Terminal.
            aux.setPrimero(conjuntoPrimero);
        }        
    }

    /**
     * Este metodo calcula recursivamente el conjunto primero de un No-Terminal dado. Cada
     * conjunto se puede hallarse mas de una vez, ya que solo se halla el conjunto Primero del
     * No-Terminal que se desea, si guardar los conjuntos intermedios hallados.
     * @param terminales Contedra el conjunto Primero que se obtuvo para el No-Terminal.
     * @param X Simbolo del cual se desea calcular el Conjuno Primero.
     */
    private void primero(Vector terminales, String X) {
        //Si X es un No-Terminal entonces se itera sobre las producciones de X de manera
        //a obtener su conjunto primero. Si es un Terminal se lo agrega al conjunto Primero
        //del No-Terminal.
        if (this.no_terminales.containsKey(X)) {
            NoTerminal n = (NoTerminal) this.no_terminales.get(X);
            Iterator iter = n.getProducciones().iterator();
            //Iterar sobre todas las producciones que tiene un No-Terminal.
            while (iter.hasNext()) {
                // Los elementos de lado derecho estan separados por espacios, ese es el token.
                String prod = (String) iter.next();
                StringTokenizer token = new StringTokenizer(prod, " ");
                String elemento = token.nextToken();
                //Si es que existe una produccion del No-Terminal que es @ (Vacio), se lo agrega directamente.
                //En otro caso se halla el conjunto primero del primer Token de la Produccion que se trata.
                if (elemento.compareTo("@") == 0) {
                    this.agregar(terminales, "@");
                } else {
                    Vector aux = new Vector();
                    this.primero(aux, elemento);
                    //En esta parte se hace cumplir la tercera regla para hallar el conjunto Primero.
                    while (this.tieneVacio(aux) && token.hasMoreTokens()) {
                        elemento = token.nextToken();
                        this.primero(aux, elemento);
                        if (!token.hasMoreTokens()) {
                            break;
                        }
                    }
                    //Se agrega el conjunto primero hallado al conjunto primero del No-Terminal tratado.
                    this.agregarTodos(terminales, aux);
                }
            }
        } else {
            //Condicion de parada de la recursion.
            this.agregar(terminales, X);
        }
    }

    /**
     * Es utilizado para hallar el primero de una produccion.
     * Antes de utilizar esta funcion, primero se debe correr la funcion Primero().
     * Este metodo es utilizado en el calculo de Siguiente al querer hallar el primero de una
     * produccion de la forma Y1Y2....Yn.
     * @param terminales Contendra el Conjunto Primero hallado.
     * @param prod Es la produccion a la cual le queremos hallar su conjunto primero.
     */
    public void primeroProduccion(Vector terminales, String prod) {
        //Lo que el metodo realiza en realidad es aplicar la regla 3 del Conjunto Primero.
        StringTokenizer token = new StringTokenizer(prod, " ");
        while (token.hasMoreElements()) {
            String elemento = token.nextToken();
            if (this.no_terminales.containsKey(elemento)) {
                NoTerminal nT = (NoTerminal) this.no_terminales.get(elemento);
                this.agregarTodos(terminales, nT.getPrimero());
                if (!this.tieneVacio(terminales)) {
                    break;
                } else if (!token.hasMoreElements()) {
                    this.agregar(terminales, "@");
                }
            } else {
                this.agregar(terminales, elemento);
                break;
            }

        }
    }

    /**
     * Este metodo halla el conjunto Siguiente de todos los No-terminales.
     * Llama sucesivamente a la funcion private void Siguiente(String elementoReal, Vector conjuntoSiguientes, String elemento)
     * el cual halla recursivamente el conjunto Siguiente del No-Terminal.
     */
    public void CalcularSiguientes() {
        //De manera a seguir con las reglas del calculo de siguiente se agrega a la TablaFila del
        //Simbolo Inicial el delimitador por la derecha ($).
        NoTerminal aux = (NoTerminal) this.no_terminales.get(this.simboloInicial);
        aux.getSiguiente().add("$");
        //Extraemos el vector de claves que contiene la tabla Hash.
        Enumeration enuM = this.no_terminales.keys();
        //Se itera sobre los No-Terminales.
        while (enuM.hasMoreElements()) {
            //Extraemos el No-Terminal correspondiente.
            String key = (String) enuM.nextElement();
            aux = (NoTerminal) this.no_terminales.get(key);
            //Llamamos al metodo correspondiente.
            this.Siguiente(key, aux.getSiguiente(), key);
        }
    }
    /**
     * Este metodo Calcula recursivamente el Conjunto Siguiente del No-Terminal Pasado. No guarda los
     * Conjuntos siguientes posiblemente hallados durante el proceso. ElementoReal viene a ser el Simbolo del
     * No-Terminal del cual se quiere hallar el conjunto Siguiente. Elemento viene a ser el Simbolo del No-Terminal
     * del cual se halla el conjunto Siguiente en este momento. Se utilizan para evitar posibles ciclos largos o cortos
     * encontrado durante el proceso del calculo.
     * @param elementoReal Viene a ser el Simbolo del No-Terminal del cual se quiere hallar el conjunto Siguiente.
     * @param conjuntoSiguientes Almacenara el Conjunto Siguiente hallado.
     * @param elemento El Simbolo del No-Terminal del cual se halla el conjunto Siguiente en este momento.
     */
    private void Siguiente(String elementoReal, Vector conjuntoSiguientes, String elemento) {
        //Extraemos el vector de claves que contiene la tabla Hash.
        Enumeration enuM = this.no_terminales.keys();
        //Se itera sobre las producciones de los no terminales de manera a ubicar a elemento en el lado
        //derecho de alguna produccion.
        while (enuM.hasMoreElements()) {
            String key = (String) enuM.nextElement();
            NoTerminal aux = (NoTerminal) this.no_terminales.get(key);
            Iterator it = aux.getProducciones().iterator();
            //Se itera sobre las producciones.
            while (it.hasNext()) {
                String prod = (String) it.next();
                //En esta parte se hace cumplir las reglas 2 y 3 del calculo de Siguiente.
                if (prod.contains(elemento)) {
                    String alpha = "";
                    String betha = "";
                    int indice = prod.indexOf(elemento);
                    alpha = prod.substring(0, indice).trim();
                    betha = prod.substring(indice + elemento.length(), prod.length()).trim();
                    if (!betha.isEmpty()) {
                        StringTokenizer tokens = new StringTokenizer(betha, " ");
                        String tok = tokens.nextToken().trim();
                        if (this.no_terminales.containsKey(tok)) {
                            NoTerminal nt = (NoTerminal) this.no_terminales.get(tok);
                            agregarTodos(conjuntoSiguientes, nt.getPrimero());
                            if (this.tieneVacio(conjuntoSiguientes)) {
                                if (!key.equals(elemento) && !key.equals(elementoReal)) {
                                    this.Siguiente(elementoReal, aux.getSiguiente(), key);
                                    agregarTodos(conjuntoSiguientes, aux.getSiguiente());
                                }
                            }
                        } else {
                            this.agregar(conjuntoSiguientes, tok);
                        }
                    } else {
                        if (!key.equals(elemento) && !key.equals(elementoReal)) {
                            this.Siguiente(elementoReal, aux.getSiguiente(), key);
                            agregarTodos(conjuntoSiguientes, aux.getSiguiente());
                        }
                    }
                }
            }
        }
    }
    /**
     * Este metodo verifica si el vector recibido contiene el simbolo de vacio (@), si es que lo
     * contiene lo elimina y devuelve true, sino devuelve false.
     * @param v Vector de Terminales.
     * @return En caso de existir el vacio lo elimina y retorna true , false en otro caso.
     */
    private boolean tieneVacio(Vector v) {
        for (int i = 0; i < v.size(); i++) {
            String s = (String) v.get(i);
            if (s.compareTo("@") == 0) {
                v.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo realiza una impresion de la informacion que contiene los No-terminales,
     * como su Nombre y conjuntos Primeros y Siguientes.
     */
    public void imprimirNoTerminales() {
        Enumeration enu = this.no_terminales.elements();
        while (enu.hasMoreElements()) {
            NoTerminal nT = (NoTerminal) enu.nextElement();
            System.out.println(nT.toString() + "\n");
        }
    }
    /**
     * Este me todo agrega los elementos que se encuentran en el vector "elementos" en "vector".
     * Un elemento solo es agregado al "vector" si no se encuentra en el mismo. Se utiliza el metodo
     * "agregar" para agregar un elemento a la vez.
     * @param vector Al cual se le quiere los elementos
     * @param elementos Vector que contiene los elementos a agregar.
     */
    public void agregarTodos(Vector vector, Vector elementos) {
        for (int i = 0; i < elementos.size(); i++) {
            String cad = (String) elementos.get(i);
            this.agregar(vector, cad);
        }
    }

    /**
     * Metodo que agrega el elemento recibido a "vector". El vector es recorrido, y si
     * el elemento no esta presente en el mismo es agregado.
     * @param vector Vector al cual se le desea agregar el elemento recibido.
     * @param elemento Elemento a agregar al vector.
     */
    public void agregar(Vector vector, String elemento) {
        boolean existe = false;
        for (int i = 0; i < vector.size(); i++) {
            String cad = (String) vector.get(i);
            if (cad.equals(elemento)) {
                existe = true;
                break;
            }
        }
        //Solo si el elemento no se encuentra en el vector es agregado.
        if (!existe) {
            vector.add(elemento);
        }
    }

    /**
     * Este metodo se encarga de poblar las FilaTabla de cada No-Terminal una vez que se hallan
     * calculados los conjuntos Primeros y Siguientes.
     * @throws java.lang.Exception
     */
    public void hacerTablaASP() throws Exception {        
        //Extraemos el vector de claves que contiene la tabla Hash de No-Terminales.
        Enumeration iter = this.no_terminales.keys();
        //Iteramos sobre los No-Terminales.
        while (iter.hasMoreElements()) {
            String noTerminal = (String) iter.nextElement();
            //Extraemos cada No-Terminal para poblar su FilaTabla.
            NoTerminal nT = (NoTerminal) this.no_terminales.get(noTerminal);
            Iterator iter2 = nT.getProducciones().iterator();
            //Iteramos sobre las producciones que tiene el No-Terminal.
            while (iter2.hasNext()) {
                Vector primeros = new Vector();
                String prod = (String) iter2.next();
                //Hallamos el conjunto primero de la produccion.
                this.primeroProduccion(primeros, prod);

                Iterator iter3 = primeros.iterator();
                //Iteramos sobre los elementos del Conjunto Primero.
                while (iter3.hasNext()) {
                    String terminal = (String) iter3.next();
                    //Si el terminal no es corresponde al simbolo de vacio se lo agrega al Conjunto Siguiente.
                    //Si para el Terminal dado el No-Terminal ya tiene un elemento en su vector de Producciones
                    //de la FilaTabla, entonces indicamos de que la Gramatica es ambigua poniendo la variable
                    //esAmbiguo a true.
                    if (terminal.compareTo("@") != 0) {
                        if (nT.setFilaTabla(terminal, prod)) {
                            this.esAmbiguo=true;
                        }
                    }
                }
                //En esta parte se hace cumplir la segunda regla de creacion de la Tabla de ASP.
                if (this.tieneVacio(primeros)) {
                    iter3 = nT.getSiguiente().iterator();
                    while (iter3.hasNext()) {
                        String terminal = (String) iter3.next();
                        //En $ tambien ya se inserta, por que para nosotros cualquier
                        //cosa que no es un NoTerminal es insertado como no Terminal. Excepto el @.
                        if (nT.setFilaTabla(terminal, prod)) {
                            this.esAmbiguo=true;
                        }
                    }

                }

            }
        }
    }

    /**
     * Este metodo imprime la Tabla ASP. Se realiza esto imprimiendo cada elemento de
     * FilaTabla de cada No-Terminal.
     */
    public void imprimirTablaASP() {
        //Extraemos el vector de claves que contiene la tabla Hash de No-Terminales.
        Enumeration iter = this.no_terminales.keys();
        //Iteramos sobre los No-Terminales.
        while (iter.hasMoreElements()) {
            //Extraemos cada No-Terminal para poblar su FilaTabla.
            String noTerminal = (String) iter.nextElement();
            //Imprimimos el nombre del No-Terminal.
            System.out.println("No Terminal: " + noTerminal);
            NoTerminal nT = (NoTerminal) this.no_terminales.get(noTerminal);
            //Imprimimos la FilaTabla.
            System.out.println(nT.getFilaTabla().toString());
        }
    }

    /**
     * Este metodo implementa el codigo dado en clases del Analizador Sintactico Predictivo
     * No Recursivo. Este puede ser utilizado luego de que se ha llamado al metodo "hacerTablaASP()".
     * @param entrada Cadena de entrada a validar.
     * @return Una cadena con derivacion obtenida o una cadena vacia en caso de no pertenecer al lenguaje.
     * @throws java.lang.Exception
     */
    public String analizar(String entrada) throws Exception {
        StringTokenizer tokens = new StringTokenizer(entrada, " ");
        Stack pila = new Stack();
        //Primeramente agregamos el delimitador por la derecha ($) y el simbolo inicial
        //a la pila que se utiliza durante el Analisis.
        pila.push("$");
        pila.push(this.simboloInicial);
        //peek() devuelve el objeto que esta en el tope de la pila sin quitarlo.
        //Ponemos en X el objeto que esta en el tope de la pila, sin quitarlo de la pila.
        String X = (String) pila.peek();
        //Establecemos a Pre-analisis al primer componente lexico.
        String preanalisis = tokens.nextToken();
        String derivacion = "";
        //Se itera hasta que X sea igual al delimitador por la derecha ($).
        while (!X.equals("$")) {
            if (X.equals("$") || !this.no_terminales.containsKey(X)) {
                if (X.equals(preanalisis)) {
                    pila.pop();
                    preanalisis = tokens.nextToken();
                } else {
                    this.solucion = false;
                    break;
                }
            } else {
                NoTerminal nT = (NoTerminal) this.no_terminales.get(X);
                String prod =(String)((Vector)nT.getFilaTabla().get(preanalisis)).get(0);
                if (prod != null && !prod.isEmpty()) {
                    pila.pop();
                    if(!prod.trim().equals("@")){
                        String prodInvertido = this.invertirCadena(prod);
                        StringTokenizer auxTokens = new StringTokenizer(prodInvertido, " ");
                        while(auxTokens.hasMoreElements()){
                            pila.push(auxTokens.nextToken());
                        }
                    }
                    derivacion = derivacion +"\t"+nT.getNombre()+" -> "+prod+"\n";
                } else {
                    this.solucion = false;
                    break;
                }
            }
            X = (String)pila.peek();
        }
        this.derivacion = derivacion;
        return derivacion;
    }

    /**
     * Este metodo es invierte el orden de los simbolos de la produccion recibida como parametro. Este metodo se utiliza
     * en el proceso de analisis del ASP. En el momento de agregar una produccion Y1Y2...Yn a la pila, ya
     * que esto se agrega invirtiendo el orden, es decir YnY(n-1)...Y2Y1.
     * @param cadena La produccion a invertir.
     * @return La cadena invertida.
     */
    public String invertirCadena(String cadena) {
        String cadenaInvertida = "";
        StringTokenizer tokens = new StringTokenizer(cadena, " ");
        while(tokens.hasMoreElements()) {
            cadenaInvertida = tokens.nextToken() +" " + cadenaInvertida;
        }
        return cadenaInvertida.trim();
    }
}
