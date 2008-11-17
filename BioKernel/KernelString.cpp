/* 
 * File:   Main.cpp
 * Author: Ramon
 *
 * Created on 16 de noviembre de 2008, 14:47
 */

#include <stdlib.h>
#include <iostream.h>
/*
 * 
 */
double** crearMatriz(int cantFila, int cantColumna);
void destruirMatriz(double** mat);
double* calcularPorGWSK(char* s, char* t, int n, int m, int p, double lambda);
double* calcularPorWNG(char* charS, char* charT, int n, int m, int p, double lambda);

int main(int argc, char** argv) {
    
    //cadenas a comparar
    char *s="DYLIVIGVVSIILALVCYPLWPRSMRRGSYYVSLGAFGILAGFFAVAILRLILYVLSLIVYKDVGGFWIFPNLFEDCGVLESFKPLYGFGEKDTYSYKKKLKRMKKKQAKRESNKKKAINEKAEQN,MMTRESIDKRAGRRGPNLNIVLTCPECKVYPPKIVERFSEGDVVCALCGLVLSDKLVDTRSEWRTFSNDDHNGDDPSRVGEASNPLLDGNNLSTRIGKGETTDMRFTKELNKAQGKNVMDKKDNEVQAAFAKITMLCDAAELPKIVKDCAKEAYKLCHDEKTLKGKSMESIMAASILIGCRRAEVARTFKEIQSLIHVKTKEFGKTLNIMKNILRGKSEDGFLKIDTDNMSGAQNLTYIPRFCSHLGLPMQVTTSAEYTAKKCKEIKEIAGKSPITIAVVSIYLNILLFQIPITAAKVGQTLQVTEGTIKSGYKILYEHRDKLVDPQLIANGVVSLDNLPGVEKK";
    char *t="MEETIENVEVPSSNVSKQNDDGLDMKTLFVRSIPQDVTDEQLADFFSNFAPIKHAVVVKDTNKRSRGFGFVSFAVEDDTKEALAKARKTKFNGHILRVDIAKRRDRSKKTSEVVEKSTPESSEKITGQNNEDEDDADGEDSMLKGKPKLIIRNMPWSCRDPVKLKKIFGRYGTVVEATIPRKRDGKLCGFAFVTMKKISNCRIALENTKDLKIDGRKVAVDFAVQKNRWEDYKKAQPEMNDKDDNESGNEDAEENHDDEEDENEEEDRQVDQASKNKESKRKAQNKREDFSVFVRNVPYDATEESLAPHFSKFGSVKYALPVIDKSTGLAKGTAFVAFKDQYTYNECIKNAPAAGSTSLLIGDDVMPEYVYEGRVLSITPTLVREDAGRMAEKNAAKRKEALGKAPGEKDRRNLYLLNEGRVVEGSKMADLLTNTDMEIREKSYKLRVEQLKKNPSLHLSMTRLAIRNLPRAMNDKALKALARKAVVEFATEVKNKERHPLSKEEIIRSTKEKYKFMGPDEIEAQKKKDKKSGVVKQAKVIMEVKGSTAGRSRGYGFVEFRDHKNALMGLRWLNCHAVTSDEILEGLNDDEKKQVDNDLGKGRRLCVEFAIENSNVVKRRREQLKQARTKRTRPDNEDTGDVGESENKKPKKEEATTPTNPDDKKMGDDIKRIIGFKRKRKHAKK,MSQRKLQQDIDKLLKKVKEGIEDFDDIYEKFQSTDPSNSSHREKLESDLKREIKKLQKHRDQIKTWLSKEDVKDKQSVLMTNRRLIENGMERFKSVEKLMKTKQFSKEALTNPDIIKDPKELKKRDQVLFIHDCLDELQKQLEQYEAQENEEQTERHEFHIANLENILKKLQNNEMDPEPVEEFQDDIKYYVENNDDPDFIEYDTIYEDMGCEIQPSSSNNEAPKEGNNQTSLSSIRSSKKQERSPKKKAPQRDVSISDRATTPIAPGVESASQSISSTPTPVSTDTPLHTVKDDSIKFDNSTLGTPTTHVSMKKKESENDSEQQLNFPPDRTDEIRKTIQHDVETNAAFQNPLFNDELKYWLDSKRYLMQPLQEMSPKMVSQLESSLLNCPDSLDADSPCLYTKPLSLPHPTSIFFPNEPIRFVYPYDVPLNLTNNENDTDNKFGKDSKAKSKKDDDIYSRTSLARIFMKFDLDTLFFIFYHYQGSYEQFLAARELFKNRNWLFNKVDRCWYYKEIEKLPPGMGKSEEESWRYFDYKKSWLARRCGNDFVYNEEDFEKL";
    
   
    //longitudes de las cadenas.
    int longS=strlen(s);
    int longT=strlen(t);
    
    //Longitud de maximas secuencias a buscar, valor de p>2.
    int p=10;
    
    //Lambda es el peso para realizar la ponderacion, entre 0 y 1.
    double lambda = 0.5;
    
    //Vector que contendra los resultados obtenidos luego de cada calculo.
    double* resultado;
    double* resultado2;
    
    resultado=calcularPorGWSK(s, t, longS, longT, p, lambda);
    cout<<"********************************************************"<<endl;
    cout<<"*Resultados utilizando Gap-Weighted Subsequences Kernel*"<<endl;
    cout<<"********************************************************"<<endl;
    
    for (int i = 1; i < p; i++) {
        cout<<"El valor del kernel para subsecuencias de tamanho "<< (i + 1)<<" es: "<<resultado[i]<<endl;
    }
    
    
    resultado2=calcularPorWNG(s, t, longS, longT, p, lambda);
    cout<<"********************************************************"<<endl;
    cout<<"*Resultados utilizando Weighting by Number of Gaps*"<<endl;
    cout<<"********************************************************"<<endl;
    
    for (int i = 1; i < p; i++) {
        cout<<"El valor del kernel para subsecuencias de tamanho "<< (i + 1)<<" es: "<<resultado[i]<<endl;
    }

}



