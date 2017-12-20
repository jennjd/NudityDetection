package com.example.jennydegtiar.nuditydetection;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jennydegtiar.nuditydetection.com.example.jennydegtiar.nuditydetection.encryptor.EncryptorFacade;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

//import com.stfalcon.frescoimageviewer.ImageViewer;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView draweeView;
    private List<String> fileNames = new ArrayList<>();
    private String cameraFolderBase = "/storage/emulated/0/WhatsApp/Media/WhatsApp Images/";
    private int cameraPicIndex;
    private int numberOfFilesToPresent = 3;
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

        int i =0;
        int counter = 0;
        while (i < files.length && counter < numberOfFilesToPresent) {
            if (!files[i].isDirectory()) {
                counter++;
                String tempImageFile = null;
                try {
                    if(isNotTemporaryFile(files[i]) && EncryptorFacade.isImageEncrypted(files[i].getAbsolutePath())) {
                        tempImageFile = EncryptorFacade.decryptImageToTemporaryImage(files[i].getAbsolutePath(), "123", "1234", "5555");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ImageWriteException e) {
                    e.printStackTrace();
                } catch (ImageReadException e) {
                    e.printStackTrace();
                }
                if(tempImageFile == null && isNotTemporaryFile(files[i])) {
                    fileNames.add(files[i].getName());
                } else {
                    fileNames.add(tempImageFile.substring(tempImageFile.lastIndexOf("/")+1));
                }
            }
            i++;
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

    private boolean isNotTemporaryFile(File file) {
        return !file.getAbsolutePath().endsWith("_temp.jpg");
    }

}
