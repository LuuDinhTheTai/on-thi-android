package com.example.on_thi_1_2pdf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private EditText edtSongName;
    private EditText edtSingerName;
    private EditText edtRating;
    private SearchView searchView;
    private SongDbHelper songDbHelper;
    private SongAdapter songAdapter;

    private Song selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        songDbHelper = new SongDbHelper(this);
        songAdapter = new SongAdapter(song -> {
            selectedSong = song;
            edtSongName.setText(song.getName());
            edtSingerName.setText(song.getSinger());
            edtRating.setText(String.valueOf(song.getRating()));
        });

        RecyclerView recyclerView = findViewById(R.id.rvSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);

        setupListeners();
        loadSongs(null);
    }

    private void initViews() {
        edtSongName = findViewById(R.id.edtSongName);
        edtSingerName = findViewById(R.id.edtSingerName);
        edtRating = findViewById(R.id.edtRating);
        searchView = findViewById(R.id.searchSong);
    }

    private void setupListeners() {
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnOpenRingtone = findViewById(R.id.btnOpenRingtone);

        btnAdd.setOnClickListener(v -> addSong());
        btnUpdate.setOnClickListener(v -> updateSong());
        btnOpenRingtone.setOnClickListener(v -> startActivity(new Intent(this, RingtoneActivity.class)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSongs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadSongs(newText);
                return true;
            }
        });
    }

    private void addSong() {
        Song song = getSongFromInput(false);
        if (song == null) {
            return;
        }

        long result = songDbHelper.insertSong(song);
        if (result > 0) {
            clearInputs();
            loadSongs(searchView.getQuery().toString());
            Toast.makeText(this, "Thêm bài hát thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không thể thêm bài hát", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSong() {
        if (selectedSong == null) {
            Toast.makeText(this, "Chọn bài hát cần sửa trong danh sách", Toast.LENGTH_SHORT).show();
            return;
        }

        Song songInput = getSongFromInput(true);
        if (songInput == null) {
            return;
        }

        selectedSong.setName(songInput.getName());
        selectedSong.setSinger(songInput.getSinger());
        selectedSong.setRating(songInput.getRating());

        int rows = songDbHelper.updateSong(selectedSong);
        if (rows > 0) {
            clearInputs();
            selectedSong = null;
            loadSongs(searchView.getQuery().toString());
            Toast.makeText(this, "Sửa bài hát thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không thể sửa bài hát", Toast.LENGTH_SHORT).show();
        }
    }

    private Song getSongFromInput(boolean forUpdate) {
        String name = edtSongName.getText().toString().trim();
        String singer = edtSingerName.getText().toString().trim();
        String ratingText = edtRating.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(singer) || TextUtils.isEmpty(ratingText)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {
            float rating = Float.parseFloat(ratingText);
            if (forUpdate && selectedSong != null) {
                return new Song(selectedSong.getId(), name, rating, singer);
            }
            return new Song(name, rating, singer);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Điểm đánh giá phải là số thực", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void loadSongs(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            songAdapter.setSongs(songDbHelper.getAllSongs());
        } else {
            songAdapter.setSongs(songDbHelper.searchSongsByName(keyword.trim()));
        }
    }

    private void clearInputs() {
        edtSongName.setText("");
        edtSingerName.setText("");
        edtRating.setText("");
    }
}