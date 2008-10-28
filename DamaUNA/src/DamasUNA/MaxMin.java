package DamasUNA;

import java.util.Vector;



public class MaxMin extends IEstrategia {
    private int nivelMaximo;
    
    public MaxMin (int nivelMax) {
         this.nivelMaximo=nivelMax;
    }

     public Tablero jugar (Tablero actual) 
    {
        this.numNodos=0;
        Tablero t=null;
        double v=this.ValorMax(actual);
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
            //Cambiamos de turno, por que en el nivelLimite sus hijos tienen su turno opuesto.
            // Y el costo se halla siempre en el nivelLimite+1. 
            estado.CambiarTurno();
            estado.CalcularValorDeEstado();
            return estado.puntaje;
        }
        double valor= Double.POSITIVE_INFINITY;
        estado.GenerarSucesores();
        Vector sucesores= estado.getSucesores();
        for (int i = 0; i < sucesores.size(); i++) {
            Tablero s= (Tablero)sucesores.get(i);
            s.setTurnoBlancas(!estado.getTurnoBlancas());
            valor= Math.min(valor, this.ValorMax(s));
            
        }
        estado.puntaje=valor;
        return valor;
        
    }


    private double ValorMax (Tablero estado) 
    {
        this.numNodos++;
        if(this.testTerminal(estado))
        {
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
            
        }
        estado.puntaje=valor;
        return valor;
    }
    
    private boolean testTerminal(Tablero estado)
    {
        return(estado.getNivel()==this.nivelMaximo);
    }
    

}

