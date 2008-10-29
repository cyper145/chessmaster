package DamasUNA;

import java.util.Vector;



public class AlfaBeta extends IEstrategia {
    
    
    private int nivelMaximo;
    public AlfaBeta (int nivelMax) {
        this.nivelMaximo=nivelMax;
    }

    /**
     * El turno que trae este estado es el turno a quien le toca jugar. 
     * @param actual Es el estado donde jugo nuestro adversario.
     * @return
     */
    public Tablero jugar (Tablero actual) 
    {
        this.numNodos=0;
        Tablero t=null;
        double v=this.ValorMax(actual);
        
        //System.out.println("El mejor puntaje es..."+v);
        Vector sucesores=actual.getSucesores(v);//Trae los sucesores que tiene un puntaje "v"
        if(sucesores.size()!=0)
        {
            int rd=(int)(Math.random()*sucesores.size());
            t=(Tablero)sucesores.get(rd);
           
        }
        
        return t;
    }

    public  int getNumNodos() {
        int n=numNodos;
        return n;
    }

    
    private double ValorMin (Tablero estado) 
    {
        this.numNodos++;
        if(this.testTerminal(estado))
        {
            estado.CambiarTurno();
            estado.CalcularValorDeEstado();
            
            return estado.puntaje;
        }
        double valor= Double.POSITIVE_INFINITY;
        estado.GenerarSucesores();//Genero los sucesores de estado.
        Vector sucesores= estado.getSucesores();
        for (int i = 0; i < sucesores.size(); i++) {
            Tablero s= (Tablero)sucesores.get(i);
            s.setTurnoBlancas(!estado.getTurnoBlancas());
            valor= Math.min(valor, this.ValorMax(s));
            
            //System.out.println("El puntaje es..."+s.puntaje+"y el nivel es..."+s.getNivel());
            //System.out.println(s.toString());
            
            if(valor < estado.alfa || valor == estado.alfa )
            {
               
                return valor;
            }
            estado.beta=Math.min(estado.beta, valor);
        }
        estado.puntaje=valor;
        return valor;
        
    }


    private double ValorMax (Tablero estado) 
    {
        this.numNodos++;
        if(this.testTerminal(estado))
        {
            //Cambiamos de turno, por que en el nivelLimite sus hijos tienen su turno opuesto.
            // Y el costo se halla siempre en el nivelLimite+1. 
            estado.CambiarTurno();
            estado.CalcularValorDeEstado();
            return estado.puntaje;
        }
        double valor= Double.NEGATIVE_INFINITY;
        estado.GenerarSucesores();
        Vector sucesores= estado.getSucesores();
        for (int i = 0; i < sucesores.size(); i++) {
            Tablero s= (Tablero)sucesores.get(i);
            s.setTurnoBlancas(!estado.getTurnoBlancas());
            valor= Math.max(valor, this.ValorMin(s));
            
            //System.out.println("El puntaje es..."+s.puntaje+"y el nivel es..."+s.getNivel());
            //System.out.println(s.toString());
            
            if(valor > estado.beta || valor == estado.beta )
            {
                return valor;
            }
            estado.alfa=Math.max(estado.alfa, valor);
        }
        estado.puntaje=valor;
        return valor;
    }
    
    private boolean testTerminal(Tablero estado)
    {
        return(estado.getNivel()==this.nivelMaximo);
    }
    

}

