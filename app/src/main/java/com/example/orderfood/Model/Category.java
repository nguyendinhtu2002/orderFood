package com.example.orderfood.Model;

public class Category {
    private String Name;
    private String Image;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private int categoryId;


    public Category(){

    }

    public Category(String name, String image,int id ) {
        Name = name;
        Image = image;
        categoryId=id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
