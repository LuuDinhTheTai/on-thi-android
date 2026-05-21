package com.example.on_thi_1_3_pdf;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements VeAdapter.OnItemLongClickListener {

    private RecyclerView recyclerView;
    private VeAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextInputEditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        FloatingActionButton fab = findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<Ve> veList = dbHelper.getAllVe();
        adapter = new VeAdapter(veList, this);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                List<Ve> filtered = keyword.isEmpty()
                        ? dbHelper.getAllVe()
                        : dbHelper.searchVe(keyword);
                adapter.setData(filtered);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        fab.setOnClickListener(v -> AddEditActivity.start(this, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        String keyword = etSearch.getText() != null ? etSearch.getText().toString().trim() : "";
        List<Ve> list = keyword.isEmpty() ? dbHelper.getAllVe() : dbHelper.searchVe(keyword);
        adapter.setData(list);
    }

    @Override
    public void onSua(Ve ve) {
        AddEditActivity.start(this, ve);
    }

    @Override
    public void onXoa(Ve ve) {
        dbHelper.deleteVe(ve.getMaVe());
        Toast.makeText(this, "Đã xóa vé", Toast.LENGTH_SHORT).show();
        refreshList();
    }
}