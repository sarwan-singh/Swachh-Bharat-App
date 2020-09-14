package com.app.swachhbharat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Upload extends AppCompatActivity implements LocationListener {

    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private RelativeLayout main_upload, after_clicked;
    private LottieAnimationView loading, send, celebration;
    private ImageView image_main_upload_image;
    private TextView celebration_text, longitude_text, lattitude_text;
    private Button click_one, choose_from_gallery, click_another, upload;
    private CardView image_main_upload;
    private LocationManager locationManager;
    private FusedLocationProviderClient client;
    String currentImagePath = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        loading = findViewById(R.id.loading);
        main_upload = findViewById(R.id.main_upload);
        click_one = findViewById(R.id.button_click_one_button);
        choose_from_gallery = findViewById(R.id.button_choose_from_gallery_button);
        after_clicked = findViewById(R.id.afterClicked);
        image_main_upload = findViewById(R.id.image_main_upload);
        image_main_upload_image = findViewById(R.id.image_main_upload_image);
        click_another = findViewById(R.id.button_click_another);
        upload = findViewById(R.id.button_click_upload);
        celebration_text = findViewById(R.id.celebration_text);
        longitude_text = findViewById(R.id.longitude);
        lattitude_text = findViewById(R.id.lattitude);
        celebration = findViewById(R.id.celebration_animation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        choose_from_gallery = findViewById(R.id.button_choose_from_gallery_button);
        send = findViewById(R.id.send_animation);
        client = LocationServices.getFusedLocationProviderClient(this);
        main_upload.setVisibility(View.GONE);
        if (checkPermission()) {

            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {

                    loading.pauseAnimation();
                    loading.setVisibility(View.GONE);
                    main_upload.setVisibility(View.VISIBLE);
                }
            }, 1500);

        } else {
            requestPermission();
        }

        click_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    File imagefile = null;

                    try {
                        imagefile = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (imagefile != null) {
                        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.android.fileprovider", imagefile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 1);

                    }
                }

            }
        });
        click_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    File imagefile = null;

                    try {
                        imagefile = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Please choose a valid image", Toast.LENGTH_LONG).show();
                    }

                    if (imagefile != null) {
                        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.android.fileprovider", imagefile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 1);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please choose a valid image,Click the button below", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        choose_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage();

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                after_clicked.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                send.playAnimation();
                final Handler handler_send = new Handler();
                handler_send.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        send.pauseAnimation();
                        send.setVisibility(View.GONE);
                        celebration.playAnimation();
                        celebration.setVisibility(View.VISIBLE);
                        celebration_text.setVisibility(View.VISIBLE);
                    }
                }, 2000);

                final Handler handler_celebrate = new Handler();
                handler_celebrate.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        celebration.pauseAnimation();
                        celebration.setVisibility(View.GONE);
                        celebration_text.setVisibility(View.GONE);
                        Intent intent = new Intent(Upload.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 6000);


            }
        });


        location();  //Function To Detect Location
    }

    private void requestPermissionLocation(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void location() {

        if (checkSelfPermission(ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){

            return;
        }
        client.getLastLocation().addOnSuccessListener(Upload.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    //get Your Longitude And Lattitude Using @param longitude and @param lattitude respectively

                    Log.v("longitude", "lauda" + longitude);
                    Log.v("lattitude", "" + latitude);
                    longitude_text.setText("long.  " + longitude);
                    lattitude_text.setText("lati.  " + latitude);
                }

            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {

        Log.v("location",""+location);
        if (location!=null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            //get Your Longitude And Lattitude Using @param longitude and @param lattitude respectively

            Log.v("longitude", "lauda" + longitude);
            Log.v("lattitude", "" + latitude);
            longitude_text.setText("long.  " + longitude);
            lattitude_text.setText("lati.  " + latitude);
        }
    }




    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {

                Toast.makeText(getApplicationContext(), "Please Choose A Photo Properly", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image_main_upload_image.setImageBitmap(bitmap);
                after_clicked.setVisibility(View.VISIBLE);
                main_upload.setVisibility(View.GONE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        } else {

            Bitmap bitmap =BitmapFactory.decodeFile(currentImagePath);
            image_main_upload_image.setImageBitmap(bitmap);
            after_clicked.setVisibility(View.VISIBLE);
            main_upload.setVisibility(View.GONE);
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    final Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            loading.pauseAnimation();
                            loading.setVisibility(View.GONE);
                            main_upload.setVisibility(View.VISIBLE);
                        }
                    },1500);
                    // main logic


                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public  File getImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_MMmmss").format(new Date());
        String imageName = "jpg_" +timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath  = imageFile.getAbsolutePath();
        return imageFile;
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

