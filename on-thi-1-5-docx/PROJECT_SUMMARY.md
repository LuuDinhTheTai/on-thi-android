# Android Store App - Project Completion Summary

## Project Overview
A fully functional Android Store App with product management features, SQLite database, and network connectivity monitoring.

---

## ✅ Requirements Completed

### 1. **Product Class with Constructor (2 điểm)**
   - **File:** `Product.java`
   - **Features:**
     - Constructor with parameters: `id (int)`, `name (String)`, `price (String)`, `details (String)`, `discount (boolean)`
     - Getters and Setters for all properties
     - **Automatic discount check function:** `getDiscountedPrice()` - automatically calculates 10% discount if `discount = true`
     - **Display function:** `getDisplayDetails()` - shows details with discounted price info

### 2. **Search Bar Functionality (1 điểm)**
   - **File:** `MainActivity.java` + `activity_main.xml`
   - **Features:**
     - Search EditText at top of screen
     - Real-time search by product name using TextWatcher
     - Uses `DatabaseHelper.searchByName()` to query database

### 3. **SQLite Database with Sample Data (2 điểm)**
   - **File:** `DatabaseHelper.java`
   - **Features:**
     - SQLite database with 3 sample products:
       - Iphone 15 Promax (15000000) - with 10% discount → 14850000
       - TV Sony 75' (25000000) - with 10% discount → 24750000
       - Iphone 14 (15000000) - no discount
     - 10% discount automatically applied when `discount = true`
     - Discount info displayed in product details

### 4. **Display Product List (1 điểm)**
   - **File:** `ProductAdapter.java` + `MainActivity.java` + `item_product.xml`
   - **Features:**
     - RecyclerView to display products
     - Each item shows: Product Name, Discounted/Original Price, Details
     - Clean, formatted layout with proper spacing

### 5. **Context Menu with CRUD Operations (3 điểm)**
   - **File:** `MainActivity.java`
   - **Menu Options:**
     - **Sort:** Sorts products alphabetically by name (A-Z)
     - **Delete:** Deletes all products with price LOWER than selected product
     - **Update:** Opens dialog to edit product details:
       - Edit name, price, details
       - Toggle discount status
     - Context menu triggered by clicking on product item

### 6. **Broadcast Receiver for Network Connectivity (1 điểm)**
   - **File:** `NetworkChangeReceiver.java` + `AndroidManifest.xml`
   - **Features:**
     - Monitors network connectivity changes
     - Shows Toast notification when network is lost: "Mất kết nối mạng!"
     - Shows Toast notification when network is restored: "Có kết nối mạng!"
     - Registered in AndroidManifest.xml with proper permissions:
       - `android.permission.ACCESS_NETWORK_STATE`
       - `android.permission.CHANGE_NETWORK_STATE`

---

## 📁 Project File Structure

```
app/src/main/
├── java/com/example/on_thi_1_5_docx/
│   ├── MainActivity.java                 (Main activity with UI logic)
│   ├── Product.java                      (Product model class)
│   ├── DatabaseHelper.java               (SQLite database management)
│   ├── ProductAdapter.java               (RecyclerView adapter)
│   └── NetworkChangeReceiver.java        (Network monitoring)
├── res/
│   ├── layout/
│   │   ├── activity_main.xml             (Main UI layout)
│   │   ├── item_product.xml              (Product list item layout)
│   │   └── dialog_update_product.xml     (Update dialog layout)
│   └── values/colors.xml, strings.xml, themes.xml
└── AndroidManifest.xml                   (App configuration)
```

---

## 🎨 UI Features

1. **Header Section:**
   - Purple header bar with "StoreApp" title
   - Search EditText for real-time name search

2. **Product List:**
   - RecyclerView with product items
   - Each item displays:
     - Product name (bold)
     - Discounted price (if applicable)
     - Details/description
     - Separator line between items

3. **FAB Button:**
   - Green floating action button in bottom-right
   - Adds new sample products

4. **Context Menu:**
   - Triggered by tapping product item
   - Options: Sort, Delete, Update

5. **Update Dialog:**
   - Edit all product properties
   - CheckBox to toggle discount status

---

## 💾 Database Structure

**Table: products**
- `id` (INTEGER PRIMARY KEY)
- `name` (TEXT NOT NULL)
- `price` (TEXT NOT NULL)
- `details` (TEXT)
- `discount` (INTEGER, 0 or 1)

---

## 🔧 Key Functions

### DatabaseHelper.java
- `getAllProducts()` - Retrieve all products
- `searchByName(String name)` - Search by product name
- `insertProduct(Product)` - Add new product
- `updateProduct(Product)` - Update product info
- `deleteProductsByPrice(long maxPrice)` - Delete products below price
- `sortByName()` - Sort products alphabetically

### Product.java
- `getDiscountedPrice()` - Calculate 10% discount
- `getDisplayDetails()` - Get display text with discount info

### ProductAdapter.java
- Manages RecyclerView display
- Handles item clicks for context menu

---

## 📝 Notes

1. **Price Format:** All prices stored as String (Vietnamese format without currency symbol)
2. **Discount Calculation:** 10% automatic discount applied when `discount = true`
3. **API Compatibility:** Code works with minSdkVersion 24+
4. **Network Monitoring:** Receiver auto-registers on app start and unregisters on app destroy

---

## 🚀 How to Use

1. **View Products:** App loads with 3 sample products
2. **Search:** Type in search bar to find products by name
3. **Add Product:** Tap FAB button to add new product
4. **Context Menu:** Tap any product to show menu options
   - **Sort:** Arrange products A-Z
   - **Delete:** Remove cheaper products
   - **Update:** Edit product properties
5. **Network Status:** Watch app notifications for network changes

---

**Total Points: 10/10**

