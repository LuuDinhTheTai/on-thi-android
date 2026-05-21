package com.example.on_thi_1_3_pdf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    private static final String EXTRA_MA_VE = "extra_ma_ve";
    private static final String EXTRA_GA_DI = "extra_ga_di";
    private static final String EXTRA_GA_DEN = "extra_ga_den";
    private static final String EXTRA_DON_GIA = "extra_don_gia";
    private static final String EXTRA_LOAI_VE = "extra_loai_ve";

    private TextInputEditText etGaDi, etGaDen, etDonGia;
    private RadioGroup rgLoaiVe;
    private RadioButton rbKhuHoi, rbMotChieu;
    private Button btnAction, btnQuayVe;
    private DatabaseHelper dbHelper;
    private Ve currentVe;
    private boolean isEdit = false;

    public static void start(Context context, Ve ve) {
        Intent intent = new Intent(context, AddEditActivity.class);
        if (ve != null) {
            intent.putExtra(EXTRA_MA_VE, ve.getMaVe());
            intent.putExtra(EXTRA_GA_DI, ve.getGaDi());
            intent.putExtra(EXTRA_GA_DEN, ve.getGaDen());
            intent.putExtra(EXTRA_DON_GIA, ve.getDonGia());
            intent.putExtra(EXTRA_LOAI_VE, ve.isLoaiVe());
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dbHelper = new DatabaseHelper(this);
        etGaDi = findViewById(R.id.etGaDi);
        etGaDen = findViewById(R.id.etGaDen);
        etDonGia = findViewById(R.id.etDonGia);
        rgLoaiVe = findViewById(R.id.rgLoaiVe);
        rbKhuHoi = findViewById(R.id.rbKhuHoi);
        rbMotChieu = findViewById(R.id.rbMotChieu);
        btnAction = findViewById(R.id.btnAction);
        btnQuayVe = findViewById(R.id.btnQuayVe);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MA_VE)) {
            isEdit = true;
            currentVe = new Ve();
            currentVe.setMaVe(intent.getIntExtra(EXTRA_MA_VE, 0));
            currentVe.setGaDi(intent.getStringExtra(EXTRA_GA_DI));
            currentVe.setGaDen(intent.getStringExtra(EXTRA_GA_DEN));
            currentVe.setDonGia(intent.getFloatExtra(EXTRA_DON_GIA, 0f));
            currentVe.setLoaiVe(intent.getBooleanExtra(EXTRA_LOAI_VE, true));

            etGaDi.setText(currentVe.getGaDi());
            etGaDen.setText(currentVe.getGaDen());
            etDonGia.setText(String.valueOf(currentVe.getDonGia()));
            if (currentVe.isLoaiVe()) rbKhuHoi.setChecked(true);
            else rbMotChieu.setChecked(true);

            btnAction.setText("SỬA");
        } else {
            btnAction.setText("THÊM");
            rbKhuHoi.setChecked(true);
        }

        btnAction.setOnClickListener(v -> saveVe());
        btnQuayVe.setOnClickListener(v -> finish());
    }

    private void saveVe() {
        String gaDi = etGaDi.getText() != null ? etGaDi.getText().toString().trim() : "";
        String gaDen = etGaDen.getText() != null ? etGaDen.getText().toString().trim() : "";
        String donGiaStr = etDonGia.getText() != null ? etDonGia.getText().toString().trim() : "";

        if (gaDi.isEmpty() || gaDen.isEmpty() || donGiaStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        float donGia;
        try {
            donGia = Float.parseFloat(donGiaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Đơn giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean loaiVe = rbKhuHoi.isChecked();
        Ve ve = new Ve(0, gaDi, gaDen, donGia, loaiVe);

        if (isEdit) {
            ve.setMaVe(currentVe.getMaVe());
            dbHelper.updateVe(ve);
            Toast.makeText(this, "Cập nhật thành công! Giá: " +
                    String.format(Locale.getDefault(), "%,.3f", ve.tinhGia()), Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.insertVe(ve);
            Toast.makeText(this, "Thêm thành công! Giá: " +
                    String.format(Locale.getDefault(), "%,.3f", ve.tinhGia()), Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}


