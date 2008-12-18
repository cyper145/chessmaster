/* 
 * File:   Main.cpp
 * Author: Ramon
 *
 * Created on 17 de diciembre de 2008, 17:14
 */

#include <stdlib.h>
#include "kernelString.h"
#include "Archivo.h"

/*
 * 
 */
int main(int argc, char** argv) {
        
    //Longitud de maximas secuencias a buscar, valor de p>2.
    int p = 6;

    //Lambda es el peso para realizar la ponderacion, entre 0 y 1.
    double lambda = 0.5;

    //Vector que contendra los resultados obtenidos luego de cada calculo.
    double* resultado;
    double* resultado2;
    double* resultado3;
    vector<string> secuencias;
    //Empezamos la lectura de las secuencias para compararlas.
    kernelString kernel;
    Archivo arch("C:\\Users\\Usuario\\Desktop\\Ing. Inf. 2008\\Bioinformatica\\Secuencias de Prueba\\secuencias20.txt");
    arch.leerArchivo();
    secuencias=arch.cadenas;
    
    cout << "Cantidad de Secuencias leidas = " << secuencias.size() << endl;
    cout << " " << endl;
    cout << "********************************************************" << endl;
    cout << "*Resultados utilizando Gap-Weighted Subsequences Kernel*" << endl;
    cout << "********************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {        
          //Realizamos el calculo del Kernel para ambas cadenas.        
        resultado = kernel.calcularPorGWSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);
        cout << "Cadena S = " << secuencias[j] << endl;
        cout << "Cadena T = " << secuencias[j+1] << endl;
        for (int i = 1; i < p; i++) {
            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado[i] << endl;
        }        
    }
    cout << " " << endl;
    cout << "***************************************************" << endl;
    cout << "*Resultados utilizando Weighting by Number of Gaps*" << endl;
    cout << "***************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {
        //Realizamos el calculo del Kernel para ambas cadenas.
        resultado2 = kernel.calcularPorWNG(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);
       
        cout << "Cadena S = " << secuencias[j] << endl;
        cout << "Cadena T = " << secuencias[j+1] << endl;
        for (int i = 1; i < p; i++) {
            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado2[i] << endl;
        }
    }
    cout << " " << endl;
    cout << "**********************************************************" << endl;
    cout << "*Resultados utilizando Character Weightings String Kernel*" << endl;
    cout << "**********************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {
        //Realizamos el calculo del Kernel para ambas cadenas.
        resultado3 = kernel.calcularPorCWSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda, arch.lamda, arch.miu, arch.alfabeto);

        cout << "Cadena S = " << secuencias[j] << endl;
        cout << "Cadena T = " << secuencias[j+1] << endl;
        for (int i = 1; i < p; i++) {
            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado3[i] << endl;
        }
    }

}

