package com.example.on_thi_1_4_pdf;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<User> userList;
    private NetworkChangeReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init database
        dbHelper = new DatabaseHelper(this);
        userList = dbHelper.getAllUsers();

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ContactAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        // Search bar - filter by name
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
        });

        // ADD button
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> showAddDialog());

        // DELETE button — deletes all checked contacts
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> deleteSelected());

        // Register BroadcastReceiver for network changes
        networkReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    // ─── Options Menu ────────────────────────────────────────────────────────

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            sortContacts();
            return true;
        } else if (id == R.id.action_add) {
            showAddDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ─── Actions ─────────────────────────────────────────────────────────────

    /** Sort contacts alphabetically by name */
    private void sortContacts() {
        userList = dbHelper.getUsersSortedByName();
        adapter.updateList(userList);
        Toast.makeText(this, "Sorted A–Z", Toast.LENGTH_SHORT).show();
    }

    /** Show dialog to add a new contact */
    private void showAddDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_contact, null);
        EditText etName = dialogView.findViewById(R.id.etDialogName);
        EditText etPhone = dialogView.findViewById(R.id.etDialogPhone);

        new AlertDialog.Builder(this)
                .setTitle("Add Contact")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    if (name.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.insertUser(name, phone, "");
                        refreshList();
                        Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /** Delete all checked contacts from DB and refresh list */
    private void deleteSelected() {
        List<User> selected = adapter.getCheckedUsers();
        if (selected.isEmpty()) {
            Toast.makeText(this, "No contacts selected", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Delete Contacts")
                .setMessage("Delete " + selected.size() + " selected contact(s)?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    for (User user : selected) {
                        dbHelper.deleteUser(user.getId());
                    }
                    refreshList();
                    Toast.makeText(this,
                            "Deleted " + selected.size() + " contact(s)",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshList() {
        userList = dbHelper.getAllUsers();
        adapter.updateList(userList);
    }

    // ─── Lifecycle ────────────────────────────────────────────────────────────

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }
}