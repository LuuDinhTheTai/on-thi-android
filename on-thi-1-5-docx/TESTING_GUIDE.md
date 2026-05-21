# Android Store App - Testing Guide

## Prerequisites
- Android SDK installed
- Android Emulator or physical Android device (API 24+)
- Android Studio or terminal with Gradle

---

## How to Build and Run

### Option 1: Using Terminal
```bash
# Navigate to project directory
cd D:\Custom\IT3\Android\de-on-thi\project\on-thi-1-5-docx

# Build the app
.\gradlew build

# Create and run on emulator
.\gradlew installDebug
```

### Option 2: Using Android Studio
1. Open project in Android Studio
2. Wait for Gradle sync
3. Click "Run" or press Shift+F10
4. Select emulator/device

---

## Test Cases

### 1. **Load Initial Data**
   ✓ App displays 3 sample products on startup
   ✓ Products: Iphone 15 Promax, TV Sony 75', Iphone 14
   ✓ Prices are visible in purple header section

### 2. **Test Search Functionality**
   - Type "Iphone" in search bar
   - ✓ Shows only Iphone products
   - Type "Sony"
   - ✓ Shows TV Sony 75' product
   - Clear search bar
   - ✓ Shows all products again

### 3. **Test Discount Display**
   - Iphone 15 Promax should show:
     - Price: 14850000 (10% discount applied)
     - Details: "giam gia con 14850000"
   - TV Sony 75' should show:
     - Price: 24750000 (10% discount applied)
     - Details: "giam gia con 24750000"
   - Iphone 14 should show:
     - Price: 15000000 (no discount)
     - Details: "No discount"

### 4. **Test Sort Function**
   - Tap on any product
   - Select "Sort" from menu
   - ✓ Products sorted alphabetically:
     1. Iphone 14
     2. Iphone 15 Promax
     3. TV Sony 75'

### 5. **Test Delete Function**
   - Tap on Iphone 15 Promax (price: 15000000)
   - Select "Delete"
   - ✓ Toast shows "Đã xóa X sản phẩm!"
   - ✓ All products with price < 15000000 are deleted
   - ✓ Iphone 15 Promax remains (same price)
   - TV Sony 75' remains (higher price)

### 6. **Test Update Function**
   - Tap on Iphone 14
   - Select "Update"
   - ✓ Dialog opens with current product info
   - Change name to "New Phone"
   - Toggle discount checkbox (enable discount)
   - Tap "Lưu"
   - ✓ Product updated in list
   - ✓ Price now shows discounted amount
   - ✓ Toast confirms update

### 7. **Test Add Product (FAB)**
   - Tap green FAB button
   - ✓ "Sản phẩm mới được thêm!" Toast appears
   - ✓ "New Product" appears in list

### 8. **Test Network Monitoring**
   - Turn off WiFi or data on device
   - ✓ Toast: "Mất kết nối mạng!" appears
   - Turn on WiFi or data
   - ✓ Toast: "Có kết nối mạng!" appears

### 9. **Test Search After Operations**
   - After adding/updating products
   - Type in search bar
   - ✓ Search still works correctly
   - ✓ Updated products are searchable

---

## Expected Output

### Startup Screen
```
=====================================
       StoreApp (Purple Header)
   [Search Bar for product name]
=====================================
Iphone 15 Promax              14850000
giam gia con 14850000
-----
TV Sony 75'                   24750000
giam gia con 24750000
-----
Iphone 14                     15000000
No discount
-----
                              [+] FAB
=====================================
```

### Update Dialog
```
┌─────────────────────────────────┐
│  Cập nhật sản phẩm               │
├─────────────────────────────────┤
│ [Tên sản phẩm]                  │
│ [Giá]                            │
│ [Chi tiết]                       │
│ [✓] Có giảm giá?                │
│                                  │
│      [Lưu]    [Hủy]             │
└─────────────────────────────────┘
```

---

## Key Features to Verify

- [ ] All 3 sample products load correctly
- [ ] Prices with 10% discount displayed correctly
- [ ] Search filters products by name in real-time
- [ ] Sort arranges products A-Z
- [ ] Delete removes cheaper products
- [ ] Update allows editing all fields
- [ ] Discount toggle works
- [ ] Network notifications appear
- [ ] FAB adds new products
- [ ] Context menu appears on tap

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| App crashes on startup | Check minSdkVersion is 24+ |
| Database not initializing | Clear app data and restart |
| Search not working | Verify text matches product names exactly |
| Network receiver not working | Check permissions in AndroidManifest.xml |
| Context menu not showing | Ensure item is fully visible in RecyclerView |

---

## APK Location
`app/build/outputs/apk/debug/app-debug.apk`

---

Generated: May 21, 2026

