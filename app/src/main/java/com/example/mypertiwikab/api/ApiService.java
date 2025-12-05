package com.example.mypertiwikab.api;

import com.example.mypertiwikab.model.AbsenRequest;
import com.example.mypertiwikab.model.StatusAbsenResponse;
import com.example.mypertiwikab.model.DefaultResponse;
import com.example.mypertiwikab.model.IzinRequest;
import com.example.mypertiwikab.model.LoginRequest;
import com.example.mypertiwikab.model.LoginResponse;
import com.example.mypertiwikab.model.ProfileResponse;
import com.example.mypertiwikab.model.IzinStatusResponse;
import com.example.mypertiwikab.model.OtpResponse; // <-- TAMBAHAN WAJIB

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Multipart;
import retrofit2.http.Query;

public interface ApiService {

    // ===============================
    //  AUTENTIKASI
    // ===============================

    @POST("login_guru.php")
    Call<LoginResponse> loginGuru(@Body LoginRequest request);

    // Register 3 Data (Nama, Email, Password)
    @FormUrlEncoded
    @POST("register_guru.php")
    Call<DefaultResponse> registerGuru(
            @Field("fullname") String fullname,
            @Field("email") String email,
            @Field("password") String password
    );

    // ===============================
    //  ABSENSI
    // ===============================

    // Endpoint untuk kirim absen (Datang/Pulang/Alfa)
    @POST("absensi.php")
    Call<DefaultResponse> postAbsen(@Body AbsenRequest request);

    // Endpoint untuk cek status harian (Hadir/Terlambat/Alfa)
    @GET("get_status_hari_ini.php")
    Call<StatusAbsenResponse> getStatusHariIni(@Query("guru_id") int guruId);

    // ===============================
    //  IZIN
    // ===============================

    // Endpoint untuk mengajukan izin
    @POST("pengajuan_izin.php")
    Call<DefaultResponse> submitIzin(@Body IzinRequest request);

    // Endpoint untuk cek status izin (Diterima/Menunggu)
    @GET("get_izin_status.php")
    Call<IzinStatusResponse> getIzinStatus(@Query("guru_id") int guruId);

    // ===============================
    //  PROFIL
    // ===============================

    @GET("get_profile.php")
    Call<ProfileResponse> getProfile(@Query("guru_id") int guruId);

    @Multipart
    @POST("update_profile.php")
    Call<DefaultResponse> updateProfile(
            @Part("guru_id") RequestBody guruId,
            @Part("fullname") RequestBody fullname,
            @Part("jabatan") RequestBody jabatan,
            @Part("nisn") RequestBody nisn,
            @Part("alamat") RequestBody alamat,
            @Part MultipartBody.Part photo
    );

    // ===============================
    //  OTP / LUPA PASSWORD (Fixed Response Type)
    // ===============================

    // Endpoint untuk meminta OTP (Mengembalikan Guru ID)
    @FormUrlEncoded
    @POST("send_otp.php")
    Call<OtpResponse> sendOtpRequest(@Field("email") String email); // <-- PAKAI OtpResponse

    // Endpoint untuk memverifikasi kode OTP
    @FormUrlEncoded
    @POST("verify_otp.php")
    Call<OtpResponse> verifyOtpRequest( // <-- PAKAI OtpResponse
                                        @Field("guru_id") int guruId,
                                        @Field("otp_code") String otpCode
    );
}