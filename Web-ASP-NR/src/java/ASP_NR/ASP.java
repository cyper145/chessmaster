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
 *
 * @author Arturo
 */
public class ASP {

    public Vector terminales;// Vector de String que contiene todos los terminales.
    public Hashtable no_terminales;// Vector que contiene objetos No_terminales
    public String simboloInicial = "";
    public String derivacion = "";

    public ASP() {
        this.terminales = new Vector();
        this.no_terminales = new Hashtable();
    }

    public void setTablaHash(Vector producciones) {
        Iterator iterProduc = producciones.iterator();
        boolean primeraVez = true;
        //Creamos los objetos NoTerminal.
        while (iterProduc.hasNext()) {
            String p = (String) iterProduc.next();
            StringTokenizer token = new StringTokenizer(p, ":");

            String ladoIzquierdo = token.nextToken().trim();
            String ladoDerecho = token.nextToken().trim();

            if (primeraVez) {
                this.simboloInicial = ladoIzquierdo;
                primeraVez = false;
            }


            NoTerminal nT = new NoTerminal(ladoIzquierdo);
            nT.setProducciones(ladoDerecho);

            if (this.no_terminales.containsKey(ladoIzquierdo)) {
                NoTerminal aux = (NoTerminal) this.no_terminales.get(ladoIzquierdo);

                aux.setProducciones(ladoDerecho);
            } else {
                this.no_terminales.put(ladoIzquierdo, nT);
            }


        }

        //Llenamos los vectores de ladosDerecho de cada NoTerminal.
        iterProduc = producciones.iterator();
        while (iterProduc.hasNext()) {
            Enumeration iterKey = this.no_terminales.keys();

            String p = (String) iterProduc.next();
            StringTokenizer token = new StringTokenizer(p, ":");

            token.nextToken();// Sacamos el lado izquierdo
            String ladoDerecho = token.nextToken().trim(); //sacamos el lado derecho.
            while (iterKey.hasMoreElements()) {
                String clave = (String) iterKey.nextElement();
                if (ladoDerecho.contains(clave)) {
                    NoTerminal nT = (NoTerminal) this.no_terminales.get(clave);
                    nT.setLadosDerecho(ladoDerecho);
                }
            }
        }

    }

    public void CalcularPrimeros() {
        Enumeration enuM = this.no_terminales.keys();

        while (enuM.hasMoreElements()) {
            Vector conjuntoPrimero = new Vector();
            String key = (String) enuM.nextElement();
            NoTerminal aux = (NoTerminal) this.no_terminales.get(key);

            this.primero(conjuntoPrimero, key);
            aux.setPrimero(conjuntoPrimero);
            this.agregarTodos(this.terminales, conjuntoPrimero);
        }
        //Agregamos el $ al final de los terminales.
        this.terminales.add("$");
        //Eliminamos el vacio de los terminales.
        this.tieneVacio(this.terminales);
    }

