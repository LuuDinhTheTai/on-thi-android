package com.example.on_thi_1_3_pdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VeTau.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_VE = "ve";
    public static final String COL_MA_VE = "ma_ve";
    public static final String COL_GA_DI = "ga_di";
    public static final String COL_GA_DEN = "ga_den";
    public static final String COL_DON_GIA = "don_gia";
    public static final String COL_LOAI_VE = "loai_ve"; // 1 = Khứ Hồi, 0 = Một Chiều

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_VE + " (" +
                    COL_MA_VE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_GA_DI + " TEXT NOT NULL, " +
                    COL_GA_DEN + " TEXT NOT NULL, " +
                    COL_DON_GIA + " REAL NOT NULL, " +
                    COL_LOAI_VE + " INTEGER NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VE);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        String[][] data = {
                {"Vinh", "Nam Định", "667.85", "1"},
                {"Nam Định", "Thanh Hóa", "451.25", "1"},
                {"Hà Nội", "Nam Định", "248.88", "1"},
                {"Thanh Hóa", "Hà Nội", "170.0", "0"},
                {"Hà Nội", "Thanh Hóa", "170.0", "0"}
        };
        for (String[] row : data) {
            ContentValues cv = new ContentValues();
            cv.put(COL_GA_DI, row[0]);
            cv.put(COL_GA_DEN, row[1]);
            cv.put(COL_DON_GIA, Float.parseFloat(row[2]));
            cv.put(COL_LOAI_VE, Integer.parseInt(row[3]));
            db.insert(TABLE_VE, null, cv);
        }
    }

    public long insertVe(Ve ve) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_GA_DI, ve.getGaDi());
        cv.put(COL_GA_DEN, ve.getGaDen());
        cv.put(COL_DON_GIA, ve.getDonGia());
        cv.put(COL_LOAI_VE, ve.isLoaiVe() ? 1 : 0);
        long id = db.insert(TABLE_VE, null, cv);
        db.close();
        return id;
    }

    public int updateVe(Ve ve) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_GA_DI, ve.getGaDi());
        cv.put(COL_GA_DEN, ve.getGaDen());
        cv.put(COL_DON_GIA, ve.getDonGia());
        cv.put(COL_LOAI_VE, ve.isLoaiVe() ? 1 : 0);
        int rows = db.update(TABLE_VE, cv, COL_MA_VE + "=?",
                new String[]{String.valueOf(ve.getMaVe())});
        db.close();
        return rows;
    }

    public int deleteVe(int maVe) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_VE, COL_MA_VE + "=?",
                new String[]{String.valueOf(maVe)});
        db.close();
        return rows;
    }

    public List<Ve> getAllVe() {
        List<Ve> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VE, null);
        if (cursor.moveToFirst()) {
            do {
                Ve ve = cursorToVe(cursor);
                list.add(ve);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Ve> searchVe(String keyword) {
        List<Ve> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_VE +
                " WHERE " + COL_GA_DI + " LIKE ? OR " + COL_GA_DEN + " LIKE ?";
        String pattern = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(query, new String[]{pattern, pattern});
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToVe(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    private Ve cursorToVe(Cursor cursor) {
        Ve ve = new Ve();
        ve.setMaVe(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MA_VE)));
        ve.setGaDi(cursor.getString(cursor.getColumnIndexOrThrow(COL_GA_DI)));
        ve.setGaDen(cursor.getString(cursor.getColumnIndexOrThrow(COL_GA_DEN)));
        ve.setDonGia(cursor.getFloat(cursor.getColumnIndexOrThrow(COL_DON_GIA)));
        ve.setLoaiVe(cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOAI_VE)) == 1);
        return ve;
    }
}

