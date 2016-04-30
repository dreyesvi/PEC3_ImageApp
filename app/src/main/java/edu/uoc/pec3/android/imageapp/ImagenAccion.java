package edu.uoc.pec3.android.imageapp;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by dreyes on 28/04/2016.
 *
 */
public class ImagenAccion {


    private static String directorio = Environment.getExternalStorageDirectory()+"/UOCImageApp";
    private static String nomfichero = "imageapp.jpg";
    private static String nomficheroTemp = "imagetemp.jpg";


    // Comprueba si el fichero guardado existe y en este caso lo borra.
    // Devuelve true si lo ha borrado y false en caso contrario.

    public static Boolean borrarImagen(){


        Boolean resultado = false;

        // Obtiene el fichero de la imagen guardada.
        File file = new File(getPathFichero());

        if (file.exists()) {
            // Si el fichero existe lo  borra.
            resultado = file.delete();
            Log.i("borrarImagen:", "Fichero Borrado");
        }

        return resultado;

    }

    // Devuelve un string con el path y nombre del fichero utilizado para guardar la imagen.
    public static String getPathFichero(){

    return directorio+"/"+nomfichero;
    }

    // Devuelve un string con el path y nombre del fichero temporal utilizado cuando se captura una imagen.
    public static String getPathFicheroTemp(){

        return directorio+"/"+nomficheroTemp;

    }

    // Crea el directorio de guardado del fichero temporal si no existe y el fichero temporal. Si el fichero ya existe lo borra. Devuelve el fichero vacio.
    public static File crearFicheroTemp(){

        File miDirectorioImagen = new File(directorio);

        // Si el directorio no existe lo crea
        if (!miDirectorioImagen.exists())
        {
            miDirectorioImagen.mkdirs();
        }

        File file = new File(getPathFicheroTemp());

        // Si el fichero temporal ya existe lo borra.
        if (file.exists()) {
            file.delete();
        }

        return file;
    }

    public static Boolean guardarImagen(Bitmap imagenOrigen){

            Boolean resultado=false;

            // Verifica que la imagine de origen existe.
            if (imagenOrigen!=null){

                // Comprueba si el directorio destino existe, sino lo crea.
                File miDirectorioImagen = new File(directorio);
                if (!miDirectorioImagen.exists())
                {
                    miDirectorioImagen.mkdirs();
                }

                // Comprueba si el fichero de destino ya existe, en este caso lo borra.
                File file = new File(directorio, nomfichero);
                if (file.exists()) {
                        file.delete();
                    }

                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    Log.i("Ruta:", miDirectorioImagen.getAbsolutePath());
                    // Guarda el bipmap de la imagen origen en el fichero de destino.
                    if (fos!= null) {
                        imagenOrigen.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        Log.i("Fichero Creado", "Fichero creado");
                        fos.close();
                        resultado=true;
                     }
                }
                catch (IOException ex) {
                    // si existe un error se muestra
                    Log.i("Ruta:", ex.getMessage());
                    resultado=false;
                }
            }
            return resultado;
    }
}
