/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DamasUNA;

import java.util.ArrayList;
import java.util.Vector;



/**
 *
 * @author Ramon
 */
public class Main2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //                   0  1   2   3  4  5  6  7
        int tablero [][]=  {{1, 0 , 0 , 0, 1, 0, 0, 0},//0
                           { 0, 1 , 0 , 1, 0, 0, 0, 1},//1
                           { 1, 0 , 0 , 0, 1, 0, 0, 0},//2
                           { 0, 0 , 0 , 0, 0, 1, 0, 0},//3
                           { 0, 0 ,-1 , 0, 0, 0, 0, 0},//4
                           { 0, 1 , 0 ,-1, 0, 0, 0, 0},//5
                           { 0, 0 , 0 , 0, 0, 0,-1, 0},//6
                           { 0,-1 , 0 ,-1, 0, 0, 0,-1}};//7
              
           Tablero table= new Tablero(tablero);
           table.setTurnoBlancas(false);
           IEstrategia jugador2 = new AlfaBeta(3);
           Tablero table2=jugador2.jugar(table);
           System.out.println("------------------------------------------------------");
           System.out.println(table.toString());
           System.out.println(table2.toString());
          // System.out.println(table2.toString());
//        Tablero table= new Tablero(tablero);
//        table.setTurnoBlancas(false);
//        IEstrategia jugador2 = new AlfaBeta(3);
//        IEstrategia jugador1 = new MaxMin(3);
//        Tablero table2=jugador2.jugar(table);
//        System.out.println(table.toString());
//        System.out.println("Jugada Alfa Beta");
//        System.out.println(table2.toString());
//        System.out.println("Cantidad de nodos AlfaBeta="+jugador2.numNodos);
//        System.out.println("Jugada MaxMin");
//        Tablero table1=jugador1.jugar(table);
//        System.out.println(table1.toString());
//        System.out.println("Cantidad de nodos MaxMin="+jugador1.numNodos);
        
        
        
        
//        table.ComerMayorCant(table.getFicha(4, 4),0);
//        table.GenerarSucesores();
//        Vector resultado = table.getSucesores();
//        Tablero tabla = null;
//        for (int i = 0; i < resultado.size(); i++) {
//            tabla = (Tablero)resultado.get(i);
//            tabla.CalcularValorDeEstado();
//            System.out.println("El puntaje es:"+tabla.puntaje);
//            System.out.println("\n"+tabla.toString()+"\n");
//            for (int j = 0; j < tabla.VectorDeComidas.size(); j++) {
//                Ficha f1=(Ficha)tabla.VectorDeComidas.get(j);
//                System.out.print(f1.toString()+",\n");
//                
//            }
//            System.out.println(tabla.fichaMovida.getPosAnterior().toString());
//                
//        }
//        System.out.println(resultado.size());
        
        //Posicion p= new Posicion(3,5);
        //System.out.println(p.esValida());
//        Ficha[][] is = table.getFichas();
//        for (int i = 0; i < is.length; i++) {
//            for (int j = 0; j < is.length; j++) {
//                if(((i+j)%2)==0)
//                {
//                    Ficha f1= is[i][j];
//                    if(f1!=null && table.puedeComer(f1))
//        
//                        System.out.println("La ficha ("+f1.getPos().fila+","+f1.getPos().columna+") SI puede comer..");
//                }
//                
//            }    
//            
//        }
        
     }

}
