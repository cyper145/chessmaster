
package ASP_NR;

import java.util.Hashtable;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Esta clase es utilizada para ejecutar el programa en caso que el ambiente \
 * Web no funcione.
 * @author Manuel Avalos
 * @author Arturo Ferreira
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        // TODO code application logic here
        try {
            Archivo arch = new Archivo("src\\ASP_NR\\");
            arch.setFileNameIN("archivoEntrada.txt");
            Vector producciones = arch.leer();

            ASP anal = new ASP();
            anal.setTablaHash(producciones);
            anal.CalcularPrimeros();
            anal.CalcularSiguientes();
            anal.imprimirNoTerminales();
            anal.hacerTablaASP();
            anal.imprimirTablaASP();
            String entrada = "id + id * id $";

            String derivacion = anal.analizar(entrada);
            System.out.println("\nDerivación realizada:\n" + derivacion);
        } catch (Exception e) {
            if(e.getMessage().equals("GRAMATICA AMBIGUA")){
                System.out.println("ERROR = La Gramatica dada es ambigua.");
            }else if(e.getMessage().equals("NO PERTENECE")){
                System.out.println("ERROR = La cadena de entrada no pertenece al lenguaje.");
            }else{
                System.out.println("OCURRIO UN ERROR\n");
                e.printStackTrace();
            }
        }
    }
}
