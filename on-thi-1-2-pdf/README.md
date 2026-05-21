# Song Application (SQLite + Service)

Ung dung Android Java gom cac chuc nang:

- Lop `Song` co constructor va cac thuoc tinh: ma bai hat (`int`), ten (`String`), diem danh gia (`float`), ten ca si (`String`).
- SQLite (`SongDbHelper`) + du lieu mau khi tao DB lan dau.
- Danh sach bai hat bang `RecyclerView`.
- Tim kiem theo ten bai hat (`SearchView`).
- Them/Sua bai hat trong `MainActivity`.
- Service phat nhac chuong (`RingtoneService`) voi man hinh dieu khien `Play/Stop` (`RingtoneActivity`).

## Cac file chinh

- `app/src/main/java/com/example/on_thi_1_2pdf/Song.java`
- `app/src/main/java/com/example/on_thi_1_2pdf/SongDbHelper.java`
- `app/src/main/java/com/example/on_thi_1_2pdf/SongAdapter.java`
- `app/src/main/java/com/example/on_thi_1_2pdf/MainActivity.java`
- `app/src/main/java/com/example/on_thi_1_2pdf/RingtoneActivity.java`
- `app/src/main/java/com/example/on_thi_1_2pdf/RingtoneService.java`

## Chay nhanh

```powershell
cd "D:\Custom\IT3\Android\de-on-thi\project\on-thi-1-2-pdf"
.\gradlew.bat assembleDebug
```

## Kiem tra unit test

```powershell
cd "D:\Custom\IT3\Android\de-on-thi\project\on-thi-1-2-pdf"
.\gradlew.bat test
```

