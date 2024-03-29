package com.example.kanokkornthepburi.newhcvvoice.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public interface Service {
    @POST("check_user.php")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("login_type") String type, @Field("username") String username, @Field("password") String password, @Field("keyword") String keyword);

    @POST("show_status_for_android.php")
    @FormUrlEncoded
    Call<DevicesResponse> devices(@Field("controllerName") String controllerName);

    @GET("show_microcontroller_list.php")
    Call<MicroResponse> microllers();

    @POST("add_microcontroller.php")
    @FormUrlEncoded
    Call<ActionResponse> addMicroller(@Field("microController") String name, @Field("microControllerTH") String nameThai);

    @POST("delete_microcontroller.php")
    @FormUrlEncoded
    Call<ActionResponse> deleteMicroller(@Field("id") int id);

    @POST("add_device.php")
    @FormUrlEncoded
    Call<ActionResponse> addDevice(@Field("microController") String microControllerName, @Field("deviceName") String nameEng, @Field("deviceNameTH") String deviceNameTh);

    @POST("edit_device.php")
    @FormUrlEncoded
    Call<ActionResponse> editDevice(@Field("id") int id, @Field("deviceName") String nameEng, @Field("deviceNameTH") String nameThai);

    @POST("show_history_dynamic.php")
    @FormUrlEncoded
    Call<HistoryResponse> getHistory(@Field("Type") String type, @Field("Controller") String microController);
}