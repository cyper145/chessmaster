/*
 * File:   Main.cpp
 * Author: Ramon
 *
 * Created on 17 de diciembre de 2008, 17:14
 */

#include <stdlib.h>
#include "kernelString.h"
#include "Archivo.h"
#include <windows.h>

double performancecounter_diff(LARGE_INTEGER *a, LARGE_INTEGER *b);
/*
 *
 */
int main(int argc, char** argv) {

    LARGE_INTEGER t_ini, t_fin;
    double dif;
    double suma = 0;

    //Longitud de maximas secuencias a buscar, valor de p>2.
    int p = 6;

    //Lambda es el peso para realizar la ponderacion, entre 0 y 1.
    double lambda = 0.5;

    //Vector que contendra los resultados obtenidos luego de cada calculo.
    double* resultado;
    double* resultado2;
    double* resultado3;
    double* resultado4;
    vector<string> secuencias;
    //Empezamos la lectura de las secuencias para compararlas.
    
    Archivo arch("secuencias20.txt");
    arch.leerArchivo();
    secuencias=arch.cadenas;

    cout << "Cantidad de Secuencias leidas = " << secuencias.size() << endl;
    cout << " " << endl;
    cout << "********************************************************" << endl;
    cout << "*Resultados utilizando Gap-Weighted Subsequences Kernel*" << endl;
    cout << "********************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {
          //Realizamos el calculo del Kernel para ambas cadenas.
         dif=0;
        QueryPerformanceCounter(&t_ini);

        resultado = kernel.calcularPorGWSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);

        QueryPerformanceCounter(&t_fin);
        dif = performancecounter_diff(&t_fin, &t_ini);
        suma = suma + dif;
        cout<<endl<<"El tiempo es="<<dif<<" segundos"<<endl;

        cout << "Cadena S = " << secuencias[j] << endl;
        cout << "Cadena T = " << secuencias[j+1] << endl;
        for (int i = 1; i < p; i++) {
            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado[i] << endl;
        }
    }
    cout<<endl<<"El tiempo promedio es="<<(suma/10)<<" segundos"<<endl;
    suma = 0;
    cout << " " << endl;
    cout << "***************************************************" << endl;
    cout << "*Resultados utilizando Weighting by Number of Gaps*" << endl;
    cout << "***************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {
        //Realizamos el calculo del Kernel para ambas cadenas.
        dif=0;
        QueryPerformanceCounter(&t_ini);

        resultado2 = kernel.calcularPorWNG(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);

        QueryPerformanceCounter(&t_fin);
        dif = performancecounter_diff(&t_fin, &t_ini);
        suma = suma + dif;
        cout<<endl<<"El tiempo es="<<dif<<" segundos"<<endl;

//        cout << "Cadena S = " << secuencias[j] << endl;
//        cout << "Cadena T = " << secuencias[j+1] << endl;
//        for (int i = 1; i < p; i++) {
//            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado2[i] << endl;
//        }
    }
    cout<<endl<<"El tiempo promedio es="<<(suma/10)<<" segundos"<<endl;
    suma = 0;
    cout << " " << endl;
    cout << "**********************************************************" << endl;
    cout << "*Resultados utilizando Character Weightings String Kernel*" << endl;
    cout << "**********************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {
        //Realizamos el calculo del Kernel para ambas cadenas.
        dif=0;
        QueryPerformanceCounter(&t_ini);

        resultado3 = kernel.calcularPorCWSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda, arch.lamda, arch.miu, arch.alfabeto);

        QueryPerformanceCounter(&t_fin);
        dif = performancecounter_diff(&t_fin, &t_ini);
        suma = suma + dif;
        cout<<endl<<"El tiempo es="<<dif<<" segundos"<<endl;

