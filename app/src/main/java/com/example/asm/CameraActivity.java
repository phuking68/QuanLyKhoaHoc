package com.example.asm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final String JEG_FILE_PREFIX = "IMG_";
    private static final String JEG_FILE_SUFFIX = ".jpg";

    private String currentPhotoPath;
    private ImageView imgPhoto;
    private Button btnPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnPhoto = findViewById(R.id.btnPhoto);
        imgPhoto = findViewById(R.id.imgPhoto);

        //camera
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yêu cầu quyền truy cập vào camera
                if (ActivityCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }

                openCamera();
            }

        });

    }

    //hàm tạo file ảnh từ camera
    private File createFileImage() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JEG_FILE_PREFIX + timeStamp + "_";
        File storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, JEG_FILE_SUFFIX, storage);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //
    private void openCamera(){
        Intent takePictureIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File file = createFileImage();
            if ( file != null){
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.asm.fileprovider", file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 999);
            }

        }catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Không tìm thấy phần mềm chụp ảnh!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == RESULT_OK){
            setPhoto();
            getPhotoFromAlbum();
        }
    }

    //
    private void setPhoto(){
        //lấy kích thước của ImageView
        int targetW = imgPhoto.getWidth();
        int targetH = imgPhoto.getHeight();

        //lấy kích thước của ảnh
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;

        //
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW , photoH/targetH);
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);
        imgPhoto.setImageBitmap(bitmap);
    }

    //
    private void getPhotoFromAlbum(){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(currentPhotoPath);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
    }
}