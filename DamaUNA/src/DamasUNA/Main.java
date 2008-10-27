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
        Aleatorio jugador1 = new Aleatorio();
        MaxMin jugador2 = new MaxMin(4);
        Tablero jugada = null;
        double nodosJugador1 = 0;
        double nodosJugador2 = 0;
        double jugoJugador1 = 0;
        double jugoJugador2 = 0;
        double time1 = 0;        
        double time2 = 0;
        double timeJugador1 = 0;        
        double timeJugador2 = 0;
        long fin = System.currentTimeMillis() + 150000;
        while(System.currentTimeMillis() <= fin){            
            
            table.setTurnoBlancas(true);
            if(table.cantidadFichasDelTurno() != 0){
                Tablero aux = new Tablero(tablero);
                aux.setTurnoBlancas(true);
                time1 = System.nanoTime()/1000.0;
                jugada = jugador1.jugar(aux);
                time2 = System.nanoTime()/1000.0;
                timeJugador1 = time2 - time1;
                nodosJugador1 = nodosJugador1+jugador1.numNodos;
                jugoJugador1++;
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
            table.Tabla=jugada.clonarTabla(jugada.Tabla);
            if(table.getTurnoBlancas())
                System.out.println("Jugó blancas");
            else
                System.out.println("Jugó negras");
            jugada = null;
            
            System.out.println(table.toString());
            table.setTurnoBlancas(false);
                        
            if(table.cantidadFichasDelTurno() != 0){
                Tablero aux = new Tablero(tablero);
                aux.setTurnoBlancas(false);
                time1 = System.nanoTime()/1000.0;
                jugada = jugador2.jugar(aux);
                time2 = System.nanoTime()/1000.0;
                timeJugador2 = time2 - time1;
                nodosJugador2 = nodosJugador2+jugador2.numNodos;
                jugoJugador2++;
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
            table.Tabla=jugada.clonarTabla(jugada.Tabla);
            if(table.getTurnoBlancas())
                System.out.println("Jugó blancas");
            else
                System.out.println("Jugó negras");
            jugada = null;
            System.out.println(table.toString());
        }
        System.out.println("------------------------------------------------------");
        System.out.println("********Estadisticas Jugador1********");
        System.out.println("El tiempo promedio que demora en retornar una jugada = "+timeJugador1/jugoJugador1+" milisegundos");
        System.out.println("La cantidad de estados promedio que visita la búsqueda = "+nodosJugador1/jugoJugador1+" estados");
        System.out.println("\n");
        System.out.println("********Estadisticas Jugador2********");
        System.out.println("El tiempo promedio que demora en retornar una jugada = "+timeJugador2/jugoJugador2+" milisegundos");
        System.out.println("La cantidad de estados promedio que visita la búsqueda = "+nodosJugador2/jugoJugador2+" estados");
        
//        table.ComerMayorCant(table.getFicha(4, 4),0);
//        table.setTurnoBlancas(false);
//        table.GenerarSucesores();
//        Vector resultado = table.getSucesores();
//        Tablero tabla = null;
//        for (int i = 0; i < resultado.size(); i++) {
//            tabla = (Tablero)resultado.get(i);
//            System.out.println("\n"+tabla.toString()+"\n");
//            for (int j = 0; j < tabla.VectorDeFichasComidas.size(); j++) {
//                Ficha f1=(Ficha)tabla.VectorDeFichasComidas.get(j);
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
