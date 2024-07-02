package com.jibi.cameragalerywebserver;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4;
    ImageView iv;
    Bitmap imageBitmap;
    WebServiceAPI webServiceAPI;

    // ImageResponse imageResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        iv = findViewById(R.id.imageView);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.18.252:8000/") // Ganti dengan IP atau domain server Anda
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        // Inisialisasi Interface API
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        b1.setOnClickListener(view -> {
            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getImageIntent.launch(it);
        });

        b2.setOnClickListener(view -> {
            // call get files
            Call<ImageResponse> call = webServiceAPI.getFiles();

            // only check success or not and print the response
            call.enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(@NonNull Call<ImageResponse> call, @NonNull Response<ImageResponse> response) {
                    if (response.isSuccessful()) {
                        // Jika berhasil, ambil URL gambar dari respons
                        ImageResponse imageResponse = response.body();
                        if (imageResponse != null && imageResponse.getUrl() != null) {
                            // Gunakan URL untuk mengambil gambar dari server dan tampilkan di ImageView
                            System.out.println(imageResponse.getUrl());
                            loadImageFromServer(imageResponse.getUrl());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Get Image failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ImageResponse> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "Upload failed: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    System.out.println(t.getMessage() + " hehe");
                }
            });
        });

        // b3 just remove image from iv and change it to mipmap/ic_launcher
        b3.setOnClickListener(view -> {
            iv.setImageResource(R.mipmap.ic_launcher);
            Toast.makeText(MainActivity.this, "Image removed",
                    Toast.LENGTH_SHORT).show();
        });

        // b4 just remove image from server ;/remove
        b4.setOnClickListener(view -> {
            iv.setImageResource(R.mipmap.ic_launcher);
            removeImageFromServer();
        });

    }

    ActivityResultLauncher<Intent> getImageIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.containsKey("data")) {
                        // Retrieve the captured image
                        imageBitmap = (Bitmap) extras.get("data");
                        // iv.setImageBitmap(imageBitmap);

                        // Upload the image to server
                        uploadImageToServer(imageBitmap);
                    } else {
                        Log.e("MainActivity", "No image data found in result extras.");
                    }
                } else {
                    Log.e("MainActivity", "Image capture operation cancelled or failed.");
                }
            });

    // Method Remove image from server
    private void removeImageFromServer() {
        // call get files
        Call<BasicResponse> call = webServiceAPI.removeImage();

        // only check success or not and print the response
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                if (response.isSuccessful()) {
                    // Jika berhasil, ambil URL gambar dari respons
                    BasicResponse basicResponse = response.body();
                    if (basicResponse != null && basicResponse.getMessage() != null) {
                        Toast.makeText(MainActivity.this, basicResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Remove Image failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Remove Image failed: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage() + " hehe");
            }
        });
    }

    // Method untuk mengunggah gambar ke server
    private void uploadImageToServer(Bitmap bitmap) {
        try {
            // Mengubah bitmap ke bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Membuat request body dari bytes
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);

            // call get files
            // Call<ImageResponse> call = webServiceAPI.getFiles();

            // only check success or not and print the response
            // call.enqueue(new Callback<ImageResponse>() {
            // @Override
            // public void onResponse(@NonNull Call<ImageResponse> call, @NonNull
            // Response<ImageResponse> response) {
            // if (response.isSuccessful()) {
            // // Jika berhasil, ambil URL gambar dari respons
            // ImageResponse imageResponse = response.body();
            // if (imageResponse != null && imageResponse.getUrl() != null) {
            // // Gunakan URL untuk mengambil gambar dari server dan tampilkan di ImageView
            // loadImageFromServer(imageResponse.getUrl());
            // }
            // } else {
            // Toast.makeText(MainActivity.this, "Upload failed",
            // Toast.LENGTH_SHORT).show();
            // }
            // }

            // @Override
            // public void onFailure(@NonNull Call<ImageResponse> call, @NonNull Throwable
            // t) {
            // Toast.makeText(MainActivity.this, "Upload failed: " + t.getMessage(),
            // Toast.LENGTH_SHORT).show();
            // System.out.println(t.getMessage() + " hehe");
            // }
            // });

            // Mengirim request ke server
            Call<BasicResponse> call = webServiceAPI.uploadImage(body);
            call.enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                    if (response.isSuccessful()) {

                        // toast message
                        Toast.makeText(MainActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Upload failed",
                                Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "Upload failed: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    System.out.println(t.getMessage() + " hehe");
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Method untuk mengambil gambar dari server dan menampilkannya di ImageView
    private void loadImageFromServer(String imageUrl) {
        // change to https
        GlideApp.with(this)
                .load(imageUrl)
                .override(500, 500)
                .fitCenter()
                .into(iv);

    }

    // Interface untuk Web Service API menggunakan Retrofit
    public interface WebServiceAPI {
        @retrofit2.http.Multipart
        @retrofit2.http.POST("upload")
        Call<BasicResponse> uploadImage(@retrofit2.http.Part MultipartBody.Part image);

        @retrofit2.http.GET("geturl")
        Call<ImageResponse> getFiles();

        @retrofit2.http.GET("remove")
        Call<BasicResponse> removeImage();
    }

    // Kelas POJO untuk respons dari server
    public static class ImageResponse {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }
    }

    // Kelas basic untuk respons dari server
    public static class BasicResponse {
        @SerializedName("message")
        private String message;

        public String getMessage() {
            return message;
        }
    }
}
