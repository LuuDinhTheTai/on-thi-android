package com.example.on_thi_1_docx;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private NhaHangAdapter nhaHangAdapter;
    private EditText edtSearch;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);
        networkChangeReceiver = new NetworkChangeReceiver();

        edtSearch = findViewById(R.id.edtSearch);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNhaHang);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        nhaHangAdapter = new NhaHangAdapter(this::hienThiXacNhanXoa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nhaHangAdapter);

        fabAdd.setOnClickListener(v -> {
            themDuLieuMau();
            Toast.makeText(this, "Them du lieu thanh cong.", Toast.LENGTH_SHORT).show();
            taiDanhSachNhaHang(edtSearch.getText().toString());
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taiDanhSachNhaHang(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        taiDanhSachNhaHang("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(networkChangeReceiver, filter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(networkChangeReceiver, filter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }

    private void taiDanhSachNhaHang(String tuKhoa) {
        List<NhaHang> ds = databaseHelper.getDanhSachNhaHang(tuKhoa);
        nhaHangAdapter.setItems(ds);
    }

    private void themDuLieuMau() {
        List<NhaHang> mau = Arrays.asList(
                new NhaHang("Sen Tay Ho", "514 Lac Long Quan", 8.6),
                new NhaHang("Non La", "Nguyen Dinh Chieu", 8.8),
                new NhaHang("Quan Ngon Ha Noi", "Phan Boi Chau", 8.9),
                new NhaHang("Luc Thuy", "Le Thai To", 8.5),
                new NhaHang("Charm Cham", "Phan Van Chuong", 8.2),
                new NhaHang("Ly Club", "Le Phung Hieu", 7.8)
        );

        for (NhaHang nhaHang : mau) {
            databaseHelper.insertNhaHang(nhaHang);
        }
    }

    private void hienThiXacNhanXoa(NhaHang nhaHangDangChon) {
        new AlertDialog.Builder(this)
                .setTitle("Ban chac chua ?")
                .setMessage("Confirm to delete")
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("OK", (dialog, which) -> {
                    databaseHelper.xoaNhaHangDiemThapHon(nhaHangDangChon.getDanhGia());
                    taiDanhSachNhaHang(edtSearch.getText().toString());
                })
                .show();
    }
}