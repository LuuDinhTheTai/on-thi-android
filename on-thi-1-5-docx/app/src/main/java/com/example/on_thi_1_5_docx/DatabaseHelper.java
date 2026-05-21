package com.example.on_thi_1_5_docx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "store_app.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "products";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DETAILS = "details";
    public static final String COLUMN_DISCOUNT = "discount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_PRICE + " TEXT NOT NULL, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_DISCOUNT + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);

        // Add sample data
        addSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add sample data
    private void addSampleData(SQLiteDatabase db) {
        Product[] sampleProducts = {
                new Product(1, "Iphone 15 Promax", "15000000", "Giam gia con 14850000", true),
                new Product(2, "TV Sony 75'", "25000000", "giam gia con 24750000", true),
                new Product(3, "Iphone 14", "15000000", "No discount", false)
        };

        for (Product product : sampleProducts) {
            addProduct(db, product);
        }
    }

    // Add product (internal method for initial data)
    private void addProduct(SQLiteDatabase db, Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DETAILS, product.getDetails());
        values.put(COLUMN_DISCOUNT, product.isDiscount() ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
    }

    // Add product (public method)
    public long insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DETAILS, product.getDetails());
        values.put(COLUMN_DISCOUNT, product.isDiscount() ? 1 : 0);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    // Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getString(2));
                product.setDetails(cursor.getString(3));
                product.setDiscount(cursor.getInt(4) == 1);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    // Get product by name (search)
    public List<Product> searchByName(String name) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE name LIKE '%" + name + "%'", null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getString(2));
                product.setDetails(cursor.getString(3));
                product.setDiscount(cursor.getInt(4) == 1);
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    // Update product
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DETAILS, product.getDetails());
        values.put(COLUMN_DISCOUNT, product.isDiscount() ? 1 : 0);
        int result = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
        return result;
    }

    // Delete product by id
    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    // Delete all products with price lower than given price
    public int deleteProductsByPrice(long maxPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_PRICE + " < ?", new String[]{String.valueOf(maxPrice)});
        db.close();
        return result;
    }

    // Sort products by name (A-Z)
    public List<Product> sortByName() {
        List<Product> products = getAllProducts();
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        });
        return products;
    }
}

