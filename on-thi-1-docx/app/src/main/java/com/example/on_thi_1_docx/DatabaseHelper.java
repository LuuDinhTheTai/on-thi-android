package com.example.on_thi_1_docx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "nhahang.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NHA_HANG = "NhaHang";
    private static final String COL_ID = "id";
    private static final String COL_TEN = "ten";
    private static final String COL_DIA_CHI = "diaChi";
    private static final String COL_DANH_GIA = "danhGia";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NHA_HANG + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TEN + " TEXT NOT NULL, "
                + COL_DIA_CHI + " TEXT NOT NULL, "
                + COL_DANH_GIA + " REAL NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHA_HANG);
        onCreate(db);
    }

    public long insertNhaHang(NhaHang nhaHang) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TEN, nhaHang.getTen());
        values.put(COL_DIA_CHI, nhaHang.getDiaChi());
        values.put(COL_DANH_GIA, nhaHang.getDanhGia());
        return db.insert(TABLE_NHA_HANG, null, values);
    }

    public List<NhaHang> getDanhSachNhaHang(String tenCanTim) {
        List<NhaHang> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selection = null;
        String[] args = null;
        if (tenCanTim != null && !tenCanTim.trim().isEmpty()) {
            selection = COL_TEN + " LIKE ?";
            args = new String[]{"%" + tenCanTim.trim() + "%"};
        }

        try (Cursor cursor = db.query(
                TABLE_NHA_HANG,
                null,
                selection,
                args,
                null,
                null,
                COL_ID + " ASC"
        )) {
            int idIndex = cursor.getColumnIndexOrThrow(COL_ID);
            int tenIndex = cursor.getColumnIndexOrThrow(COL_TEN);
            int diaChiIndex = cursor.getColumnIndexOrThrow(COL_DIA_CHI);
            int danhGiaIndex = cursor.getColumnIndexOrThrow(COL_DANH_GIA);

            while (cursor.moveToNext()) {
                result.add(new NhaHang(
                        cursor.getInt(idIndex),
                        cursor.getString(tenIndex),
                        cursor.getString(diaChiIndex),
                        cursor.getDouble(danhGiaIndex)
                ));
            }
        }

        return result;
    }

    public int xoaNhaHangDiemThapHon(double diemDanhGia) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NHA_HANG, COL_DANH_GIA + " < ?", new String[]{String.valueOf(diemDanhGia)});
    }
}

