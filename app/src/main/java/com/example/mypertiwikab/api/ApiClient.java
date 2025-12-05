package com.example.mypertiwikab.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // ⚠️ CEK IP DI SINI: Pastikan sama dengan hasil ipconfig di laptop
    private static final String BASE_URL = "http://10.221.36.10/api_tk/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // --- ATUR TIMEOUT AGAR UPLOAD FOTO TIDAK GAGAL ---
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS) // 1 Menit
                .readTimeout(60, TimeUnit.SECONDS)    // 1 Menit
                .writeTimeout(60, TimeUnit.SECONDS)   // 1 Menit
                .build();

        // --- ATUR GSON AGAR TIDAK REWEL (LENIENT) ---
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Pakai Gson yang sudah di-set
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}