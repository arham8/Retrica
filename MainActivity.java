package com.example.photoapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Button b, b2;
    int MY_CAMERA_PERMISSION_CODE =777;
    int CAMERA_REQUEST = 123;
    int PICK_FROM_GALLERY = 123;
    int RESULT_LOAD_IMAGE = 123;
    Uri imageUri;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        ll = findViewById(R.id.VeritcalScroll);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                          != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);}

                else {
                    ContentValues values = new ContentValues();

                    //values.put(MediaStore.Images.Media.TITLE, "Test Photo Title");
                    // values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_REQUEST);

                }
            }

        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]
                            {
                                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE }, PICK_FROM_GALLERY);
                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // receive photo from camera here???
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap thumbnail;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ImageView imageview = new ImageView(this);
                imageview.setImageBitmap(thumbnail);
                ll.addView(imageview);
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = "Your current location is https://www.latlong.net/c/?lat=31.454362&long=74.228387";
                        Intent ShareIntent = new Intent();
                        ShareIntent.setAction(Intent.ACTION_SEND);
                        ShareIntent.putExtra(Intent.EXTRA_TEXT, uri);
                        ShareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        ShareIntent.setType("image/jpeg");
                        startActivity(ShareIntent);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            ClipData mClipData = data.getClipData();
            if (data.getData() != null) {
                Uri selectedImage = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ImageView imageview = new ImageView(this);
                    imageview.setImageBitmap(bitmap);
                    ll.addView(imageview);
                    imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String uri = "Your current location is https://www.latlong.net/c/?lat=31.454362&long=74.228387";
                            Intent ShareIntent = new Intent();
                            ShareIntent.setAction(Intent.ACTION_SEND);
                            ShareIntent.putExtra(Intent.EXTRA_TEXT, uri);
                            ShareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            ShareIntent.setType("image/jpeg");
                            startActivity(ShareIntent);
                        }
                    });

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


    }




}