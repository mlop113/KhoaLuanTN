package com.android.Global;

import com.android.API.APIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalController {
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GlobalStaticData.URL_HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static APIService apiService = retrofit.create(APIService.class);
}
