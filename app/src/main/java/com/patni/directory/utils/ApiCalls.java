package com.patni.directory.utils;

import com.patni.directory.model.Adds;
import com.patni.directory.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiCalls {

    @Headers({"Accept: application/json"})
    @PUT("user/login")
    Call<User> loginUser(@Body User user);

    @Headers({"Accept: application/json"})
    @PUT
    Call<User> logoutUser(@Body User user);

    @GET("adds/{id}")
    Call<Adds> getAdds(@Path("id") Long id);

}
