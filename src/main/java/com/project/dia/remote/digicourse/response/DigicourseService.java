package com.project.dia.remote.digicourse.response;

import com.project.dia.remote.digicourse.DigicourseApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Component
public class DigicourseService {
    public static final String BASE_URL = "https://digicourse.id/api_digicoursev2/";

    public static final int CONNECT_TIMEOUT = 60;
    public static final int READ_TIMEOUT = 60;
    public static final int WRITE_TIMEOUT = 60;

    private DigicourseApiService digicourseApiService;

    public DigicourseService() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        digicourseApiService = retrofit.create(DigicourseApiService.class);
    }

    public Call<DigicourseResponse> registerUSer(String email, String password, String institutId) {
        return digicourseApiService.register(email, password);
    }

    public Call<DigicourseResponse> activate(String email) {
        return digicourseApiService.setActive(email);
    }
}