double* calcularPorGWSK(char* charS, char* charT, int n, int m, int p, double lambda)
{
    //Inicializamos las variables a utilizar.
    double** DP;
    double** DPS;
    double* Kern;
    
    //Creamos las matrices de forma dinamica y el vector tambien.
    //Creamos matriz DP
    DP=(double **)calloc(n+1, sizeof(double *));
     for (int i=0; i<n+1; i++)
	DP[i] = (double *)calloc(m+1, sizeof(double));
    
    //Creamos matriz DPS
    DPS=(double **)calloc(n, sizeof(double *));
     for (int i=0; i<n; i++)
	DPS[i] = (double *)calloc(m, sizeof(double));
    
    //Creamos el vector.
    Kern= new double[p];
    
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if(charS[i]==charT[j])
            {
                DPS[i][j]=lambda*lambda;
            }
        }
    }
    
    for (int l = 1; l < p; l++) {
        for (int i = 1; i < n+1; i++) {
            for (int j = 1; j < m+1; j++) {
                if(i!=n && j!= m)
                        DP[i][j] = DPS[i-1][j-1] + lambda*DP[i-1][j] + lambda*DP[i][j-1] - lambda*lambda*DP[i-1][j-1];                                        
                
                if(charS[i-1] == charT[j-1]){
                        DPS[i-1][j-1] = lambda*lambda*DP[i-1][j-1];
                        Kern[l] = Kern[l] + DPS[i-1][j-1];
                }  


            }
        }
    }
    //Liberamos nuestra memoria..
     for (int i=0; i<n+1; i++)
     {
	free(DP[i]);
     }
    free(DP);
    
    for (int i=0; i<n+1; i++)
    {
	free(DPS[i]);
    }
    free(DPS);
    
    return Kern;
}

double* calcularPorWNG(char* charS, char* charT, int n, int m, int p, double lambda)
{
    //Inicializamos las variables a utilizar.
    double** DP;
    double** DPS;
    double* Kern;
    
    //Creamos las matrices de forma dinamica y el vector tambien.
    //Creamos matriz DP con n+1 filas y m+1 columnas
    DP=(double **)calloc(n+1, sizeof(double *));
     for (int i=0; i<n+1; i++)
	DP[i] = (double *)calloc(m+1, sizeof(double));
    
    //Creamos matriz DPS con n filas y m columnas.
    DPS=(double **)calloc(n, sizeof(double *));
     for (int i=0; i<n; i++)
	DPS[i] = (double *)calloc(m, sizeof(double));
    
    //Creamos el vector.
    Kern= new double[p];
    
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if(charS[i]==charT[j])
            {
                DPS[i][j]=1;
            }
        }
    }
    
    for (int l = 1; l < p; l++) {
            for (int i = 1; i < n+1; i++) {
                for (int j = 1; j < m+1; j++) { 
                    if(i!=n && j!= m)
                        DP[i][j] = DPS[i-1][j-1] + 1*DP[i-1][j] + 1*DP[i][j-1] - 1*1*DP[i-1][j-1];                                        
                    if(charS[i-1] == charT[j-1] && (i >= 2) && (j >= 2)){
                        DPS[i-1][j-1] = DP[i-1][j-1] + (lambda-1)*(DP[i-2][j-1] + DP[i-1][j-2] + (lambda-1)*(DP[i-2][j-2]));
                        Kern[l] = Kern[l] + DPS[i-1][j-1];
                    }                    
                }                
            }            
        }
    //Liberamos nuestra memoria..
     for (int i=0; i<n+1; i++)
     {
	free(DP[i]);
     }
    free(DP);
    
    for (int i=0; i<n+1; i++)
    {
	free(DPS[i]);
    }
    free(DPS);
    
    return Kern;
}