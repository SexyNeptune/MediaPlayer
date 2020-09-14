package com.example.mobileplayer.pager;

//import io.reactivex.rxjava3.core.Observable;
import com.example.mobileplayer.domain.NetMediaItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetMediaService {
    @GET("PageSubArea/TrailerList.api")
    Call<List<NetMediaItem>> getData() ;
}
