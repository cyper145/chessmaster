package chessmaster;

import java.util.Vector;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.C466D43B-9B69-36C2-E561-E0EE4521A0DE]
// </editor-fold> 
public class Aleatorio extends IEstrategia {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.48E95DAD-337F-F0D4-6169-B61D5B94B905]
    // </editor-fold> 
    public Aleatorio () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.792A9D4B-159E-CDB8-CC84-FC273206ECDF]
    // </editor-fold> 
    public Tablero jugar (Tablero actual) {
        int valor = 0;
        actual.GenerarSucesores();
        Vector sucesores = actual.getSucesores();
        if(sucesores != null && sucesores.size()!= 0){
              valor = (int)(Math.random()*sucesores.size());
              return (Tablero)sucesores.get(valor);
        }
        
        return null;
    }

}

