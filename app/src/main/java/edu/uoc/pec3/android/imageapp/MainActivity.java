package edu.uoc.pec3.android.imageapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TAG logs
    private final String TAG = this.getClass().getSimpleName();

    // constante para solicitud de permisos de captura de imagen
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA =123;

    // Views
    private Button mButtonOpenImage;
    private ImageView mImageView;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set views
        mButtonOpenImage = (Button) findViewById(R.id.button);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mTextView = (TextView) findViewById(R.id.textView);

        // Set listeners
        mButtonOpenImage.setOnClickListener(this);

        //Verifica si existe una imagen guardada y en tal caso la muestra
        File file = new File(ImagenAccion.getPathFichero());

        if (file.exists()) {

            mImageView.setImageBitmap(BitmapFactory.decodeFile(ImagenAccion.getPathFichero()));
            mTextView.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Gestionar la seleccion de un item del menu
        switch (id){

            case R.id.menu_item_borrar:

                    File file = new File(ImagenAccion.getPathFichero());

                if (file.exists()) {

                    // dialogo de confirmación

                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            MainActivity.this);
                    alert.setTitle("Delete Image!!");
                    alert.setMessage("Do you want to delete this image?");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                           if (ImagenAccion.borrarImagen()) {
                               // Borra la imagen
                               mImageView.setImageDrawable(null);
                               // Muestra el texto de que no hay imagen.
                               mTextView.setVisibility(View.VISIBLE);

                               //Mensaje de confirmación
                               Context context = getApplicationContext();
                               CharSequence text = "Image Deleted Correctly!!";
                               int duration = Toast.LENGTH_SHORT;

                               Toast toast = Toast.makeText(context, text, duration);
                               toast.show();
                           }
                            else
                           {
                               // no existe la imagen.
                               Context context = getApplicationContext();
                               CharSequence text = "Ops!! Image is empty !!";
                               int duration = Toast.LENGTH_SHORT;

                               Toast toast = Toast.makeText(context, text, duration);
                               toast.show();

                           }

                            dialog.dismiss();

                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();

                }


                return true;
            case R.id.menu_item_guardar:

                boolean hasDrawable = (mImageView.getDrawable() != null);
                if(hasDrawable) {


                       if (ImagenAccion.guardarImagen(((BitmapDrawable) mImageView.getDrawable()).getBitmap())) {
                           // La imagen se ha guardado correctamente
                           Context context = getApplicationContext();
                           CharSequence text = "Image Saved Correctly!!";
                           int duration = Toast.LENGTH_SHORT;

                           Toast toast = Toast.makeText(context, text, duration);
                           toast.show();
                       } else {
                           // La imagen no se ha guardado

                           Context context = getApplicationContext();
                           CharSequence text = "Ops! Image not Saved!!";
                           int duration = Toast.LENGTH_SHORT;

                           Toast toast = Toast.makeText(context, text, duration);
                           toast.show();
                       }
                   }
                    else {

                       // La imagen no se ha guardado

                       Context context = getApplicationContext();
                       CharSequence text = "Ops! There is not Image !!";
                       int duration = Toast.LENGTH_SHORT;

                       Toast toast = Toast.makeText(context, text, duration);
                       toast.show();
                   }


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onClick(View v) {



        if (v == mButtonOpenImage) {

            // Verificar si ya se han aceptado los permisos para utilizar la cámara
            int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);

            // Verificar si ya se han aceptado los permisos para utilizar la cámara
            int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheckCamera == PackageManager.PERMISSION_GRANTED && permissionCheckStorage==PackageManager.PERMISSION_GRANTED) {
                // El usuario ya ha aceptado los permisos

                // Lanza un intent para capturar la imagen de la cámara
                Intent capturaImagen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File ficheroImagen = null;

                ficheroImagen = ImagenAccion.crearFicheroTemp();


                // Continue only if the File was successfully created
                if (ficheroImagen != null) {
                    capturaImagen.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(ficheroImagen));


                    startActivityForResult(capturaImagen, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }

            }
            else {
                // Se solictan permisos

                if (permissionCheckCamera == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
                if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }


            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Captura el resultado al volver del Intent.
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(ImagenAccion.getPathFicheroTemp(), bmOptions);




            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            }

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            //bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(ImagenAccion.getPathFicheroTemp(), bmOptions);

		/* Associate the Bitmap to the ImageView */
            mImageView.setImageBitmap(bitmap);
            mTextView.setVisibility(View.INVISIBLE);




        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        // Gestiona la respuesta del usuario a la solicitud de permisos
        switch (requestCode) {

            // Si la solicitud de permiso es para la cámara
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Verificar si ya se han aceptado los permisos para guardar la foto
                    int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                    else{


                        // Lanza un intent para capturar la imagen de la cámara
                        Intent capturaImagen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File ficheroImagen = null;

                        ficheroImagen = ImagenAccion.crearFicheroTemp();


                        // Continue only if the File was successfully created
                        if (ficheroImagen != null) {
                            capturaImagen.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(ficheroImagen));


                            startActivityForResult(capturaImagen, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }

                    }


                } else {

                    Context context = getApplicationContext();
                    CharSequence text = "Ops! Permission to CAMERA DENIED !!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                }
                return;
            }

            // Si la solicitud de permiso es para la cámara
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Verificar si ya se han aceptado los permisos para utilizar la cámara

                    int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA);

                    if (permissionCheckCamera == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    else{

                        // Lanza un intent para capturar la imagen de la cámara
                        Intent capturaImagen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File ficheroImagen = null;

                        ficheroImagen = ImagenAccion.crearFicheroTemp();


                        // Continue only if the File was successfully created
                        if (ficheroImagen != null) {
                            capturaImagen.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(ficheroImagen));


                            startActivityForResult(capturaImagen, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                    }



                } else {

                Context context = getApplicationContext();
                CharSequence text = "Ops! Permission to SAVE IMAGE DENIED !!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                }

                return;

            }


        }
    }

}
