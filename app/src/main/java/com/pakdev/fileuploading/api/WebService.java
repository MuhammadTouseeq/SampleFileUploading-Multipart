package com.pakdev.fileuploading.api;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


// To do : make callbacks


public interface WebService {

    @Multipart
    @POST("MyPath")
    Call<ResponseBody> uploadFileToServer(
            @Part MultipartBody.Part profile_picture);
}
