package DamasUNA;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *///package chessmaster;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramon
 */
public class MaquinaVsMaquina extends Thread{

    private TableroGUI ITablero;
    public int VELOCIDAD=5;
    private IEstrategia jugador1;
    private IEstrategia jugador2;
    private String nameE1;
    private String nameE2;

    public MaquinaVsMaquina(IEstrategia j1,IEstrategia j2, String nameE1, String nameE2) {
        this.ITablero= new TableroGUI('A', nameE1+"(Verde)Vs"+nameE2+"(azul)");
        this.jugador1=j1;
        this.jugador2=j2;
        this.nameE1=nameE1;
        this.nameE2=nameE2;
    }

    
    
    public void run() {
        //                           0  1   2   3  4  5  6  7
        int matTablero[][] = {{1, 0, 1, 0, 1, 0, 1, 0},//0
            {0, 1, 0, 1, 0, 1, 0, 1},//1
            {1, 0, 1, 0, 1, 0, 1, 0},//2
            {0, 0, 0, 0, 0, 0, 0, 0},//3
            {0, 0, 0, 0, 0, 0, 0, 0},//4
            {0, -1, 0, -1, 0, -1, 0, -1},//5
            {-1, 0, -1, 0, -1, 0, -1, 0},//6
            {0, -1, 0, -1, 0, -1, 0, -1}};//7

        Tablero table = new Tablero(matTablero);
        System.out.println(table.toString());
        System.out.println("------------------------------------------------------");

        
        Tablero jugada = null;

        long fin = System.currentTimeMillis() + 300000;
        while (true) {

            table.setTurnoBlancas(true);
            if (table.cantidadFichasDelTurno() != 0) {
                Tablero aux = new Tablero(matTablero);
                aux.setTurnoBlancas(true);
                
                int cant1=aux.cantidadTotalPiezas();
                
                jugada = jugador1.jugar(aux);
                if (jugada == null) {
                    System.out.println(" 1 Ha ganado el Jugador con fichas azules");
                    JOptionPane.showMessageDialog( this.ITablero,
		                	this.nameE2+" ha ganado!, "+ this.nameE1+ " se ha quedado kure'i...",
		                     "Fin del Juego-"+this.nameE1+"Vs"+this.nameE2, JOptionPane.PLAIN_MESSAGE );	
                    break;
                } else {
                    int cant2= jugada.cantidadTotalPiezas();
                    int ctotal=cant1-cant2;
                    //actualizar el tablero original...
                    jugada.coronarPeon();
                    matTablero = jugada.getTablero();
                    table.Tabla = jugada.clonarTabla(jugada.Tabla);
                    table.setTurnoBlancas(false);
                   
                    //Desde aca muestra se hace para mostrar en la consola

                    if (table.getTurnoBlancas()) {
                        System.out.println("Jugó blancas");
                    } else {
                        System.out.println("Jugó negras");
                    }
                    System.out.println(table.toString());
                    
                    //Prueba de consistencia....
                    if(ctotal<0)
                    {
                        System.out.println("Algo anda mal... Aparecieron nuevas piezas");
                    }
                    else if(ctotal!=jugada.VectorDeFichasComidas.size())
                    {
                        System.out.println("Desaparecieron fichas");
                    }
                    
                    //Desde aca vamos a usar la interfaz grafica...

                    //Hacemos el movimiento dependiendo si se comieron piezas o si solo se movio una.
                    if (jugada.VectorDeFichasComidas.size() != 0) {
                        //Ubicamos a la pieza en su nueva posicion luego de comer piezas...
                        Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                        Posicion pfinal = jugada.fichaMovida.getPos();
                        long t = System.currentTimeMillis() + 5000/this.VELOCIDAD;
                        while (t > System.currentTimeMillis());
                        this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                        this.ITablero.cambiarTurno();
                        //Esperamos 2 segundo para borrar las piezas comidas...
                       

                        for (int i = 0; i < jugada.VectorDeFichasComidas.size(); i++) {
                            t = System.currentTimeMillis() + 5000/this.VELOCIDAD;
                            while (t > System.currentTimeMillis());
                            Ficha comida = (Ficha) jugada.VectorDeFichasComidas.get(i);
                            this.ITablero.comerPieza(comida.getPos().fila, comida.getPos().columna);

                        }

                    } else {
                        //Ubicamos a la pieza en su nueva posicion...
                        Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                        Posicion pfinal = jugada.fichaMovida.getPos();
                        long t = System.currentTimeMillis() + 5000/this.VELOCIDAD;
                        while (t > System.currentTimeMillis());
                        this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                        this.ITablero.cambiarTurno();
                    }
                     jugada = null;

                }
            } else {
                System.out.println(" 2 Ha ganado el Jugador con fichas azules");
                JOptionPane.showMessageDialog( this.ITablero,
		                	this.nameE2+" ha ganado!, "+this.nameE1+" ya no tiene piezas..",
		                     "Fin del Juego-"+this.nameE1+"Vs"+this.nameE2, JOptionPane.PLAIN_MESSAGE );
                break;
            }






            if (table.cantidadFichasDelTurno() != 0) {
                Tablero aux = new Tablero(matTablero);
                aux.setTurnoBlancas(false);
                int cant1=aux.cantidadTotalPiezas();
                jugada = jugador2.jugar(aux);
                
                if (jugada == null) {
                    System.out.println(" 3 Ha ganado el Jugador con fichas verdes");
                    JOptionPane.showMessageDialog( this.ITablero,
		                	this.nameE1+" ha ganado, "+this.nameE2+" ya no puede moverse!",
		                     "Fin del Juego-"+this.nameE1+"Vs"+this.nameE2, JOptionPane.PLAIN_MESSAGE );
                    break;
                } else {
                    int cant2= jugada.cantidadTotalPiezas();
                    int ctotal=cant1-cant2;
                    //actualizar el tablero original...
                    jugada.coronarPeon();
                    matTablero = jugada.getTablero();
                    table.Tabla = jugada.clonarTabla(jugada.Tabla);
                    table.setTurnoBlancas(true);
                    
                    //Desde aca muestra se hace para mostrar en la consola

                    if (table.getTurnoBlancas()) {
                        System.out.println("Jugó blancas");
                    } else {
                        System.out.println("Jugó negras");
                    }
                    System.out.println(table.toString());
                    if(ctotal<0)
                    {
                        System.out.println("Algo anda mal... Aparecieron nuevas piezas");
                    }
                    else if(ctotal!=jugada.VectorDeFichasComidas.size())
                    {
                        System.out.println("Desaparecieron fichas");
                    }
                    //Desde aca vamos a usar la interfaz grafica...

                    //Hacemos el movimiento dependiendo si se comieron piezas o si solo se movio una.
                    if (jugada.VectorDeFichasComidas.size() != 0) {
                        //Ubicamos a la pieza en su nueva posicion luego de comer piezas...
                        Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                        Posicion pfinal = jugada.fichaMovida.getPos();
                        long t = System.currentTimeMillis() + 5000/this.VELOCIDAD;
                        while (t > System.currentTimeMillis());
                       this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                        this.ITablero.cambiarTurno();
       
                        //Esperamos 2 segundo para borrar las piezas comidas...
                        

                        for (int i = 0; i < jugada.VectorDeFichasComidas.size(); i++) {
                            t = System.currentTimeMillis() + 5000/this.VELOCIDAD;
                            while (t > System.currentTimeMillis());
                            Ficha comida = (Ficha) jugada.VectorDeFichasComidas.get(i);
                            this.ITablero.comerPieza(comida.getPos().fila, comida.getPos().columna);

                        }

                    } else {
                        //Ubicamos a la pieza en su nueva posicion...
                        Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                        Posicion pfinal = jugada.fichaMovida.getPos();
                        long t = System.currentTimeMillis() + 5000/this.VELOCIDAD;
                        while (t > System.currentTimeMillis());
                        this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                        this.ITablero.cambiarTurno();
                    }
                    jugada = null;
                }
            } else {
                System.out.println(" 4 Ha ganado el Jugador con fichas verdes");
                JOptionPane.showMessageDialog( this.ITablero,
		                	this.nameE1+" ha ganado, "+this.nameE2+ " ya no tiene piezas!",
		                    "Fin del Juego"+this.nameE1+"Vs"+this.nameE2, JOptionPane.PLAIN_MESSAGE );
                break;
            }
        }

    }

    void inicializar(JDesktopPane mdiForm) {
        
        this.ITablero.muestra(mdiForm);

    }
    
}