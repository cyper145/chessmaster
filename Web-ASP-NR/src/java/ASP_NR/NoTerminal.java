/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ASP_NR;

import java.util.Vector;
import java.util.Hashtable;
/**
 *
 * @author Arturo
 */
public class NoTerminal {
    private Vector Primero;// Conjunto (String) primero de Terminales.
    private Vector Siguiente; // Conjunto (String) siguiente de terminales.
    private String Nombre; // Nombre que representa al No terminal.
    private Hashtable FilaTabla;// Tabla Hash que guarda la fila en la Tabla ASP correspondiente al
                                //No terminal. La clave es un String Terminal, y el resultado
                                //es un String que representa la produccion resultante.

    private Vector Producciones; //Vector de String que guarda la producciones del no terminal.
    private Vector ladosDerecho; //Vector de String que guarda los lados derecho donde aparace el NoTerminal.

    public NoTerminal(String Nombre) {
        this.Nombre=Nombre;
        this.Producciones=new Vector();

        this.ladosDerecho=new Vector();
        this.Primero= new Vector();
        this.Siguiente= new Vector();
        this.FilaTabla= new Hashtable();
    }

    public Hashtable getFilaTabla() {
        return FilaTabla;
    }

    public void setLadosDerecho(String ladosDerecho) {
        this.ladosDerecho.add(ladosDerecho);
    }

    /**
     *
     * @return True si ya tiene conjunto primero, y false si es que no tiene.
     */
    public boolean tieneConjuntoPrimero()
    {
        return !this.Primero.isEmpty();
    }

    public String getNombre() {
        return Nombre;
    }

    public Vector getPrimero() {
        return Primero;
    }

    public Vector getProducciones() {
        return Producciones;
    }

    public Vector getSiguiente() {
        return Siguiente;
    }

    public boolean setFilaTabla(String terminal, String produccion) {
        boolean esAmbiguo;
        Vector producciones;
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

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setPrimero(Vector Primero) {
        this.Primero = Primero;
    }

    public void setProducciones(String Produccion) {
        this.Producciones.add(Produccion) ;
    }

    public void setSiguiente(Vector Siguiente) {
        this.Siguiente = Siguiente;
    }

    public String toString() {
        String superCadena="No terminal: "+this.Nombre+"\nConjunto Primero: "+ this.Primero.toString()+"\nConjunto Siguiente: "+ this.Siguiente.toString();
        return superCadena;
    }



}
