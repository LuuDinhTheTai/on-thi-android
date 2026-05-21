package com.example.on_thi_1_pdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "students.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_STUDENT = "students";
    public static final String COL_ID = "id";
    public static final String COL_MA_SV = "ma_sv";
    public static final String COL_TEN = "ten";
    public static final String COL_TOAN = "diem_toan";
    public static final String COL_HOA = "diem_hoa";
    public static final String COL_LY = "diem_ly";

    public StudentDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_STUDENT + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MA_SV + " INTEGER UNIQUE NOT NULL, "
                + COL_TEN + " TEXT NOT NULL, "
                + COL_TOAN + " INTEGER NOT NULL, "
                + COL_HOA + " INTEGER NOT NULL, "
                + COL_LY + " INTEGER NOT NULL"
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    public void seedSampleDataIfEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_STUDENT, null);
        boolean hasData = false;

        if (cursor.moveToFirst()) {
            hasData = cursor.getInt(0) > 0;
        }
        cursor.close();

        if (hasData) {
            return;
        }

        insertStudent(new Student(1001, "Ha", 9, 10, 9));
        insertStudent(new Student(1002, "Hung", 8, 8, 8));
        insertStudent(new Student(1003, "Huy", 7, 9, 8));
        insertStudent(new Student(1004, "Phuong", 10, 9, 9));
    }

    public boolean insertStudent(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(student);
        long result = db.insert(TABLE_STUDENT, null, values);
        return result != -1;
    }

    public boolean updateStudent(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(student);
        int rows = db.update(
                TABLE_STUDENT,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(student.getId())}
        );
        return rows > 0;
    }

    public List<Student> getStudentsByName(String keyword) {
        List<Student> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_STUDENT;
        String[] args = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " WHERE " + COL_TEN + " LIKE ?";
            args = new String[]{"%" + keyword.trim() + "%"};
        }

        sql += " ORDER BY " + COL_TEN + " COLLATE NOCASE ASC";

        Cursor cursor = db.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            int maSv = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MA_SV));
            String ten = cursor.getString(cursor.getColumnIndexOrThrow(COL_TEN));
            int diemToan = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOAN));
            int diemHoa = cursor.getInt(cursor.getColumnIndexOrThrow(COL_HOA));
            int diemLy = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LY));

            data.add(new Student(id, maSv, ten, diemToan, diemHoa, diemLy));
        }
        cursor.close();
        return data;
    }

    public int deleteStudentsByTotalLessThan(int minTotal) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "(" + COL_TOAN + " + " + COL_HOA + " + " + COL_LY + ") < ?";
        String[] whereArgs = new String[]{String.valueOf(minTotal)};
        return db.delete(TABLE_STUDENT, whereClause, whereArgs);
    }

    private ContentValues toContentValues(Student student) {
        ContentValues values = new ContentValues();
        values.put(COL_MA_SV, student.getMaSv());
        values.put(COL_TEN, student.getTen());
        values.put(COL_TOAN, student.getDiemToan());
        values.put(COL_HOA, student.getDiemHoa());
        values.put(COL_LY, student.getDiemLy());
        return values;
    }
}

