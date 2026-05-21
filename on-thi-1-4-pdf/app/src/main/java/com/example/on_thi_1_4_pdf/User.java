package com.example.on_thi_1_4_pdf;

public class User {
    private int id;
    private String name;
    private String phone;
    private String image;
    private boolean checked; // checkbox state on UI

    public User(int id, String name, String phone, String image) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.checked = false;
    }

    /** Auto toggle checkbox state */
    public void toggleChecked() {
        this.checked = !this.checked;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
}

