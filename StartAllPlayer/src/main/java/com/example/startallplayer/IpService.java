package com.example.startallplayer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface IpService {
    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent: MoonRetrofit"
    })
    @GET("getIpInfo.php?ip=183.236.19.184")
    Call<IpModel> getIpMsg();
}
