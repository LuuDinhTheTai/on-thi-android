package com.example.on_thi_1_3_pdf;

public class Ve {
    private int maVe;
    private String gaDen;
    private String gaDi;
    private float donGia;
    private boolean loaiVe; // true = Khứ Hồi, false = Một Chiều

    public Ve() {}

    public Ve(int maVe, String gaDi, String gaDen, float donGia, boolean loaiVe) {
        this.maVe = maVe;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.donGia = donGia;
        this.loaiVe = loaiVe;
    }

    public int getMaVe() { return maVe; }
    public void setMaVe(int maVe) { this.maVe = maVe; }

    public String getGaDen() { return gaDen; }
    public void setGaDen(String gaDen) { this.gaDen = gaDen; }

    public String getGaDi() { return gaDi; }
    public void setGaDi(String gaDi) { this.gaDi = gaDi; }

    public float getDonGia() { return donGia; }
    public void setDonGia(float donGia) { this.donGia = donGia; }

    public boolean isLoaiVe() { return loaiVe; }
    public void setLoaiVe(boolean loaiVe) { this.loaiVe = loaiVe; }

    /**
     * Tự động tính giá tiền:
     * - Khứ Hồi (loaiVe = true): donGia * 1.9
     * - Một Chiều (loaiVe = false): donGia * 1
     */
    public float tinhGia() {
        if (loaiVe) {
            return donGia * 1.9f;
        } else {
            return donGia;
        }
    }

    public String getLoaiVeText() {
        return loaiVe ? "Khứ Hồi" : "Một Chiều";
    }
}

