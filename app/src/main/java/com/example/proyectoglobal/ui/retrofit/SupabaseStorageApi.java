package com.example.proyectoglobal.ui.retrofit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface SupabaseStorageApi {

    @Multipart
    @POST("storage/v1/object/{bucket}/{fileName}")
    Call<Void> uploadImage(
            @Header("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRxcHlqdHN5Z3FlenJpcXBjcXh2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDEzNDkyMjEsImV4cCI6MjA1NjkyNTIyMX0.bBws1ooiK5RNBZdeJjw41H0cSqTbgozJhXcJa3zYuqw") String authToken,
            @Path("bucket") String bucket,
            @Path("fileName") String fileName,
            @Part MultipartBody.Part file
    );

    @DELETE("storage/v1/object/{bucket}/{fileName}")
    Call<Void> deleteImage(
            @Header("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRxcHlqdHN5Z3FlenJpcXBjcXh2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDEzNDkyMjEsImV4cCI6MjA1NjkyNTIyMX0.bBws1ooiK5RNBZdeJjw41H0cSqTbgozJhXcJa3zYuqw") String authToken,
            @Path("bucket") String bucket,
            @Path("fileName") String fileName
    );


}
