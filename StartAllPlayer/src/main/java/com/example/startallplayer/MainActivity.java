package com.example.startallplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private Button mBtnPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnPlay = findViewById(R.id.play);
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartAllPlayer();
//                getIpInformation();
            }
        });
    }

    public void StartAllPlayer(){
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2016/07/19/mp4/160719095812990 469.mp4"),"video/*");
        startActivity(intent);
    }

    /**
     * 普通GET请求
     */
    private void getIpInformation() {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpService ipService = retrofit.create(IpService.class);
        Call<IpModel> call = ipService.getIpMsg();
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().getCountry();
                Log.i("wangshu", "country" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

}
