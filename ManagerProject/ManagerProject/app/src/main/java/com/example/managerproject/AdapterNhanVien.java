package com.example.managerproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterNhanVien extends BaseAdapter {
    Activity context;
    ArrayList<NhanVien>list;

    public AdapterNhanVien(Activity context, ArrayList<NhanVien> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inlater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inlater.inflate(R.layout.listview_row,null);
        ImageView imgHinhDaiDien = (ImageView) row.findViewById(R.id.imageHinhDaiDien);
        TextView txtId = (TextView) row.findViewById(R.id.txtId);
        TextView txtTen = (TextView) row.findViewById(R.id.txtTen);
        TextView txtSdt = (TextView) row.findViewById(R.id.txtSdt);
        Button btnsua = (Button) row.findViewById(R.id.btnSua) ;
        Button btnxoa = (Button) row.findViewById(R.id.btnXoa);
        NhanVien nhanVien = list.get(position);
        txtId.setText(nhanVien.id + "");
        txtTen.setText(nhanVien.ten);
        txtSdt.setText(nhanVien.sdt);
        Bitmap bmHinhDaiDien = BitmapFactory.decodeByteArray(nhanVien.anh, 0,nhanVien.anh.length);
        imgHinhDaiDien.setImageBitmap(bmHinhDaiDien);
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateActivity.class);
                intent.putExtra("ID",nhanVien.id);
                context.startActivity(intent);
            }
        });
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("xac nhan xoa");
                builder.setMessage("Ban co chac  chan muon xoa nhan vien nay?");
                builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Delete(nhanVien.id);
                    }
                });
                builder.setNegativeButton("khong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }

    private void Delete(int idNhanVien) {
        SQLiteDatabase database = Database.initDatabase(context, "EmployeeDB.sqlite");
        database.delete("NhanVien", "ID =?",new String[]{idNhanVien + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);
            list.add(new NhanVien(id,ten,sdt,anh));
        }
        notifyDataSetChanged();
    }
}
