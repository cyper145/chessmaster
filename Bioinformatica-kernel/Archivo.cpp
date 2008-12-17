
#include "Archivo.h"
using namespace std;

Archivo::~Archivo()
{
    
}
Archivo::Archivo(char* path)
{
    Archivo::path=path;
}

void Archivo::leerArchivo() {
    string linea;
    ifstream archivo(Archivo::path);
    const char* aux;
    char* cp;
    char* token;
    vector<double> vec;
    vector<char> alfabeto;
    string cadena;
    double dato;
    //Inicializamos las variables a utilizar, que almacenara las secuencias a estudiar.
    

    if (archivo.is_open()) {
        //Mientras que no sea fin de archivo
        while (!archivo.eof()) {
            getline(archivo, linea);
            if (linea.size() > 0) 
            {
                cp=new char[linea.size()];
                aux=linea.c_str();
                for (int i = 0; i < linea.size(); i++) {
                    cp[i]=aux[i];
                }
                
                token=strtok(cp," ");
                if(strcmp(token,"VecLamda:")==0)
                {
                    for ( token = strtok(NULL, " "); token != NULL;token = strtok(NULL, " ") )
                    {
                        dato=atof(token);
                        Archivo::lamda.push_back(dato);
                    }
                }
                if(strcmp(token,"VecMiu:")==0)
                {
                    for ( token = strtok(NULL, " "); token != NULL;token = strtok(NULL, " ") )
                    {
                        dato=atof(token);
                        Archivo::miu.push_back(dato);
                    }
                }else if(strcmp(token,"Cadena:")==0)
                {
                    token=strtok(NULL, " ");
                    cadena.append(token);
                    Archivo::cadenas.push_back(cadena);
                    cadena.clear();
                }else if(strcmp(token,"Alfabeto:")==0)
                {
                    for ( token = strtok(NULL, " "); token != NULL;token = strtok(NULL, " ") )
                    {
                        char c=token[0];
                        Archivo::alfabeto.push_back(c);
                    }
                }else if(strcmp(token,"Matriz:")==0)
                {
                    vector<double> vectito;
                    for (token = strtok(NULL, " ") ; token != NULL;token = strtok(NULL, " ") )
                    {
                        dato=atof(token);
                        vectito.push_back(dato);
                    }
                    Archivo::matriz.push_back(vectito);
                    vectito.clear();
                }
                
                
            }
        }
        archivo.close();
    } else cout << "No se pudo abrir el archivo.";
    
}

