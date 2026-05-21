package com.example.on_thi_1_2pdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SongDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SONGS = "songs";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_RATING = "rating";
    private static final String COL_SINGER = "singer";

    public SongDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_SONGS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_RATING + " REAL NOT NULL, "
                + COL_SINGER + " TEXT NOT NULL)";
        db.execSQL(createTableSql);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        insertSongInternal(db, new Song("Kiếp đỏ đen", 4.56f, "Duy Mạnh"));
        insertSongInternal(db, new Song("Xuân này con không về", 5.2f, "Quang Lê"));
        insertSongInternal(db, new Song("Nơi này có anh", 4.8f, "Sơn Tùng M-TP"));
    }

    private long insertSongInternal(SQLiteDatabase db, Song song) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, song.getName());
        values.put(COL_RATING, song.getRating());
        values.put(COL_SINGER, song.getSinger());
        return db.insert(TABLE_SONGS, null, values);
    }

    public long insertSong(Song song) {
        SQLiteDatabase db = getWritableDatabase();
        return insertSongInternal(db, song);
    }

    public int updateSong(Song song) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, song.getName());
        values.put(COL_RATING, song.getRating());
        values.put(COL_SINGER, song.getSinger());
        return db.update(TABLE_SONGS, values, COL_ID + "=?", new String[]{String.valueOf(song.getId())});
    }

    public List<Song> getAllSongs() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SONGS, null, null, null, null, null, COL_ID + " DESC");
        return mapCursorToSongs(cursor);
    }

    public List<Song> searchSongsByName(String keyword) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_SONGS,
                null,
                COL_NAME + " LIKE ?",
                new String[]{"%" + keyword + "%"},
                null,
                null,
                COL_ID + " DESC"
        );
        return mapCursorToSongs(cursor);
    }

    private List<Song> mapCursorToSongs(Cursor cursor) {
        List<Song> songs = new ArrayList<>();
        if (cursor == null) {
            return songs;
        }

        try {
            int idIndex = cursor.getColumnIndexOrThrow(COL_ID);
            int nameIndex = cursor.getColumnIndexOrThrow(COL_NAME);
            int ratingIndex = cursor.getColumnIndexOrThrow(COL_RATING);
            int singerIndex = cursor.getColumnIndexOrThrow(COL_SINGER);

            while (cursor.moveToNext()) {
                songs.add(new Song(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getFloat(ratingIndex),
                        cursor.getString(singerIndex)
                ));
            }
        } finally {
            cursor.close();
        }
        return songs;
    }
}

