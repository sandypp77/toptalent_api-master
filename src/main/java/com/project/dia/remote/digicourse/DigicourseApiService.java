package com.project.dia.remote.digicourse;

import com.project.dia.remote.digicourse.response.DigicourseResponse;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.Call;
import retrofit2.http.*;

public interface DigicourseApiService {
    @FormUrlEncoded
    @POST("user/auth/create_user_toptalent")
    Call<DigicourseResponse> register(@Field("email") String email,
                                      @Field("password") String password);

    @GET("user/auth/verify_toptalent")
    Call<DigicourseResponse> setActive(@Query("email") String email);
}
