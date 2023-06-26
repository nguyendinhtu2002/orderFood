package com.example.orderfood.Model;

import java.util.Date;

public class HistoryOrder {
    private String orderId;
    private String userPhone;
    private String deliveryAddress;
    private Date creationDate;
    private String price;
    private String status;

    public HistoryOrder(String userPhone, String deliveryAddress, Date creationDate, String price, String status) {
        this.userPhone = userPhone;
        this.deliveryAddress = deliveryAddress;
        this.creationDate = creationDate;
        this.price = price;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
