package com.example.on_thi_1_pdf;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchView svSearch;
    private ListView lvStudents;
    private StudentDbHelper dbHelper;
    private StudentAdapter studentAdapter;
    private final List<Student> students = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        dbHelper = new StudentDbHelper(this);
        dbHelper.seedSampleDataIfEmpty();

        svSearch = findViewById(R.id.svSearch);
        lvStudents = findViewById(R.id.lvStudents);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        studentAdapter = new StudentAdapter(this, students);
        lvStudents.setAdapter(studentAdapter);
        registerForContextMenu(lvStudents);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadStudents(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadStudents(newText);
                return true;
            }
        });

        fabAdd.setOnClickListener(v -> showStudentDialog(null));
        loadStudents("");
    }

    private void loadStudents(String keyword) {
        List<Student> data = dbHelper.getStudentsByName(keyword);
        students.clear();
        students.addAll(data);
        studentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        try {
            menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            return super.onContextItemSelected(item);
        }

        Student selectedStudent = studentAdapter.getItem(menuInfo.position);

        int itemId = item.getItemId();
        if (itemId == R.id.action_edit) {
            showStudentDialog(selectedStudent);
            return true;
        }
        if (itemId == R.id.action_delete) {
            showDeleteConfirmDialog();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete this contact!")
                .setMessage("Bạn có muốn xoá tất cả các học sinh có tổng điểm nhỏ hơn 25 không?")
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("YES", (dialog, which) -> {
                    int deletedRows = dbHelper.deleteStudentsByTotalLessThan(25);
                    loadStudents(svSearch.getQuery().toString());
                    Toast.makeText(
                            this,
                            "Đã xoá " + deletedRows + " sinh viên.",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .show();
    }

    private void showStudentDialog(Student editingStudent) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_student, null);

        EditText edtMaSv = dialogView.findViewById(R.id.edtMaSv);
        EditText edtTen = dialogView.findViewById(R.id.edtTen);
        EditText edtToan = dialogView.findViewById(R.id.edtToan);
        EditText edtHoa = dialogView.findViewById(R.id.edtHoa);
        EditText edtLy = dialogView.findViewById(R.id.edtLy);

        if (editingStudent != null) {
            edtMaSv.setText(String.valueOf(editingStudent.getMaSv()));
            edtTen.setText(editingStudent.getTen());
            edtToan.setText(String.valueOf(editingStudent.getDiemToan()));
            edtHoa.setText(String.valueOf(editingStudent.getDiemHoa()));
            edtLy.setText(String.valueOf(editingStudent.getDiemLy()));
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(editingStudent == null ? "Thêm sinh viên" : "Sửa sinh viên")
                .setView(dialogView)
                .setNegativeButton("Huỷ", (d, which) -> d.dismiss())
                .setPositiveButton("Lưu", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String maSvText = edtMaSv.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String toanText = edtToan.getText().toString().trim();
            String hoaText = edtHoa.getText().toString().trim();
            String lyText = edtLy.getText().toString().trim();

            if (TextUtils.isEmpty(maSvText) || TextUtils.isEmpty(ten)
                    || TextUtils.isEmpty(toanText) || TextUtils.isEmpty(hoaText)
                    || TextUtils.isEmpty(lyText)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int maSv = Integer.parseInt(maSvText);
                int diemToan = Integer.parseInt(toanText);
                int diemHoa = Integer.parseInt(hoaText);
                int diemLy = Integer.parseInt(lyText);

                Student student = new Student(maSv, ten, diemToan, diemHoa, diemLy);
                boolean success;

                if (editingStudent == null) {
                    success = dbHelper.insertStudent(student);
                } else {
                    student.setId(editingStudent.getId());
                    success = dbHelper.updateStudent(student);
                }

                if (success) {
                    loadStudents(svSearch.getQuery().toString());
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Lưu thất bại. Mã sinh viên có thể đã tồn tại.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Điểm và mã sinh viên phải là số nguyên", Toast.LENGTH_SHORT).show();
            }
        }));

        dialog.show();
    }
}