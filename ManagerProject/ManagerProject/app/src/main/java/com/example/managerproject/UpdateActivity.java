package com.example.managerproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    final String DATABASE_NAME = "EmployeeDB.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOSE_PHOTO = 321;
    int id =-1;
    Button btnChonHinh,btnChupHinh,btnLuu,btnHuy;
    EditText edtTen,edSdt;
    ImageView imgHinhDaiDien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        addControls();
        addEvent();
        initUI();
    }
    private void addEvent(){
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePhoto();
            }
        });
        btnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePiture();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        SQLiteDatabase database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien WHERE ID = ? ",new String[]{id + ""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String sdt =cursor.getString(2);
        byte[] anh = cursor.getBlob(3);
        edtTen.setText(ten);
        edSdt.setText(sdt);
        Bitmap bitmap = BitmapFactory.decodeByteArray(anh,0,anh.length);
        imgHinhDaiDien.setImageBitmap(bitmap);
    }
    private void addControls() {
        btnChonHinh = (Button) findViewById(R.id.btnChonHinh);
        btnChupHinh = (Button)findViewById(R.id.btnChupHinh);
        btnLuu = (Button)findViewById(R.id.btnluu);
        btnHuy = (Button)findViewById(R.id.btnhuy);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imageHinhDaiDien);
        edtTen = (EditText) findViewById(R.id.editName);
        edSdt = (EditText) findViewById(R.id.editSdt);
    }
    private void takePiture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_TAKE_PHOTO);
    }

    private void chosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CHOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOSE_PHOTO) {
            try {
                Uri imageUri = data.getData();
                InputStream is = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imgHinhDaiDien.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgHinhDaiDien.setImageBitmap(bitmap);
        }
    }

    private void update(){
        String ten = edtTen.getText().toString();
        String sdt = edSdt.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgHinhDaiDien);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten",ten);
        contentValues.put("SDT",sdt);
        contentValues.put("Anh",anh);
        SQLiteDatabase database = Database.initDatabase(this,DATABASE_NAME);
        database.update("NhanVien", contentValues, "ID = ?", new String[]{id + ""});
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void cancel(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    private byte[] getByteArrayFromImageView(ImageView imgv){
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray =stream.toByteArray();
        return byteArray;
    }
}