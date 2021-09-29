package com.misfit.ekyc.http;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {
    //1 nrc front
    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("mm_id_card")
    Call<JsonElement> Nrc_first(@Header("Authorization") String apiKey/*, @Header("usersId") String usersId*/, @Body HashMap id);

    //2 nrc back
    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("detect")
    Call<JsonElement> Nrc_back(@Header("Authorization") String apiKey/*, @Header("usersId") String usersId*/, @Body HashMap id);

    //3 token
    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("auth/login-safe")
    Call<JsonElement> gen_token(@Body HashMap id);

    //4 check liveness
    @Multipart
    //@Headers("Content-Type: multipart/form-data")
    @POST("face/liveness")
    Call<JsonElement> get_liveness(@Header("Authorization") String apiKey, @Part MultipartBody.Part file);

    //5 check maches
    @Multipart
    //@Headers("Content-Type: multipart/form-data")
    @POST("face/match")
    Call<JsonElement> get_macthness(@Header("Authorization") String apiKey, @Part MultipartBody.Part file, @Part MultipartBody.Part file2);


    //6 registration
    @Multipart
    //@Headers("Content-Type: multipart/form-data")
    @POST("register")
    Call<JsonElement> send_registration(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file, @Part MultipartBody.Part file2);

    //7 send money
    @Multipart
    //@Headers("Content-Type: multipart/form-data")
    @POST("send_money")
    Call<JsonElement> send_money(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);
}