    private void primero(Vector terminales, String X) {

        if (this.no_terminales.containsKey(X)) {
            NoTerminal n = (NoTerminal) this.no_terminales.get(X);
            Iterator iter = n.getProducciones().iterator();
            //Itera sobre todas las producciones que tiene un NoTerminal.
            while (iter.hasNext()) {
                // Los elementos de lado derecho estan separados por espacios, ese el token.
                String prod = (String) iter.next();
                StringTokenizer token = new StringTokenizer(prod, " ");
                String elemento = token.nextToken();

                if (elemento.compareTo("@") == 0) {
                    this.agregar(terminales, "@");
                } else {
                    Vector aux = new Vector();
                    this.primero(aux, elemento);

                    while (this.tieneVacio(aux) && token.hasMoreTokens()) {
                        elemento = token.nextToken();
                        this.primero(aux, elemento);
                        if (!token.hasMoreTokens()) {
                            break;
                        }
                    }
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
     * Antes de utilizar esta funcion, primero se debe correr la funcion Primero()
     * @param terminales
     * @param prod Es la produccion a la cual le queremos hallar su conjunto primero.
     */
    public void primeroProduccion(Vector terminales, String prod) {

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

    public void CalcularSiguientes() {

        NoTerminal aux = (NoTerminal) this.no_terminales.get(this.simboloInicial);
        aux.getSiguiente().add("$");

        Enumeration enuM = this.no_terminales.keys();

        while (enuM.hasMoreElements()) {
            String key = (String) enuM.nextElement();
            aux = (NoTerminal) this.no_terminales.get(key);

            this.Siguiente(key, aux.getSiguiente(), key);
        }
    }

    private void Siguiente(String elementoReal, Vector conjuntoSiguientes, String elemento) {
        Enumeration enuM = this.no_terminales.keys();
        while (enuM.hasMoreElements()) {
            String key = (String) enuM.nextElement();
            NoTerminal aux = (NoTerminal) this.no_terminales.get(key);
            Iterator it = aux.getProducciones().iterator();
            while (it.hasNext()) {
                String prod = (String) it.next();
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

    public void imprimirConjuntosPrimeros() {
        Enumeration enu = this.no_terminales.elements();
        while (enu.hasMoreElements()) {
            NoTerminal nT = (NoTerminal) enu.nextElement();
            System.out.println(nT.toString() + "\n");
        }
    }

    public void agregarTodos(Vector vector, Vector elementos) {

        for (int i = 0; i < elementos.size(); i++) {
            String cad = (String) elementos.get(i);
            this.agregar(vector, cad);
        }
    }

    public void agregar(Vector vector, String elemento) {
        boolean existe = false;
        for (int i = 0; i < vector.size(); i++) {
            String cad = (String) vector.get(i);
            if (cad.equals(elemento)) {
                existe = true;
                break;
            }
        }

        if (!existe) {
            vector.add(elemento);
        }
    }

    public void hacerTablaASP() throws Exception {
        Enumeration iter = this.no_terminales.keys();
        while (iter.hasMoreElements()) {
            String noTerminal = (String) iter.nextElement();
            NoTerminal nT = (NoTerminal) this.no_terminales.get(noTerminal);
            Iterator iter2 = nT.getProducciones().iterator();
            while (iter2.hasNext()) {
                Vector primeros = new Vector();
                String prod = (String) iter2.next();
                this.primeroProduccion(primeros, prod);

                Iterator iter3 = primeros.iterator();
                while (iter3.hasNext()) {
                    String terminal = (String) iter3.next();
                    if (terminal.compareTo("@") != 0) {
                        if (nT.getFilaTabla().containsKey(terminal)) {
                            System.out.println("No-Terminal = "+nT.getNombre());
                            System.out.println("Terminal = "+terminal);
                            System.out.println("Ya esta = "+(String) nT.getFilaTabla().get(terminal));
                            System.out.println("Nuevo = "+prod);
                            throw new Exception("GRAMATICA AMBIGUA");
                        } else {
                            nT.getFilaTabla().put(terminal, prod);
                        }
                    }
                }
                if (this.tieneVacio(primeros)) {
                    iter3 = nT.getSiguiente().iterator();
                    while (iter3.hasNext()) {
                        String terminal = (String) iter3.next();
                        //En $ tambien ya inserta, por que para nosotros cualquier
                        //cosa que no es un NoTerminal es insertado como no Terminal. Excepto el @.
                        if (nT.getFilaTabla().containsKey(terminal)) {
                            System.out.println("No-Terminal = "+nT.getNombre());
                            System.out.println("Terminal = "+terminal);
                            System.out.println("Ya esta = "+(String) nT.getFilaTabla().get(terminal));
                            System.out.println("Nuevo = "+prod);
                            throw new Exception("GRAMATICA AMBIGUA");
                        } else {
                            nT.getFilaTabla().put(terminal, prod);
                        }
                    }

                }

            }
        }
    }

    public void imprimirTablaASP() {
        Enumeration iter = this.no_terminales.keys();

        while (iter.hasMoreElements()) {
            String noTerminal = (String) iter.nextElement();
            System.out.println("No Terminal: " + noTerminal);
            NoTerminal nT = (NoTerminal) this.no_terminales.get(noTerminal);
            System.out.println(nT.getFilaTabla().toString());
        }
    }

    public String analizar(String entrada) throws Exception {
        StringTokenizer tokens = new StringTokenizer(entrada, " ");
        Stack pila = new Stack();
        pila.push("$");
        pila.push(this.simboloInicial);
        //peek() devuelve el objeto que esta en el tope de la pila sin quitarlo.
        String X = (String) pila.peek();
        String preanalisis = tokens.nextToken();
        String derivacion = "";
        while (!X.equals("$")) {
            if (X.equals("$") || !this.no_terminales.containsKey(X)) {
                if (X.equals(preanalisis)) {
                    pila.pop();
                    preanalisis = tokens.nextToken();
                } else {
                    throw new Exception("NO PERTENECE");
                }
            } else {
                NoTerminal nT = (NoTerminal) this.no_terminales.get(X);
                String prod = (String) nT.getFilaTabla().get(preanalisis);
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
                    throw new Exception("NO PERTENECE");
                }
            }
            X = (String)pila.peek();
        }
        this.derivacion = derivacion;
        return derivacion;
    }

    public String invertirCadena(String cadena) {
        String cadenaInvertida = "";
        StringTokenizer tokens = new StringTokenizer(cadena, " ");
        while(tokens.hasMoreElements()) {
            cadenaInvertida = tokens.nextToken() +" " + cadenaInvertida;
        }
        return cadenaInvertida.trim();
    }
}
