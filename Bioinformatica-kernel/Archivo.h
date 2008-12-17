/* 
 * File:   Archivo.h
 * Author: Ramon
 *
 * Created on 17 de diciembre de 2008, 15:39
 */

#ifndef _ARCHIVO_H
#define	_ARCHIVO_H

#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

class Archivo {

public:
    char* path;
    vector<vector<double> > matriz;
    vector<double> vec;
    vector<char> alfabeto;
    vector<string> cadenas;
    
    //Constructor
    Archivo(char* path);
    //Destructor
    ~Archivo();
    
    //Metodos
    void leerArchivo();
    
    
          
};


#endif	/* _ARCHIVO_H */
