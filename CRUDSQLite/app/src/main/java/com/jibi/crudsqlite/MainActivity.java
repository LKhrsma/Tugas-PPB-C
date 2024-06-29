package com.jibi.crudsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nrp, nama;
    private Button simpan, ambildata;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper Opendb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nrp = findViewById(R.id.nrp);
        nama = findViewById(R.id.nama);
        simpan = findViewById(R.id.Simpan);
        ambildata = findViewById(R.id.ambildata);
        simpan.setOnClickListener(operasi);
        ambildata.setOnClickListener(operasi);

        // Initialize SQLiteOpenHelper here
        Opendb = new SQLiteOpenHelper(this, "db.sql", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {}

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
        };

        dbku = Opendb.getWritableDatabase();
        dbku.execSQL("create table if not exists mhs(nrp TEXT, nama TEXT);");
    }

    @Override
    protected void onStop() {
        dbku.close();
        Opendb.close();
        super.onStop();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.Simpan) {
                simpan();
            } else if (id == R.id.ambildata) {
                ambildata();
            }
        }
    };

    private void simpan() {
        ContentValues dataku = new ContentValues();
        dataku.put("nrp", nrp.getText().toString());
        dataku.put("nama", nama.getText().toString());
        dbku.insert("mhs", null, dataku);
        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_LONG).show();
    }

    private void ambildata() {
        Cursor cur = dbku.rawQuery("select * from mhs where nrp='" + nrp.getText().toString() + "'", null);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            int namaIndex = cur.getColumnIndex("nama");
            if (namaIndex != -1) {
                Toast.makeText(this, "Data Ditemukan Sejumlah " + cur.getCount(), Toast.LENGTH_LONG).show();
                nama.setText(cur.getString(namaIndex));
            } else {
                Toast.makeText(this, "Kolom 'nama' tidak ditemukan", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
        }
    }

}
