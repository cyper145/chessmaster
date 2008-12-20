
#include "Archivo.h"
using namespace std;

Archivo::~Archivo()
{
    
}
Archivo::Archivo(char* path)
{
    Archivo::path=path;
}
/**
 *Reorna 0 si el archivo no pudo abrirse, y retorna 1 si el archivo fue abierto con exito.
 */
int Archivo::leerArchivo() {
    string linea;
    ifstream archivo(Archivo::path);
    const char* aux;
    char* cp;
    char* token;
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
                }else if(strcmp(token,"VecMiu:")==0)
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
    } else
    {
        cout << "Upss!!! ERROR: No se pudo abrir el archivo."<<endl;
        return 0;
    }

    return 1;
}

void Archivo::escribirArchivo(char* nombreArchivo,vector<double> resultado)
{

    ofstream archivo(nombreArchivo,ios_base::app);

    if(archivo.is_open())
    {
        for (int i = 0; i < resultado.size(); i++) {
            archivo<<resultado[i]<<"  ";
        }

        archivo<<"\n";
        archivo.close();
    }else{
        cout << "Upss!!! ERROR: No se pudo abrir el archivo."<<endl;
    }


}

