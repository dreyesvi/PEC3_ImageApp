package edu.uoc.pec3.android.imageapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dreyes on 28/04/2016.
 */
public class ImagenAccion {

    private Boolean imagenGuardada;




    void ImagenAccion (){

    }

    public Boolean getImagenGuardada() {
        return imagenGuardada;
    }

    public void setImagenGuardad(Boolean imagenGuardada) {
        this.imagenGuardada = imagenGuardada;
    }

    public static void borrarImagen(){



    }

    public static void guardarImagen(Bitmap imagenOrigen){

        try {

            File miFicheroImagen = new File(Environment.getExternalStorageDirectory()+"/UOCImageApp","imageapp.jpg");

            FileOutputStream fos =null;

            fos = new FileOutputStream(miFicheroImagen);

            imagenOrigen.compress(Bitmap.CompressFormat.JPEG,100,fos);

            fos.close();



        }
        catch (IOException ex) {

        };




    }

    protected static Boolean hayImagenGuardad(){

        return true;

    }




}
