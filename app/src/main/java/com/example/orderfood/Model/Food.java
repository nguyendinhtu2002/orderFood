package com.example.orderfood.Model;

public class Food {
    private String Name, Image, Description, Price, Discount, MenuId;
    private int ID;

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public Food() {
    }

    public Food(int id, String name, String image, String description, String price, String discount, String menuId) {
        ID = id;
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuId = menuId;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}