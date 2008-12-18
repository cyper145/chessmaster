/* 
 * File:   Main.cpp
 * Author: Ramon
 *
 * Created on 16 de noviembre de 2008, 14:47
 */
// Leer un archivo de texto

#include <stdlib.h>

#include "kernelString.h"
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include "kernelString.h"

using namespace std;
/*
 * 
 */
kernelString::kernelString()
{
    
}
kernelString::~kernelString()
{
    
}
double* kernelString::calcularPorGWSK(string charS, string charT, int n, int m, int p, double lambda) {
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
    for (int i = 0; i < p; i++) {
        Kern[i] = 0;
    }

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

double* kernelString::calcularPorWNG(string charS, string charT, int n, int m, int p, double lambda) {
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
    for (int i = 0; i < p; i++) {
        Kern[i] = 0;
    }

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
/**
 *Si encuentra la letra en el alfabeto, retorna el numero i que representa su posicion en el vector, 
 *sino retorna -1 en caso que no se encuentra. 
 */
int kernelString::traerPos(char letra, vector<char> alfabeto)
{
    for (int i = 0; i < alfabeto.size(); i++) {
        if(alfabeto[i]==letra)
        {
            return i;
        }
    }
    return -1;

}
double* kernelString::calcularPorCWSK(string charS, string charT, int n, int m, int p, double lambda, vector<double> vecLambda, vector<double> vecMiu, vector<char> alfabeto) {
    //Inicializamos las variables a utilizar.
    double** DP;
    double** DPS;
    double* Kern;
    int posicionI = 0;
    int posicionJ = 0;

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
    for (int i = 0; i < p; i++) {
        Kern[i] = 0;
    }

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (charS[i] == charT[j]) {
                posicionI = kernelString::traerPos(charS[i], alfabeto);
                if(posicionI >= 0){
                    DPS[i][j] = vecMiu[posicionI]*vecMiu[posicionI];
                }

            }
        }
    }

    for (int l = 1; l < p; l++) {
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                if (i != n && j != m){
                    posicionI = kernelString::traerPos(charS[i - 1], alfabeto);
                    posicionJ = kernelString::traerPos(charT[j - 1], alfabeto);
                    if(posicionI >= 0 && posicionJ >= 0){
                        DP[i][j] = DPS[i - 1][j - 1] +  vecLambda[posicionI]* DP[i - 1][j] + vecLambda[posicionJ]*( DP[i][j - 1] - vecLambda[posicionI]* DP[i - 1][j - 1]);
                    }
                }
                if (charS[i - 1] == charT[j - 1] && (i >= 2) && (j >= 2)) {
                    posicionI = kernelString::traerPos(charS[i - 1], alfabeto);
                    if(posicionI >= 0){
                        DPS[i - 1][j - 1] = vecMiu[posicionI]*vecMiu[posicionI]*DP[i - 1][j - 1];
                        Kern[l] = Kern[l] + DPS[i - 1][j - 1];
                    }
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