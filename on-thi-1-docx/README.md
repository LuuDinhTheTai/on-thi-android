# NhaHangDemo

Ung dung Android Java + SQLite quan ly danh sach nha hang.

## Chuc nang

- Model `NhaHang` co constructor va id tu dong tang.
- Luu/lay du lieu bang SQLite (`DatabaseHelper`).
- Hien thi danh sach bang `RecyclerView`.
- Nhan nut `+` de them du lieu mau va hien Toast "Them du lieu thanh cong.".
- Tim kiem theo ten nha hang ngay tren thanh search.
- Nhan giu mot dong de hien `AlertDialog`, neu chon OK se xoa tat ca nha hang co diem danh gia thap hon dong dang chon.
- Broadcast Receiver thong bao Toast khi mat ket noi mang.

## Chay nhanh

1. Mo project trong Android Studio.
2. Dong bo Gradle.
3. Run app tren emulator/thiet bi Android.

## Kiem tra bang command line

```powershell
Set-Location "D:\Custom\IT3\Android\de-on-thi\project\on-thi-1-docx"
.\gradlew.bat testDebugUnitTest
```

