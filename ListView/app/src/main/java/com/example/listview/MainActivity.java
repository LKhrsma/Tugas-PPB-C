package com.example.listview;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Data untuk ditampilkan dalam ListView
        String[] namaArray = {"Anggur", "Apel","Blubery", "Cery", "Duku", "Durian", "Gedondong", "Kelengkeng","Langsat", "leci", "Lemon", "Manggis",
                "Markisa","Melon","Nanas","Nangka","indah","Rambutan","Semangka","Tomat","Wortel", "Zaitun"};

        // Buat adapter untuk ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namaArray);

        // Dapatkan referensi ke ListView
        ListView listView = findViewById(R.id.listView);

        // Tetapkan adapter ke ListView
        listView.setAdapter(adapter);
    }

}