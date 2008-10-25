package DamasUNA;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuario
 */
public class AleatoriovsMinimax {
    
                              //                           0  1   2   3  4  5  6  7
     /*  public int matTablero2 [][]=  {{1, 0 , 1 , 0, 1, 0, 1, 0},//0
                           { 0, 1 , 0 , 1, 0, 1, 0, 1},//1
                           { 1, 0 , 1 , 0, 1, 0, 1, 0},//2
                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//3
                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//4
                           { 0,-1 , 0 ,-1, 0,-1, 0,-1},//5
                           {-1, 0 ,-1 , 0,-1, 0,-1, 0},//6
                           { 0,-1 , 0 ,-1, 0,-1, 0,-1}};//7  */
    
public int[][] jugar() {
                              //                           0  1   2   3  4  5  6  7
        int matTablero [][]=  {{1, 0 , 1 , 0, 1, 0, 1, 0},//0
                           { 0, 1 , 0 , 1, 0, 1, 0, 1},//1
                           { 1, 0 , 1 , 0, 1, 0, 1, 0},//2
                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//3
                           { 0, 0 , 0 , 0, 0, 0, 0, 0},//4
                           { 0,-1 , 0 ,-1, 0,-1, 0,-1},//5
                           {-1, 0 ,-1 , 0,-1, 0,-1, 0},//6
                           { 0,-1 , 0 ,-1, 0,-1, 0,-1}};//7
        
        Tablero table= new Tablero(matTablero);
        System.out.println(table.toString());
        System.out.println("------------------------------------------------------");
        
        //IEstrategia jugador1 = new Aleatorio();
        IEstrategia jugador1 = new Aleatorio();//verde
        IEstrategia jugador2 = new MaxMin(3);//azul
        Tablero jugada = null;
        
        long fin = System.currentTimeMillis() + 300000;
        while(true){            
            
            table.setTurnoBlancas(true);
            if(table.tieneFichasEnTablero() != 0){
                Tablero aux = new Tablero(matTablero);
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
            matTablero = jugada.getTablero(); 
            
            
            
            
            table.Tabla=jugada.clonarTabla(jugada.Tabla);
            if(table.getTurnoBlancas())
                System.out.println("Jugó blancas");
            else
                System.out.println("Jugó negras");
            jugada = null;
            
            System.out.println(table.toString());
            
           table.setTurnoBlancas(false);
                        
            if(table.tieneFichasEnTablero() != 0){
                Tablero aux = new Tablero(matTablero);
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
            matTablero = jugada.getTablero();
            table.Tabla=jugada.clonarTabla(jugada.Tabla);
            if(table.getTurnoBlancas())
                System.out.println("Jugó blancas");
            else
                System.out.println("Jugó negras");
            jugada = null;
            System.out.println(table.toString());
                  }
                       return table.Tabla;
		      	     }
    
}
