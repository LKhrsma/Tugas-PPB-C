package com.jibi.cameragalery;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button b1;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);
        iv = findViewById(R.id.imageView);

        b1.setOnClickListener(view -> {
            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityIntent.launch(it);
        });
    }
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.containsKey("data")) {
                        // Retrieve the captured image and set it to the ImageView
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        iv.setImageBitmap(imageBitmap);
                        Log.d("MainActivity", "Image captured and set to ImageView successfully.");
                    } else {
                        Log.e("MainActivity", "No image data found in result extras.");
                    }
                } else {
                    Log.e("MainActivity", "Image capture operation cancelled or failed.");
                }
            });
}

