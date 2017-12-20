package com.example.jennydegtiar.nuditydetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
//import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView draweeView;
    private List<String> fileNames = new ArrayList<>();
    private String cameraFolderBase = "/storage/emulated/0/DCIM/Camera/";
    private int cameraPicIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);


        //String pathname = cameraFolderBase + "/IMG_20171219_200123.jpg";


        File imgFile = new File(cameraFolderBase);
        File[] files = imgFile.listFiles();

        Arrays.sort(files, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });

        for (File inFile : files) {
            if (!inFile.isDirectory()) {
                fileNames.add(inFile.getName());
            }
        }

//        Uri imageUri = Uri.parse("https://i.imgur.com/tGbaZCY.jpg");
        draweeView = (SimpleDraweeView) findViewById(R.id.sdvImage);


        draweeView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
//                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "MainActivity.this, top");
            }

            public void onSwipeRight() {
//                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "MainActivity.this, right");
                if (cameraPicIndex > 0) {
                    cameraPicIndex--;
                    Uri imageUri = Uri.fromFile(new File(cameraFolderBase + fileNames.get(cameraPicIndex)));// For files on device
                    draweeView.setImageURI(imageUri);
                }
            }

            public void onSwipeLeft() {
//                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "MainActivity.this, left");
                if (cameraPicIndex < fileNames.size() - 1) {
                    cameraPicIndex++;
                    Uri imageUri = Uri.fromFile(new File(cameraFolderBase + fileNames.get(cameraPicIndex)));// For files on device
                    draweeView.setImageURI(imageUri);
                }
            }

            public void onSwipeBottom() {
//                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "MainActivity.this, bottom");
            }
        });


        cameraPicIndex = 0;
        Uri imageUri = Uri.fromFile(new File(cameraFolderBase + fileNames.get(cameraPicIndex)));// For files on device
        draweeView.setImageURI(imageUri);


    }

}
