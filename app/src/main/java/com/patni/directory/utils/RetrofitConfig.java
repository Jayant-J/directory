package com.patni.directory.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.10:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    public static ApiCalls apiCalls = retrofit.create(ApiCalls.class);

}
