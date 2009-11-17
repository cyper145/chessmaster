/*
 * 
 *
 */
package ASP_NR;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Clase utilizada para el manejo de archivos. Esta clase es utilizada
 * solo si el programa se corre sin ambiente web.
 * @author Manuel Avalos
 * @author Arturo Ferreira
 */
public class Archivo {
    private String path=null;
    private String fileNameIN=null;
    private String fileNameOUT=null;

    private int cant_lineas=0;
    private FileWriter fw_path=null;
    private FileReader fr_path=null;
    private BufferedReader br_path=null;
    private BufferedWriter bw=null;
    PrintWriter escritora=null;
    /**
     *
     * Constructor por defecto de la clase Archivo
     *
     */
    public Archivo(String path) throws IOException {
        this.path=path;

    }

    public Archivo(String path, String nombreIN, String nombreOUT) throws IOException {
        this.path=path;
        this.fileNameIN=nombreIN;
        this.fileNameOUT=nombreOUT;
        this.fr_path = new FileReader(this.path+nombreIN);
        this.br_path = new BufferedReader(this.fr_path);
        this.fw_path = new FileWriter(this.path+nombreOUT);
        
    }

    public void setFileNameIN(String fileNameIN) throws FileNotFoundException, IOException {
        this.fileNameIN = fileNameIN;
        if(this.fr_path !=null)
        {
            this.fr_path.close();
            this.fr_path = new FileReader(this.path+this.fileNameIN);
            if(this.br_path!=null)
            {
                this.br_path.close();
                this.br_path = new BufferedReader(this.fr_path);
            }else
                this.br_path = new BufferedReader(this.fr_path);
        }else
        {
            this.fr_path = new FileReader(this.path+this.fileNameIN);
            if(this.br_path!=null)
            {
                this.br_path.close();
                this.br_path = new BufferedReader(this.fr_path);
            }else
                this.br_path = new BufferedReader(this.fr_path);
        }
            
        
    }

    public void setFileNameOUT(String fileNameOUT) throws IOException {
        this.fileNameOUT = fileNameOUT;
        if(this.fw_path !=null)
        {
            this.fw_path.close();
            this.fw_path = new FileWriter(this.path+this.fileNameOUT);
            this.escritora.close();
            if(this.bw!=null)
            {
                
                
                this.bw = new BufferedWriter(fw_path);
            }else
                this.bw = new BufferedWriter(fw_path);

            this.escritora= new PrintWriter(this.bw,true);
        }else
        {
            this.fw_path = new FileWriter(this.path+this.fileNameOUT);
            if(this.bw!=null)
            {
                
                this.bw = new BufferedWriter(fw_path);
            }else
                this.bw = new BufferedWriter(fw_path);

            this.escritora= new PrintWriter(this.bw,true);
        }
            
    }



