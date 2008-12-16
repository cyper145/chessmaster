/* 
 * File:   Main.cpp
 * Author: Ramon
 *
 * Created on 16 de noviembre de 2008, 14:47
 */
// Leer un archivo de texto

#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;
/*
 * 
 */
double** crearMatriz(int cantFila, int cantColumna);
void destruirMatriz(double** mat);
double* calcularPorGWSK(string charS, string charT, int n, int m, int p, double lambda);
double* calcularPorWNG(string charS, string charT, int n, int m, int p, double lambda);
vector<string> leerArchivo(char* path);

int main(int argc, char** argv) {
        
    //Longitud de maximas secuencias a buscar, valor de p>2.
    int p = 6;

    //Lambda es el peso para realizar la ponderacion, entre 0 y 1.
    double lambda = 0.5;

    //Vector que contendra los resultados obtenidos luego de cada calculo.
    double* resultado;
    double* resultado2;
    vector<string> secuencias;
    //Empezamos la lectura de las secuencias para compararlas.
    secuencias = leerArchivo("C:\\Users\\Usuario\\Desktop\\Ing. Inf. 2008\\Bioinformatica\\Secuencias de Prueba\\secuencias20.txt");
    cout << "Cantidad de Secuencias leidas = " << secuencias.size() << endl;
    
    cout << "********************************************************" << endl;
    cout << "*Resultados utilizando Gap-Weighted Subsequences Kernel*" << endl;
    cout << "********************************************************" << endl;

    for (int j = 0; j < secuencias.size(); j=j+2) {        
        //Realizamos el calculo del Kernel para ambas cadenas.        
        resultado = calcularPorGWSK(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);
        cout << "Cadena S = " << secuencias[j] << endl;
        cout << "Cadena T = " << secuencias[j+1] << endl;
        for (int i = 1; i < p; i++) {
            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado[i] << endl;
        }
    }
    
    cout << "***************************************************" << endl;
    cout << "*Resultados utilizando Weighting by Number of Gaps*" << endl;
    cout << "***************************************************" << endl;
    for (int j = 0; j < secuencias.size(); j=j+2) {
        //Realizamos el calculo del Kernel para ambas cadenas.
        resultado2 = calcularPorWNG(secuencias[j], secuencias[j+1], secuencias[j].size()-1, secuencias[j+1].size()-1, p, lambda);
        cout << "Cadena S = " << secuencias[j] << endl;
        cout << "Cadena T = " << secuencias[j+1] << endl;
        for (int i = 1; i < p; i++) {
            cout << "El valor del kernel para subsecuencias de tamanho " << (i + 1) << " es: " << resultado[i] << endl;
        }
    }
}

double* calcularPorGWSK(string charS, string charT, int n, int m, int p, double lambda) {
    //Inicializamos las variables a utilizar.
    double** DP;
    double** DPS;
    double* Kern;

    //Creamos las matrices de forma dinamica y el vector tambien.
    //Creamos matriz DP
    DP = (double **) calloc(n + 1, sizeof (double *));
    for (int i = 0; i < n + 1; i++)
        DP[i] = (double *) calloc(m + 1, sizeof (double));

    //Creamos matriz DPS
    DPS = (double **) calloc(n, sizeof (double *));
    for (int i = 0; i < n; i++)
        DPS[i] = (double *) calloc(m, sizeof (double));

    //Creamos el vector.
    Kern = new double[p];

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (charS[i] == charT[j]) {
                DPS[i][j] = lambda*lambda;
            }
        }
    }

    for (int l = 1; l < p; l++) {
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                if (i != n && j != m){
                    DP[i][j] = DPS[i - 1][j - 1] + lambda * DP[i - 1][j] + lambda * DP[i][j - 1] - lambda * lambda * DP[i - 1][j - 1];
                }
                if (charS[i - 1] == charT[j - 1]) {
                    DPS[i - 1][j - 1] = lambda * lambda * DP[i - 1][j - 1];
                    Kern[l] = Kern[l] + DPS[i - 1][j - 1];
                }
            }
        }
    }
    //Liberamos nuestra memoria..
    for (int i = 0; i < n + 1; i++) {
        free(DP[i]);
    }
    free(DP);

    for (int i = 0; i < n; i++) {
        free(DPS[i]);
    }
    free(DPS);

    return Kern;
}

double* calcularPorWNG(string charS, string charT, int n, int m, int p, double lambda) {
    //Inicializamos las variables a utilizar.
    double** DP;
    double** DPS;
    double* Kern;

    //Creamos las matrices de forma dinamica y el vector tambien.
    //Creamos matriz DP con n+1 filas y m+1 columnas
    DP = (double **) calloc(n + 1, sizeof (double *));
    for (int i = 0; i < n + 1; i++)
        DP[i] = (double *) calloc(m + 1, sizeof (double));

    //Creamos matriz DPS con n filas y m columnas.
    DPS = (double **) calloc(n, sizeof (double *));
    for (int i = 0; i < n; i++)
        DPS[i] = (double *) calloc(m, sizeof (double));

    //Creamos el vector.
    Kern = new double[p];

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (charS[i] == charT[j]) {
                DPS[i][j] = 1;
            }
        }
    }

    for (int l = 1; l < p; l++) {
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                if (i != n && j != m)
                    DP[i][j] = DPS[i - 1][j - 1] + 1 * DP[i - 1][j] + 1 * DP[i][j - 1] - 1 * 1 * DP[i - 1][j - 1];
                if (charS[i - 1] == charT[j - 1] && (i >= 2) && (j >= 2)) {
                    DPS[i - 1][j - 1] = DP[i - 1][j - 1] + (lambda - 1)*(DP[i - 2][j - 1] + DP[i - 1][j - 2] + (lambda - 1)*(DP[i - 2][j - 2]));
                    Kern[l] = Kern[l] + DPS[i - 1][j - 1];
                }
            }
        }
    }
    //Liberamos nuestra memoria..
    for (int i = 0; i < n + 1; i++) {
        free(DP[i]);
    }
    free(DP);

    for (int i = 0; i < n; i++) {
        free(DPS[i]);
    }
    free(DPS);

    return Kern;
}

vector<string> leerArchivo(char* path) {
    string linea;
    ifstream archivo(path);
    //Inicializamos las variables a utilizar, que almacenara las secuencias a estudiar.
    vector<string> cadenas;

    //Creamos el vector que contendra las secuencias, le asignamos memoria.
    cadenas.clear();

    if (archivo.is_open()) {
        //Mientras que no sea fin de archivo
        while (!archivo.eof()) {
            getline(archivo, linea);
            if (linea.size() > 0) {
                cadenas.push_back(linea);
            }
        }
        archivo.close();
    } else cout << "No se pudo abrir el archivo.";
    return cadenas;
}