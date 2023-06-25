package com.example.orderfood.Model;

import java.util.Date;
import java.util.List;

public class HistoryOrder {
    private String orderId;
    private String userPhone;
    private String deliveryAddress;
    private Date creationDate;
    private String Price;

    public HistoryOrder( String userPhone, String deliveryAddress, Date creationDate, String price) {
        this.userPhone = userPhone;
        this.deliveryAddress = deliveryAddress;
        this.creationDate = creationDate;
        Price = price;
    }


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
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
    public Date getCreationDate() {
        return creationDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


}