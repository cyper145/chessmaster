/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ASP_NR;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Arturo
 */
public class ASP {

    public Vector terminales;// Vector de String que contiene todos los terminales.
    public Hashtable no_terminales;// Vector que contiene objetos No_terminales
    public String simboloInicial;

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
        }
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

    public void CalcularSiguientes() {

        NoTerminal aux = (NoTerminal) this.no_terminales.get(this.simboloInicial);
        aux.getSiguiente().add("$");

        Enumeration enuM = this.no_terminales.keys();

        while (enuM.hasMoreElements()) {
            String key = (String) enuM.nextElement();
            aux = (NoTerminal) this.no_terminales.get(key);

            this.Siguiente(aux.getSiguiente(), key);
        }
    }

    private void Siguiente(Vector conjuntoSiguientes, String elemento) {
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
                    betha = prod.substring(indice + 1, prod.length()).trim();
                    if (!betha.isEmpty()) {
                        StringTokenizer tokens = new StringTokenizer(betha, " ");
                        String tok = tokens.nextToken().trim();
                        if (this.no_terminales.containsKey(tok)) {
                            NoTerminal nt = (NoTerminal) this.no_terminales.get(tok);
                            agregarTodos(conjuntoSiguientes, nt.getPrimero());
                            if (this.tieneVacio(conjuntoSiguientes)) {
                                if (key != elemento) {
                                    this.Siguiente(aux.getSiguiente(), key);
                                    agregarTodos(conjuntoSiguientes, aux.getSiguiente());
                                }
                            }
                        } else {
                            this.agregar(conjuntoSiguientes, tok);
                        }
                    } else {
                        if (key != elemento) {
                            this.Siguiente(aux.getSiguiente(), key);
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
            String cad = (String)elementos.get(i);
            this.agregar(vector, cad);
        }
    }
    public void agregar(Vector vector, String elemento) {
        boolean existe = false;
        for (int i = 0; i < vector.size(); i++) {
            String cad = (String)vector.get(i);
            if(cad.equals(elemento)){
                existe = true;
                break;
            }
        }

        if(!existe)
            vector.add(elemento);
    }
}
