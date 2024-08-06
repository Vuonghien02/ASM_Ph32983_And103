package com.duyle.asm1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    String DOMAIN = "http://10.0.2.2:3000/";

    @GET("/api/list")
    Call<List<CarModel>> getCars();

    @POST("/api/add_xe")
    Call<CarModel> addCar(@Body CarModel carModel);
    @DELETE("/api/delete/{id}")
    Call<Void> deleteCar(@Path("id") String carId);
    @PUT("/api/update/{id}")
    Call<CarModel> updateCar(@Path("id") String carId, @Body CarModel car);


}

