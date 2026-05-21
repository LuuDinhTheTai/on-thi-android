package com.example.on_thi_1_docx;

public class NhaHang {
    private static int nextId = 1;

    private int maNhaHang;
    private String ten;
    private String diaChi;
    private double danhGia;

    public NhaHang(String ten, String diaChi, double danhGia) {
        this.maNhaHang = nextId++;
        this.ten = ten;
        this.diaChi = diaChi;
        this.danhGia = danhGia;
    }

    public NhaHang(int maNhaHang, String ten, String diaChi, double danhGia) {
        this.maNhaHang = maNhaHang;
        this.ten = ten;
        this.diaChi = diaChi;
        this.danhGia = danhGia;
        if (maNhaHang >= nextId) {
            nextId = maNhaHang + 1;
        }
    }

    public int getMaNhaHang() {
        return maNhaHang;
    }

    public String getTen() {
        return ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public double getDanhGia() {
        return danhGia;
    }
}

