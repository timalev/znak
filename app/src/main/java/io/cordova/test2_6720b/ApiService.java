package io.cordova.test2_6720b;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("uploadphoto.php") // Путь к скрипту на сервере
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part file);
}

// Класс для ответа сервера
class UploadResponse {
    String status;
    String url; // Ссылка, которую вернет ваш PHP
}