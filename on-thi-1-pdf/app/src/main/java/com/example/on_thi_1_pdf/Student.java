package com.example.on_thi_1_pdf;

public class Student {

    private int id;
    private int maSv;
    private String ten;
    private int diemToan;
    private int diemHoa;
    private int diemLy;

    public Student(int maSv, String ten, int diemToan, int diemHoa, int diemLy) {
        this.maSv = maSv;
        this.ten = ten;
        this.diemToan = diemToan;
        this.diemHoa = diemHoa;
        this.diemLy = diemLy;
    }

    public Student(int id, int maSv, String ten, int diemToan, int diemHoa, int diemLy) {
        this.id = id;
        this.maSv = maSv;
        this.ten = ten;
        this.diemToan = diemToan;
        this.diemHoa = diemHoa;
        this.diemLy = diemLy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaSv() {
        return maSv;
    }

    public void setMaSv(int maSv) {
        this.maSv = maSv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getDiemToan() {
        return diemToan;
    }

    public void setDiemToan(int diemToan) {
        this.diemToan = diemToan;
    }

    public int getDiemHoa() {
        return diemHoa;
    }

    public void setDiemHoa(int diemHoa) {
        this.diemHoa = diemHoa;
    }

    public int getDiemLy() {
        return diemLy;
    }

    public void setDiemLy(int diemLy) {
        this.diemLy = diemLy;
    }

    public int getTongDiem() {
        return diemToan + diemHoa + diemLy;
    }
}

