package com.example.on_thi_1_5_docx;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_SORT   = 1;
    private static final int MENU_DELETE = 2;
    private static final int MENU_UPDATE = 3;

    private DatabaseHelper databaseHelper;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private FloatingActionButton fabAdd;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);
        recyclerView  = findViewById(R.id.product_recycler_view);
        searchBar     = findViewById(R.id.search_bar);
        fabAdd        = findViewById(R.id.fab_add);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, databaseHelper.getAllProducts());
        recyclerView.setAdapter(productAdapter);

        // Long-click item → hiện PopupMenu Sort / Delete / Update
        productAdapter.setOnItemClickListener((product, anchorView) ->
                showPopupMenu(anchorView, product));

        // Tìm kiếm theo tên
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                List<Product> result = keyword.isEmpty()
                        ? databaseHelper.getAllProducts()
                        : databaseHelper.searchByName(keyword);
                productAdapter.updateList(result);
            }
        });

        // FAB thêm sản phẩm mới
        fabAdd.setOnClickListener(v -> {
            showAddDialog();
        });

        // Broadcast Receiver theo dõi mạng
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            registerReceiver(networkChangeReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(networkChangeReceiver, filter);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POPUP MENU – Sort / Delete / Update
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Hiện PopupMenu (Selected Menu) gắn với view item được long-click.
     * Ba lựa chọn: Sort, Delete, Update.
     */
    private void showPopupMenu(View anchorView, Product selectedProduct) {
        PopupMenu popup = new PopupMenu(this, anchorView);

        // Thêm 3 mục menu
        popup.getMenu().add(0, MENU_SORT,   0, "Sort");
        popup.getMenu().add(0, MENU_DELETE, 1, "Delete");
        popup.getMenu().add(0, MENU_UPDATE, 2, "Update");

        popup.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == MENU_SORT) {
                // ── Sort: sắp xếp theo bảng chữ cái (A → Z) ──
                List<Product> sorted = databaseHelper.sortByName();
                productAdapter.updateList(sorted);
                Toast.makeText(this, "Đã sắp xếp theo tên A–Z", Toast.LENGTH_SHORT).show();
                return true;

            } else if (id == MENU_DELETE) {
                // ── Delete: xóa tất cả sản phẩm có giá < giá sản phẩm được chọn ──
                handleDeleteByPrice(selectedProduct);
                return true;

            } else if (id == MENU_UPDATE) {
                // ── Update: mở dialog chỉnh sửa sản phẩm ──
                showUpdateDialog(selectedProduct);
                return true;
            }
            return false;
        });

        popup.show();
    }

    /**
     * Xóa tất cả sản phẩm có giá THẤP HƠN giá của sản phẩm được chọn.
     * Hiển thị xác nhận trước khi xóa.
     */
    private void handleDeleteByPrice(Product selectedProduct) {
        long selectedPrice;
        try {
            selectedPrice = Long.parseLong(selectedProduct.getPrice());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xác nhận trước khi xóa
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Xóa tất cả sản phẩm có giá thấp hơn "
                        + selectedProduct.getName()
                        + " (" + selectedPrice + ")?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int count = databaseHelper.deleteProductsByPrice(selectedPrice);
                    refreshProductList();
                    Toast.makeText(this,
                            "Đã xóa " + count + " sản phẩm có giá dưới " + selectedPrice,
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dialog CẬP NHẬT sản phẩm
    // ─────────────────────────────────────────────────────────────────────────

    private void showUpdateDialog(Product product) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_product, null);

        EditText etName     = dialogView.findViewById(R.id.dialog_product_name);
        EditText etPrice    = dialogView.findViewById(R.id.dialog_product_price);
        EditText etDetails  = dialogView.findViewById(R.id.dialog_product_details);
        CheckBox cbDiscount = dialogView.findViewById(R.id.dialog_product_discount);

        // Điền sẵn dữ liệu hiện tại
        etName.setText(product.getName());
        etPrice.setText(product.getPrice());
        etDetails.setText(product.getDetails());
        cbDiscount.setChecked(product.isDiscount());

        new AlertDialog.Builder(this)
                .setTitle("Cập nhật sản phẩm")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name    = etName.getText().toString().trim();
                    String price   = etPrice.getText().toString().trim();
                    String details = etDetails.getText().toString().trim();
                    boolean disc   = cbDiscount.isChecked();

                    if (name.isEmpty() || price.isEmpty()) {
                        Toast.makeText(this, "Tên và giá không được để trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    product.setName(name);
                    product.setPrice(price);
                    product.setDetails(details);
                    product.setDiscount(disc);

                    databaseHelper.updateProduct(product);
                    refreshProductList();
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dialog THÊM sản phẩm (FAB)
    // ─────────────────────────────────────────────────────────────────────────

    private void showAddDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_product, null);

        EditText etName     = dialogView.findViewById(R.id.dialog_product_name);
        EditText etPrice    = dialogView.findViewById(R.id.dialog_product_price);
        EditText etDetails  = dialogView.findViewById(R.id.dialog_product_details);
        CheckBox cbDiscount = dialogView.findViewById(R.id.dialog_product_discount);

        new AlertDialog.Builder(this)
                .setTitle("Thêm sản phẩm mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name    = etName.getText().toString().trim();
                    String price   = etPrice.getText().toString().trim();
                    String details = etDetails.getText().toString().trim();
                    boolean disc   = cbDiscount.isChecked();

                    if (name.isEmpty() || price.isEmpty()) {
                        Toast.makeText(this, "Tên và giá không được để trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Product newProduct = new Product(0, name, price, details, disc);
                    databaseHelper.insertProduct(newProduct);
                    refreshProductList();
                    Toast.makeText(this, "Đã thêm sản phẩm!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tiện ích
    // ─────────────────────────────────────────────────────────────────────────

    private void refreshProductList() {
        String keyword = searchBar.getText().toString().trim();
        List<Product> list = keyword.isEmpty()
                ? databaseHelper.getAllProducts()
                : databaseHelper.searchByName(keyword);
        productAdapter.updateList(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}

