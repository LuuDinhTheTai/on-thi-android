package com.example.on_thi_1_5_docx;

public class Product {
    private int id;
    private String name;
    private String price;
    private String details;
    private boolean discount;

    public Product(int id, String name, String price, String details, boolean discount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.discount = discount;
    }

    public Product() {
    }

    // Getter and Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    // Method to check discount and return discounted price
    public String getDiscountedPrice() {
        if (discount) {
            try {
                long originalPrice = Long.parseLong(price);
                long discountedPrice = (long) (originalPrice * 0.9);
                return String.valueOf(discountedPrice);
            } catch (NumberFormatException e) {
                return price;
            }
        }
        return price;
    }

    // Method to get display details with discount info
    public String getDisplayDetails() {
        String detail = details;
        if (discount) {
            try {
                long originalPrice = Long.parseLong(price);
                long discountedPrice = (long) (originalPrice * 0.9);
                long saved = originalPrice - discountedPrice;
                detail += "\ngiam gia con " + discountedPrice;
            } catch (NumberFormatException e) {
                // Keep original details
            }
        }
        return detail;
    }
}

