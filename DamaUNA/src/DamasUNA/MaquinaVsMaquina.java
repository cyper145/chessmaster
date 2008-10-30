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
public class MaquinaVsMaquina extends Thread {

    private TableroGUI ITablero;
    public int VELOCIDAD = 10;
    private IEstrategia jugador1;
    private IEstrategia jugador2;
    private String nameE1;
    private String nameE2;

    public MaquinaVsMaquina(IEstrategia j1, IEstrategia j2, String nameE1, String nameE2) {
        this.ITablero = new TableroGUI('A', nameE1 + "(Verde)Vs" + nameE2 + "(azul)");
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.nameE1 = nameE1;
        this.nameE2 = nameE2;
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

        Tablero jugada = new Tablero(matTablero);
        jugada.setTurnoBlancas(true);

        long fin = System.currentTimeMillis() + 300000;
        double nodosJugador1 = 0;
        double nodosJugador2 = 0;
        double jugoJugador1 = 0;
        double jugoJugador2 = 0;
        double time1 = 0;
        double time2 = 0;
        double timeJugador1 = 0;
        double timeJugador2 = 0;
        boolean timeOver=false;
        try {


            while (!this.ITablero.salio && !timeOver) 
            {
                timeOver=!(fin > System.currentTimeMillis());
                if (jugada != null && jugada.cantidadFichasDelTurno() != 0) {
                    int cant1 = jugada.cantidadTotalPiezas();

                    time1 = System.nanoTime() / 1000.0;
                    jugada = jugador1.jugar(jugada);
                    time2 = System.nanoTime() / 1000.0;
                    timeJugador1 = time2 - time1;
                    nodosJugador1 = nodosJugador1 + jugador1.numNodos;
                    jugoJugador1++;
                    jugador1.numNodos=0;
                    if (jugada == null) {
                        System.out.println(" 1 Ha ganado el Jugador con fichas azules");
                        JOptionPane.showMessageDialog(this.ITablero,
                                this.nameE2 + " ha ganado!, " + this.nameE1 + " se ha quedado kure'i...",
                                "Fin del Juego-" + this.nameE1 + "Vs" + this.nameE2, JOptionPane.PLAIN_MESSAGE);
                        break;
                    } else {

                        int cant2 = jugada.cantidadTotalPiezas();
                        int ctotal = cant1 - cant2;
                        //actualizar el tablero original...
                        jugada.coronarPeon();

                        //Desde aca muestra se hace para mostrar en la consola

//                        if (jugada.getTurnoBlancas()) {
//                            System.out.println("Jugó blancas");
//                        } else {
//                            System.out.println("Jugó negras");
//                        }
//                        System.out.println(jugada.toString());

                        //Prueba de consistencia....
//                        if (ctotal < 0) {
//                            System.out.println("Algo anda mal... Aparecieron nuevas piezas");
//                        } else if (ctotal != jugada.VectorDeFichasComidas.size()) {
//                            System.out.println("Desaparecieron fichas");
//                        }

                        //Desde aca vamos a usar la interfaz grafica...

                        //Hacemos el movimiento dependiendo si se comieron piezas o si solo se movio una.
                        if (jugada.VectorDeFichasComidas.size() != 0) {
                            //Ubicamos a la pieza en su nueva posicion luego de comer piezas...
                            Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                            Posicion pfinal = jugada.fichaMovida.getPos();
                            long t = System.currentTimeMillis() + 5000 / this.VELOCIDAD;
                            while (t > System.currentTimeMillis());
                            this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                            this.ITablero.cambiarTurno();
                            //Esperamos 2 segundo para borrar las piezas comidas...


                            for (int i = 0; i < jugada.VectorDeFichasComidas.size(); i++) {
                                t = System.currentTimeMillis() + 5000 / this.VELOCIDAD;
                                while (t > System.currentTimeMillis());
                                Ficha comida = (Ficha) jugada.VectorDeFichasComidas.get(i);
                                this.ITablero.comerPieza(comida.getPos().fila, comida.getPos().columna);

                            }

                        } else {
                            //Ubicamos a la pieza en su nueva posicion...
                            Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                            Posicion pfinal = jugada.fichaMovida.getPos();
                            long t = System.currentTimeMillis() + 5000 / this.VELOCIDAD;
                            while (t > System.currentTimeMillis());
                            this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                            this.ITablero.cambiarTurno();
                        }
                        //Le toca el turno a las negras
                        jugada.limpiarEstado();//Cero el nivel para la jugada futura...
                        jugada.setTurnoBlancas(false);

                    }
                } else {
                    System.out.println(" 2 Ha ganado el Jugador con fichas azules");
                    JOptionPane.showMessageDialog(this.ITablero,
                            this.nameE2 + " ha ganado!, " + this.nameE1 + " ya no tiene piezas..",
                            "Fin del Juego-" + this.nameE1 + "Vs" + this.nameE2, JOptionPane.PLAIN_MESSAGE);
                    break;
                }






                if (jugada != null && jugada.cantidadFichasDelTurno() != 0) {
                    int cant1 = jugada.cantidadTotalPiezas();

                    time1 = System.nanoTime() / 1000.0;
                    jugada = jugador2.jugar(jugada);
                    time2 = System.nanoTime() / 1000.0;
                    timeJugador2 = time2 - time1;
                    nodosJugador2 = nodosJugador2 + jugador2.numNodos;
                    jugoJugador2++;
                    jugador2.numNodos=0;
                    if (jugada == null) {
                        System.out.println(" 3 Ha ganado el Jugador con fichas verdes");
                        JOptionPane.showMessageDialog(this.ITablero,
                                this.nameE1 + " ha ganado, " + this.nameE2 + " ya no puede moverse!",
                                "Fin del Juego-" + this.nameE1 + "Vs" + this.nameE2, JOptionPane.PLAIN_MESSAGE);
                        break;
                    } else {

                        int cant2 = jugada.cantidadTotalPiezas();
                        int ctotal = cant1 - cant2;
                        //actualizar el tablero original...
                        jugada.coronarPeon();

                        //Desde aca muestra se hace para mostrar en la consola
//                        if (jugada.getTurnoBlancas()) {
//                            System.out.println("Jugó blancas");
//                        } else {
//                            System.out.println("Jugó negras");
//                        }
//                        System.out.println(jugada.toString());

                        //Control de Consistencia...
//                        if (ctotal < 0) {
//                            System.out.println("Algo anda mal... Aparecieron nuevas piezas");
//                        } else if (ctotal != jugada.VectorDeFichasComidas.size()) {
//                            System.out.println("Desaparecieron fichas");
//                        }
                        //Desde aca vamos a usar la interfaz grafica...

                        //Hacemos el movimiento dependiendo si se comieron piezas o si solo se movio una.
                        if (jugada.VectorDeFichasComidas.size() != 0) {
                            //Ubicamos a la pieza en su nueva posicion luego de comer piezas...
                            Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                            Posicion pfinal = jugada.fichaMovida.getPos();
                            long t = System.currentTimeMillis() + 5000 / this.VELOCIDAD;
                            while (t > System.currentTimeMillis());
                            this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                            this.ITablero.cambiarTurno();

                            //Esperamos 2 segundo para borrar las piezas comidas...


                            for (int i = 0; i < jugada.VectorDeFichasComidas.size(); i++) {
                                t = System.currentTimeMillis() + 5000 / this.VELOCIDAD;
                                while (t > System.currentTimeMillis());
                                Ficha comida = (Ficha) jugada.VectorDeFichasComidas.get(i);
                                this.ITablero.comerPieza(comida.getPos().fila, comida.getPos().columna);

                            }

                        } else {
                            //Ubicamos a la pieza en su nueva posicion...
                            Posicion pinicial = jugada.fichaMovida.getPosAnterior();
                            Posicion pfinal = jugada.fichaMovida.getPos();
                            long t = System.currentTimeMillis() + 5000 / this.VELOCIDAD;
                            while (t > System.currentTimeMillis());
                            this.ITablero.moverPieza(pinicial.fila, pinicial.columna, pfinal.fila, pfinal.columna);
                            this.ITablero.cambiarTurno();
                        }
                        jugada.limpiarEstado();//Limpio el estado de la jugada, para pasarle al adversario..
                        jugada.setTurnoBlancas(true);
                    }
                } else {
                    System.out.println(" 4 Ha ganado el Jugador con fichas verdes");
                    JOptionPane.showMessageDialog(this.ITablero,
                            this.nameE1 + " ha ganado, " + this.nameE2 + " ya no tiene piezas!",
                            "Fin del Juego" + this.nameE1 + "Vs" + this.nameE2, JOptionPane.PLAIN_MESSAGE);
                    break;
                }
            }
            if(timeOver)
                JOptionPane.showMessageDialog(this.ITablero,
                    "El juego ha terminado en EMPATE!!",
                    "Tiempo concluido...", JOptionPane.PLAIN_MESSAGE);
            
            String cadena="********Estadisticas de la estrategia "+this.nameE1+ "********\n"+"El tiempo promedio que demora en retornar una jugada = " + Double.toString(timeJugador1 / jugoJugador1) + " milisegundos\n"+"La cantidad de estados promedio que visita la búsqueda = " + Double.toString(nodosJugador1 / jugoJugador1) + " estados\n\n"+"********Estadisticas de la estrategia "+this.nameE2+"********\n"+"El tiempo promedio que demora en retornar una jugada = " + Double.toString(timeJugador2 / jugoJugador2) + " milisegundos\n"+"La cantidad de estados promedio que visita la búsqueda = " + Double.toString(nodosJugador2 / jugoJugador2) + " estados\n";
             JOptionPane.showMessageDialog(this.ITablero,
                    cadena,
                    "Estadistica de la Partida", JOptionPane.PLAIN_MESSAGE);
            System.out.println("------------------------------------------------------");
            System.out.println("********Estadisticas de la estrategia "+this.nameE1+ "(Verde)********\n");
            System.out.println("El tiempo promedio que demora en retornar una jugada = " + timeJugador1 / jugoJugador1 + " milisegundos\n");
            System.out.println("La cantidad de estados promedio que visita la búsqueda = " + nodosJugador1 / jugoJugador1 + " estados\n");
            System.out.println("\n");
            System.out.println("********Estadisticas de la estrategia "+this.nameE2+ "(Azul) ********\n");
            System.out.println("El tiempo promedio que demora en retornar una jugada = " + timeJugador2 / jugoJugador2 + " milisegundos\n");
            System.out.println("La cantidad de estados promedio que visita la búsqueda = " + nodosJugador2 / jugoJugador2 + " estados\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.ITablero,
                    e.getMessage(),
                    "Error", JOptionPane.PLAIN_MESSAGE);
        }

    }

    void inicializar(JDesktopPane mdiForm) {

        this.ITablero.muestra(mdiForm);

    }
}