//        cout << "Cadena S = " << secuencias[j] << endl;
//        cout << "Cadena T = " << secuencias[j+1] << endl;
//        for (int i = 1; i < p; i++) {
//            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado3[i] << endl;
//        }
    }
    cout<<endl<<"El tiempo promedio es="<<(suma/10)<<" segundos"<<endl;
    suma = 0;
    cout << " " << endl;
    cout << "**********************************************************" << endl;
    cout << "*Resultados utilizando Soft Matching String Kernel*" << endl;
    cout << "**********************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {
        //Realizamos el calculo del Kernel para ambas cadenas.
        dif=0;
        QueryPerformanceCounter(&t_ini);

        resultado4 = kernel.calcularPorSMSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda, arch.matriz, arch.alfabeto);

        QueryPerformanceCounter(&t_fin);

        dif = performancecounter_diff(&t_fin, &t_ini);
        suma = suma + dif;
        cout<<endl<<"El tiempo es="<<dif<<" segundos"<<endl;

//        cout << "Cadena S = " << secuencias[j] <<endl;
//        cout << "Cadena T = " << secuencias[j+1] << endl;
//        for (int i = 1; i < p; i++) {
//            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado4[i] << endl;
//        }
    }
    cout<<endl<<"El tiempo promedio es="<<(suma/10)<<" segundos"<<endl;
    suma = 0;
    getchar();
}

/* retorna "a - b" en segundos */
double performancecounter_diff(LARGE_INTEGER *a, LARGE_INTEGER *b)
{
  LARGE_INTEGER freq;
  QueryPerformanceFrequency(&freq);
  return (double)(a->QuadPart - b->QuadPart) / (double)freq.QuadPart;
}

void imprimirResultado(Archivo arch, char metodo, double lambda,int p)
{
    kernelString kernel;
    double* resultado;
    vector< string > secuencias;
    

    if(metodo=='A')
    {
        cout << "********************************************************" << endl;
        cout << "*Resultados utilizando Gap-Weighted Subsequences Kernel*" << endl;
        cout << "********************************************************" << endl;

        for (int j = 0; j < secuencias.size(); j=j+2)
        {
            //Realizamos el calculo del Kernel para ambas cadenas.
            resultado = kernel.calcularPorGWSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);
            cout << "Cadena S = " << secuencias[j] <<endl;
            cout << "Cadena T = " << secuencias[j+1] << endl;
            cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p-1] << endl;
        }
    }else if(metodo=='B')
    {
        cout << "**********************************************************" << endl;
        cout << "*Resultados utilizando Character Weightings String Kernel*" << endl;
        cout << "**********************************************************" << endl;
        for (int j = 0; j < secuencias.size(); j=j+2)
        {
            //Realizamos el calculo del Kernel para ambas cadenas.
            resultado = kernel.calcularPorCWSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda, arch.lamda, arch.miu, arch.alfabeto);
            cout << "Cadena S = " << secuencias[j] <<endl;
            cout << "Cadena T = " << secuencias[j+1] << endl;
            cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p-1] << endl;
        }
    }else if(metodo=='C')
    {
        cout << "***************************************************" << endl;
        cout << "*Resultados utilizando Soft Matching String Kernel*" << endl;
        cout << "***************************************************" << endl;
        for (int j = 0; j < secuencias.size(); j=j+2)
        {
            //Realizamos el calculo del Kernel para ambas cadenas.
            resultado = kernel.calcularPorSMSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda, arch.matriz, arch.alfabeto);
            cout << "Cadena S = " << secuencias[j] <<endl;
            cout << "Cadena T = " << secuencias[j+1] << endl;
            cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p-1] << endl;
        }
    }else if(metodo=='D')
    {
        cout << "***************************************************" << endl;
        cout << "*Resultados utilizando Weighting by Number of Gaps*" << endl;
        cout << "***************************************************" << endl;
        for (int j = 0; j < secuencias.size(); j=j+2)
        {
            //Realizamos el calculo del Kernel para ambas cadenas.
            resultado = kernel.calcularPorWNG(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);
            cout << "Cadena S = " << secuencias[j] <<endl;
            cout << "Cadena T = " << secuencias[j+1] << endl;
            cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p-1] << endl;
        }
    }

}
