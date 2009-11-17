/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ASP_NR;

import java.util.Vector;
import java.util.Hashtable;
/**
 * Esta clase representa a un No-Terminal, conteniendo la informacion necesaria para el mismo.
 * @author Arturo
 */
public class NoTerminal {
    private Vector Primero;// Conjunto (String) primero de Terminales.
    private Vector Siguiente; // Conjunto (String) siguiente de terminales.
    private String Nombre; // Nombre que representa al No terminal.
    private Hashtable FilaTabla;// Tabla Hash que guarda la fila en la Tabla ASP correspondiente al
                                //No terminal. La clave es un String Terminal, y el resultado
                                //es un Vector que representa las producciones que se tienen
                                //con un Terminal dado para este No-Terminal.

    private Vector Producciones; //Vector de String que guarda la producciones del no terminal.

    /**
     * Constructor de la clase con parametro. Inicializa las variables que se utilizaran.
     * @param Nombre Caontiene el String que representa al No-Terminal.
     */
    public NoTerminal(String Nombre) {
        this.Nombre=Nombre;
        this.Producciones=new Vector();

        this.Primero= new Vector();
        this.Siguiente= new Vector();
        this.FilaTabla= new Hashtable();
    }
    /**
     * Este metodo nos retorna la fila de la tabla de ASP que corresponde a este No-Terminal.
     * Cada no terminal contiene su propia fila que consiste en una Tabla Hash. La clave es un String
     * Terminal, y el resultado que se obtiene es un Vector que representa las producciones que se tienen
     * con un Terminal dado para este No-Terminal. Si en esta Tabla Hash existe un vector con mas de 1
     * elemento la Gramatica tratada es ambigua.
     * @return
     */
    public Hashtable getFilaTabla() {
        return FilaTabla;
    }

    /**
     *
     * @return True si ya tiene conjunto primero, y false si es que no tiene.
     */
    public boolean tieneConjuntoPrimero()
    {
        return !this.Primero.isEmpty();
    }

    /**
     *
     * @return Se devuelve la cadena que representa a este No-Terminal.
     */
    public String getNombre() {
        return Nombre;
    }

    /**
     *
     * @return Este metodo nos devuelve el Conjunto Primero del No-Terminal.
     */
    public Vector getPrimero() {
        return Primero;
    }

    /**
     * 
     * @return Retorna un vector de String que representan las producciones a las cuales puede derivar el No-Terminal.
     */
    public Vector getProducciones() {
        return Producciones;
    }

    /**
     *
     * @return Este metodo nos devuelve el Conjunto Siguiente del No-Terminal.
     */
    public Vector getSiguiente() {
        return Siguiente;
    }

    /**
     * Este metodo inserta la produccion correspondiente para un Terminal dado en la Fila de la Tabla
     * de ASP que maneja el No-Terminal.
     * @param terminal La cadena que representa al terminal.
     * @param produccion La cadena que representa a la produccion que se utilizara en el Analisis.
     * @return Retorna true si es que ya existe un elemento en el vector que corresponde al Terminal recibido,
     * false si es la unica produccion la que se esta insertando, Esto se realiza de manera a saber que la
     * Gramatica es ambigua.
     */
    public boolean setFilaTabla(String terminal, String produccion) {
        boolean esAmbiguo;
        Vector producciones;
        //Si es que el terminal ya existe en la Tabla Hash quiere decir que ya tiene un elemento para
        //el mismo terminal.
        if(this.FilaTabla.containsKey(terminal))
        {
            esAmbiguo=true;
            producciones =(Vector) this.FilaTabla.get(terminal);
            producciones.add(produccion);
            
        }else
        {
            esAmbiguo=false;
            producciones = new Vector();
            producciones.add(produccion);
            this.FilaTabla.put(terminal, producciones);
        }

        return esAmbiguo;
    }
    /**
     * Setea el nombre del No-Terminal.
     * @param Nombre Cadena que representa al nombre del No-Terminal.
     */
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    /**
     * Setea el vector del Conjunto Primero del No-Terminal.
     * @param Primero Vector que contiene el Conjunto Primero.
     */
    public void setPrimero(Vector Primero) {
        this.Primero = Primero;
    }
    /**
     * Setea el vector de Producciones a los que puede derivar el No-Terminal.
     * @param Produccion Vector que contiene las Producciones.
     */
    public void setProducciones(String Produccion) {
        this.Producciones.add(Produccion) ;
    }

    /**
     * Setea el vector del Conjunto Siguiente del No-Terminal.
     * @param Siguiente Vector que contiene el Conjunto Siguiente.
     */
    public void setSiguiente(Vector Siguiente) {
        this.Siguiente = Siguiente;
    }
    /**
     *
     * @return Retorna una cadena que contiene informacion sobre el No-Terminal, como su Nombre, y sus Conjuntos Primero
     * y Siguiente.
     */
    public String toString() {
        String superCadena="No terminal: "+this.Nombre+"\nConjunto Primero: "+ this.Primero.toString()+"\nConjunto Siguiente: "+ this.Siguiente.toString();
        return superCadena;
    }



}
