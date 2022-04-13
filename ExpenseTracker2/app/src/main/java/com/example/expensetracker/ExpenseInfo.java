package com.example.expensetracker;

public class ExpenseInfo {
    private int id;
    private String name;
    private String price;
    private String date;
    private String category;

    public ExpenseInfo(int id, String name, String price, String date, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.category = category;
    }

    public ExpenseInfo() {
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
