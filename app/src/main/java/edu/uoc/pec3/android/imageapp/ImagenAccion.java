package edu.uoc.pec3.android.imageapp;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by dreyes on 28/04/2016.
 */
public class ImagenAccion {


    private static String directorio = Environment.getExternalStorageDirectory()+"/UOCImageApp";
    private static String nomfichero = "imageapp.jpg";
    private static String nomficheroTemp = "imagetemp.jpg";



    public static Boolean borrarImagen(){


        Boolean resultado = false;

        File file = new File(getPathFichero());

        if (file.exists()) {


            resultado = file.delete();
            Log.i("borrarImagen:", "Fichero Borrado");
        }

        return resultado;

    }

    public static String getPathFichero(){

    return directorio+"/"+nomfichero;


    }

    public static String getPathFicheroTemp(){

        return directorio+"/"+nomficheroTemp;


    }

    public static File crearFicheroTemp(){

        File miDirectorioImagen = new File(directorio);

        if (!miDirectorioImagen.exists())
        {
            miDirectorioImagen.mkdirs();
        }

        File file = new File(getPathFicheroTemp());

        if (file.exists()) {
            file.delete();
        }

        return file;



    }

    public static Boolean guardarImagen(Bitmap imagenOrigen){



            Boolean resultado=false;

            if (imagenOrigen!=null){


                File miDirectorioImagen = new File(directorio);

                if (!miDirectorioImagen.exists())
                {
                    miDirectorioImagen.mkdirs();
                }


                File file = new File(directorio, nomfichero);
                if (file.exists()) {
                        file.delete();
                    }

                FileOutputStream fos =null;

                try {

                    fos = new FileOutputStream(file);

                    Log.i("Ruta:", miDirectorioImagen.getAbsolutePath());

                    if (fos!= null) {
                        imagenOrigen.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                     Log.i("Fichero Creado", "Fichero creado");
                        fos.close();
                        resultado=true;
                     }
                }
                catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.i("Ruta:", "Error creación fichero");
                    resultado=false;
                }

            }
            return resultado;


    }





}
