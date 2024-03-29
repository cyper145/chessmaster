package chessmaster;

import java.util.Vector;



public class AlfaBeta extends IEstrategia {
    
    
    private int nivelMaximo;
    public AlfaBeta (int nivelMax) {
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

