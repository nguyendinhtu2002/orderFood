package com.example.orderfood.Interface;

import com.example.orderfood.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyAPIInterface {

    @GET("users")
    Call<List<User>> getUsers();
}