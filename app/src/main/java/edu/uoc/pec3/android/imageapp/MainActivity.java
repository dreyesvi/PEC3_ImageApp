package edu.uoc.pec3.android.imageapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TAG logs
    private final String TAG = this.getClass().getSimpleName();

    // constante para solicitud de permisos de captura de imagen
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA =123;


    private Uri fileUri;
    String mCurrentPhotoPath;

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
        return super.onOptionsItemSelected(item);
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


                try {
                    ficheroImagen = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
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
           /* Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            mTextView.setVisibility(View.INVISIBLE);*/

            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);




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
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
            mImageView.setImageBitmap(bitmap);
            mTextView.setVisibility(View.INVISIBLE);




        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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

                   // Se autoriza a la app a usar la cámara por lo que se llama al intent.

                    Intent capturaImagen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    startActivityForResult(capturaImagen, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // Si la solicitud de permiso es para la cámara
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Se autoriza a la app a usar la cámara por lo que se llama al intent.

                    Intent capturaImagen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    startActivityForResult(capturaImagen, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;

            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
