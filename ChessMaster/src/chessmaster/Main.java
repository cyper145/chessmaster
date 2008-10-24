/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chessmaster;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Ramon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//                           0  1   2   3  4  5  6  7
        int tablero [][]=  {{1, 0 , 1 , 0, 1, 0, 1, 0},//0
                           { 0, 1 , 0 , 1, 0, 1, 0, 1},//1
                           { 1, 0 , 1 , 0, 1, 0, 1, 0},//2
                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//3
                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//4
                           { 0,-1 , 0 ,-1, 0,-1, 0,-1},//5
                           {-1, 0 ,-1 , 0,-1, 0,-1, 0},//6
                           { 0,-1 , 0 ,-1, 0,-1, 0,-1}};//7
        
        //                   0  1   2   3  4  5  6  7
//        int tablero [][]=  {{1, 0 , 0 , 0, 0, 0, 0, 0},//0
//                           { 0, 1 , 0 , 1, 0, 1, 0, 1},//1
//                           {-1, 0 ,-1 , 0,-1, 0,-1, 0},//2
//                           { 0,-1 , 0 ,-1, 0, 0, 0, 0},//3
//                           { 0, 0 , 0 , 0, 1, 0, 1, 0},//4
//                           { 0, 1 , 0 , 1, 0,-1, 0,-1},//5
//                           {-1, 0 ,-1 , 0, 0, 0, 0, 0},//6
//                           { 0, 0 , 0 , 2, 0, 0, 0, 2}};//7
        
//        //                   0  1   2   3  4  5  6  7
//        int tablero [][]=  {{0, 0 , 0 , 0, 0, 0, 0, 0},//0
//                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//1
//                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//2
//                           { 0,-2 , 0 , 0, 0, 0, 0, 0},//3
//                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//4
//                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//5
//                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//6
//                           { 0, 0 , 0 , 0, 0, 0, 0, 2}};//7
        
        
        Tablero table= new Tablero(tablero);
        System.out.println(table.toString());
        System.out.println("------------------------------------------------------");
        
        //IEstrategia jugador1 = new Aleatorio();
        IEstrategia jugador1 = new AlfaBeta(4);
        IEstrategia jugador2 = new MaxMin(3);
        Tablero jugada = null;
        
        long fin = System.currentTimeMillis() + 300000;
        while(System.currentTimeMillis() <= fin){            
            
            table.setTurnoBlancas(true);
            if(table.tieneFichasEnTablero() != 0){
                Tablero aux = new Tablero(tablero);
                aux.setTurnoBlancas(true);
                jugada = jugador1.jugar(aux);
                if(jugada == null){
                    System.out.println(" 1 Ha ganado el Jugador con fichas negras");
                    break;
                }
            }else{
                System.out.println(" 2 Ha ganado el Jugador con fichas negras");
                break;
            }
            //actualizar el tablero original...
            jugada.coronarPeon();
            tablero = jugada.getTablero();            
            table.setFichas(jugada.clonarFichas());
            if(table.getTurnoBlancas())
                System.out.println("Jugó blancas");
            else
                System.out.println("Jugó negras");
            jugada = null;
            
            System.out.println(table.toString());
            table.setTurnoBlancas(false);
                        
            if(table.tieneFichasEnTablero() != 0){
                Tablero aux = new Tablero(tablero);
                aux.setTurnoBlancas(false);
                jugada = jugador2.jugar(aux);
                if(jugada == null){
                    System.out.println(" 3 Ha ganado el Jugador con fichas blancas");
                    break;
                }
            }else{
                System.out.println(" 4 Ha ganado el Jugador con fichas blancas");
                break;
            }
            jugada.coronarPeon();
            tablero = jugada.getTablero();
            table.setFichas(jugada.clonarFichas());
            if(table.getTurnoBlancas())
                System.out.println("Jugó blancas");
            else
                System.out.println("Jugó negras");
            jugada = null;
            System.out.println(table.toString());
        }
        
        
//        table.ComerMayorCant(table.getFicha(4, 4),0);
//        table.setTurnoBlancas(false);
//        table.GenerarSucesores();
//        Vector resultado = table.getSucesores();
//        Tablero tabla = null;
//        for (int i = 0; i < resultado.size(); i++) {
//            tabla = (Tablero)resultado.get(i);
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
        
        
        
        
        
       // System.out.println(table.CalcularValorDeEstado());
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
