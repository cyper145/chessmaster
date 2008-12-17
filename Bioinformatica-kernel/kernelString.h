/* 
 * File:   kernelString.h
 * Author: Ramon
 *
 * Created on 17 de diciembre de 2008, 17:12
 */

#ifndef _KERNELSTRING_H
#define	_KERNELSTRING_H

#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

class kernelString {

public:
       
    //Constructor
    kernelString();
    //Destructor
    ~kernelString();
    
    //Metodos
    double* calcularPorGWSK(string charS, string charT, int n, int m, int p, double lambda);
    double* calcularPorWNG(string charS, string charT, int n, int m, int p, double lambda);
    
          
};

#endif	/* _KERNELSTRING_H */