    /**
     *
     * Metodo utilizado para la lectura del archivo que contiene los datos.
     * CUIDADO!!!: Todas las lineas del archivo son guardadas en una Collection Vector.
     * @param path Direcion en donde se encuentra el archivo con los datos de entrada.     
     * @throws Exception Se lanza una excepcion si ocurre un error durante la lectura de los
     * datos de entrada.
     *
     */
    public Vector leer() throws Exception {
              
        Vector lineas= new Vector();
        String linea;
        //Se inicia la lecyura de los datos
        try {
        
            //Empieza la lectura de las covarianza. 
            while ((linea = br_path.readLine()) != null) {
                if(!linea.startsWith("#"))
                    lineas.add(linea);
                    
            }
           return lineas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally{
            //Se cierra el archivo.
            if(null!= fr_path){
                try {
                    fr_path.close();
                    br_path.close();
                } catch (IOException ex) {
                    Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    
    public void close()
    {
        if(null!= fw_path){
                try {
                    
                    this.fw_path.flush();
                    this.fw_path.close();
                    this.bw.close();
                    escritora.close();
                } catch (IOException ex) {
                    Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        else if (fr_path!=null)
        {
            try {

                fr_path.close();
                br_path.close();
            } catch (IOException ex) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void escribir (String dato)
    {
       try{
            if(this.fw_path==null ||this.bw==null || this.escritora==null)
            {
                if(this.path ==null)
                    System.out.println("ERROR:Falta especificar la direccion del archivo de escritura.");
                else if(this.bw ==null)
                {
                    System.out.println("ERROR: buffer Writer es null.");
                }else{
                    System.out.println("ERROR: Print Writer  es null.");
                }
            }else
            {
                escritora.println(dato);
            }
                

            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Funcion utilizada para partir un archivo de texto, en varias partes.
     * El archivo se parte utilizando como palabra clave en el contenido del texto,
     * la palabra "COMMIT".
     * @param nombreArchIN Nombre del archivo de donde se leeran los datos.
     * @param nombreArchOUT Nombre del archivo donde se guardaran los datos.
     */
    public void partirArchivo_SQLManager(String nombreArchIN, String nombreArchOUT){
        int cantArchivos=1; // Inicializamos a uno, por que el nombre del primer archivo lo creamos sin usar esta variable.
        FileReader fr_path=null;
        BufferedReader br=null;

        FileWriter fw_path=null;

        BufferedWriter bw=null;
        PrintWriter copiadora=null;

        StringTokenizer tokens;

        String linea;
        //Se inicia la lecyura de los datos
        try {
            fr_path = new FileReader(this.path+nombreArchIN);
            br = new BufferedReader(fr_path);

            //Aca creamos el primer archivo.
            fw_path = new FileWriter(this.path+nombreArchOUT+"-01");
            bw = new BufferedWriter(fw_path);

            copiadora=new PrintWriter(bw);

            while ((linea = br.readLine()) != null) {
//                System.out.println(linea);
                this.cant_lineas++;

                //creamos un archivo nuevo si encontramos en el archivo de origen la palabra commit.
                //Esta palabra nos indica donde debemos cortar el archivo.
                if(linea.contains("COMMIT"))
                {
                   cantArchivos++;
                   copiadora.println(linea);
                   copiadora.flush();
                   copiadora.close();
                   fw_path.close();
                   if(cantArchivos<10)//Esta solo para que el nombre del archivo sea lindo.
                   {
                       fw_path = new FileWriter(path+nombreArchOUT+"-0"+cantArchivos);//c:\inserts_liquidacion\ + insert_liquidaciones_Det + -08
                       bw = new BufferedWriter(fw_path);
                   }
                   else
                   {
                       fw_path = new FileWriter(path+nombreArchOUT+"-"+cantArchivos);
                       bw = new BufferedWriter(fw_path);
                   }
                    System.out.println(this.cant_lineas/3085091*100+"%");// Porcentaje de termino para el archivo FLOTA_REMITO.
                    
                   copiadora= new PrintWriter(bw);
                }else
                {
                   //Escribimos en el archivo, si es que no debemos crear uno nuevo.
                    copiadora.println(linea);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //Se cierra el archivo.
            try {
                if (null != fr_path) {

                    fr_path.close();
                    br.close();
                } else if (null != fw_path) {
                    fw_path.close();
                    bw.close();
                }else if(copiadora!= null)
                {
                    copiadora.close();
                }


            } catch (IOException ex) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Funcion utilizada para partir un archivo de texto, en varias partes.
     * El archivo se parte cada 12000 insert. Hay exactamente 1 insert por cada linea del archivo.
     * @param nombreArchIN Nombre del archivo de donde se leeran los datos.
     * @param nombreArchOUT Nombre del archivo donde se guardaran los datos.
     */
    public void partirArchivo_DBVisualizer(String nombreArchIN, String nombreArchOUT){
        int cantArchivos=1; // Inicializamos a uno, por que el nombre del primer archivo lo creamos sin usar esta variable.
        FileReader fr_path=null;
        BufferedReader br=null;

        FileWriter fw_path=null;

        BufferedWriter bw=null;
        PrintWriter copiadora=null;

        StringTokenizer tokens;

        String linea;
        //Se inicia la lecyura de los datos
        try {
            fr_path = new FileReader(this.path+nombreArchIN);
            br = new BufferedReader(fr_path);

            //Aca creamos el primer archivo.
            fw_path = new FileWriter(this.path+nombreArchOUT+"-01.sql");
            bw = new BufferedWriter(fw_path);

            copiadora=new PrintWriter(bw);

            while ((linea = br.readLine()) != null) {
//                System.out.println(linea);
                this.cant_lineas++;

                //creamos un archivo nuevo si ya hay exactamente 12000 filas.
                //Esta palabra nos indica donde debemos cortar el archivo.
                if(this.cant_lineas==12000)
                {
                   cantArchivos++;
                   copiadora.println(linea);
                   copiadora.flush();
                   copiadora.close();
                   fw_path.close();
                   if(cantArchivos<10)//Esta solo para que el nombre del archivo sea lindo.
                   {
                       fw_path = new FileWriter(path+nombreArchOUT+"-0"+cantArchivos+".sql");//c:\inserts_liquidacion\ + insert_liquidaciones_Det + -08
                       bw = new BufferedWriter(fw_path);
                   }
                   else
                   {
                       fw_path = new FileWriter(path+nombreArchOUT+"-"+cantArchivos+".sql");
                       bw = new BufferedWriter(fw_path);
                   }
                    

                   copiadora= new PrintWriter(bw);

                   this.cant_lineas=0;// ceramos las variable para generar el proximo archivo.
                }else
                {
                   //Escribimos en el archivo, si es que no debemos crear uno nuevo.
                    copiadora.println(linea);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //Se cierra el archivo.
            try {
                if (null != fr_path) {

                    fr_path.close();
                    br.close();
                } else if (null != fw_path) {
                    fw_path.close();
                    bw.close();
                }else if(copiadora!= null)
                {
                    copiadora.close();
                }


            } catch (IOException ex) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    public void contarLineas(String nombreArchIN)
    {

        FileReader fr_path=null;
        BufferedReader br=null;
        String linea;
        //Se inicia la lecyura de los datos
        try {
            fr_path = new FileReader(this.path+nombreArchIN);
            br = new BufferedReader(fr_path);

            while ((linea = br.readLine()) != null) {
//                System.out.println(linea);
                this.cant_lineas++;
            }
        }catch (IOException ex) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public int getCant_lineas() {
        return cant_lineas;
    }



}