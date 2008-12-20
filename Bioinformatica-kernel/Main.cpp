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
void imprimirResultado(Archivo arch, char metodo, double lambda,int p);
/*
 *
 */
int main(int argc, char** argv) {

    

    //Longitud de maximas secuencias a buscar, valor de p>2.
    int p = 0;

    //Lambda es el peso para realizar la ponderacion, entre 0 y 1.
    double lambda = 0;

    char* metodo;
    
    if(argc!=2)
    {
        cout<<"Argumento No correcto"<<endl;
        cout<<"bioinformatica-kernel <nombre_del_archivo>"<<endl;
    }else
    {
        Archivo arch(argv[1]);


        if (arch.leerArchivo() > 0)
        {
            cout << "********************************************************" << endl;
            cout << "*           Gap-Weighted Subsequences Kernel           *" << endl;
            cout << "********************************************************" << endl;
            cout << endl;

            cout << "A- Version Original - Gap-Weighted Subsequences Kernel  " << endl;
            cout << endl;
            cout << "B- Primera variante - Character Weightings String Kernel" << endl;
            cout << endl;
            cout << "C- Version Original - Soft Matching String Kernel       " << endl;
            cout << endl;
            cout << "D- Version Original - Weighting by Number of Gaps       " << endl;
            cout << endl;

            int b=1;
            do
            {
                char* aux;
                cout << "Ingrese la opcion: ";
                cin >> aux;
                metodo = strupr(aux);
                if(strlen(metodo)!=1)
                {
                    b=0;
                }else if(metodo[0] != 'A' && metodo[0] != 'B' && metodo[0] != 'C' && metodo[0] != 'D')
                {
                    b=0;
                }

            } while (b==0);

            do {
                cout << "Ingrese el valor para lambda (0,1): ";
                cin >> lambda;
            } while (lambda <= 0 || lambda >= 1);

            do {
                cout << "Ingrese el valor para la longitud de la subsecuencia - p ( p>=2 ): ";
                cin >> p;
            } while (p < 2);

            imprimirResultado(arch, metodo[0], lambda, p);
            
        }
        
        getchar();
    }
    
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

    //Sacamos los datos del archivo.
    secuencias=arch.cadenas;
    vector<double> vecLambda=arch.lamda;
    vector<double> vecMiu=arch.miu;
    vector<char> vecAlfabeto=arch.alfabeto;
    vector< vector<double> > matriz=arch.matriz;

    vector<double> fila;
    
    cout << endl;
    if(metodo=='A'|| metodo=='a')
    {
        cout << "********************************************************" << endl;
        cout << "*Resultados utilizando Gap-Weighted Subsequences Kernel*" << endl;
        cout << "********************************************************" << endl;
        cout<<endl;
        cout << "Cantidad de Secuencias leidas = " << secuencias.size() << endl;
        for (int j = 0; j < secuencias.size(); j++)
        {
            fila.clear();
            for (int i = 0; i < secuencias.size(); i++) {
            //Realizamos el calculo del Kernel para ambas cadenas.
                cout<<"i= "<<i<<" j= "<<j;
                resultado = kernel.calcularPorGWSK(secuencias[j], secuencias[i], secuencias[j].size()-1, secuencias[i].size()-1, p, lambda);
                cout << "Cadena S = " << secuencias[j] <<endl;
                cout << "Cadena T = " << secuencias[i] << endl;
                cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p-1] << endl;
                cout << endl;
                fila.push_back(resultado[p-1]);
                
            }
            arch.escribirArchivo("ResultGWSK.txt",fila);
            
        }
    }else if (metodo == 'B' || metodo == 'b')
    {
        cout << "**********************************************************" << endl;
        cout << "*Resultados utilizando Character Weightings String Kernel*" << endl;
        cout << "**********************************************************" << endl;
        cout << endl;
        cout << "Cantidad de Secuencias leidas = " << secuencias.size() << endl;
        if (!vecLambda.empty() && !vecMiu.empty() && !vecAlfabeto.empty()) {
            for (int j = 0; j < secuencias.size(); j++) {
                fila.clear();
                for (int i = 0; i < secuencias.size(); i++) {
                    //Realizamos el calculo del Kernel para ambas cadenas.
                    cout << "i= " << i << " j= " << j;
                    resultado = kernel.calcularPorCWSK(secuencias[j], secuencias[i], secuencias[j].size() - 1, secuencias[i].size() - 1, p, lambda,vecLambda,vecMiu,vecAlfabeto);
                    cout << "Cadena S = " << secuencias[j] << endl;
                    cout << "Cadena T = " << secuencias[i] << endl;
                    cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p - 1] << endl;
                    cout << endl;
                    fila.push_back(resultado[p - 1]);

                }
                arch.escribirArchivo("ResultCWSK.txt", fila);

            }

        } else {
            cout << "Falta alguno de los vectores requeridos (vecLamda,vecMiu,Alfabeto)" << endl;
        }

    }else if(metodo=='C'|| metodo=='c')
    {
        cout << "***************************************************" << endl;
        cout << "*Resultados utilizando Soft Matching String Kernel*" << endl;
        cout << "***************************************************" << endl;
        cout << endl;
        cout << "Cantidad de Secuencias leidas = " << secuencias.size() << endl;
        if (!matriz.empty() && !vecAlfabeto.empty()) {
            for (int j = 0; j < secuencias.size(); j++) {
                fila.clear();
                for (int i = 0; i < secuencias.size(); i++) {
                    //Realizamos el calculo del Kernel para ambas cadenas.
                    cout << "i= " << i << " j= " << j;
                    resultado = kernel.calcularPorSMSK(secuencias[j], secuencias[i], secuencias[j].size() - 1, secuencias[i].size() - 1, p, lambda,matriz,vecAlfabeto);
                    cout << "Cadena S = " << secuencias[j] << endl;
                    cout << "Cadena T = " << secuencias[i] << endl;
                    cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p - 1] << endl;
                    cout << endl;
                    fila.push_back(resultado[p - 1]);

                }
                arch.escribirArchivo("ResultSMSK.txt", fila);

            }
        } else {
            cout << "Falta el Alfabeto o la Matriz requerida." << endl;
        }
    }else if(metodo=='D'|| metodo=='d')
    {
        cout << "***************************************************" << endl;
        cout << "*Resultados utilizando Weighting by Number of Gaps*" << endl;
        cout << "***************************************************" << endl;
        cout<<endl;
        cout << "Cantidad de Secuencias leidas = " << secuencias.size() << endl;
        for (int j = 0; j < secuencias.size(); j++)
        {
            fila.clear();
            for (int i = 0; i < secuencias.size(); i++) {
            //Realizamos el calculo del Kernel para ambas cadenas.
                cout<<"i= "<<i<<" j= "<<j;
                resultado = kernel.calcularPorWNG(secuencias[j], secuencias[i], secuencias[j].size()-1, secuencias[i].size()-1, p, lambda);
                cout << "Cadena S = " << secuencias[j] <<endl;
                cout << "Cadena T = " << secuencias[i] << endl;
                cout << "El valor del kernel para subsecuencias de tamanho " << p << " es: " << resultado[p-1] << endl;
                cout << endl;
                fila.push_back(resultado[p-1]);

            }
            arch.escribirArchivo("ResultWNG.txt",fila);

        }
    }

}
