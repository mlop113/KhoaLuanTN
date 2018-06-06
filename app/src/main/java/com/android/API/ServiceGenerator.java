package com.android.API;

import com.android.Global.GlobalStaticData;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator{
        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(GlobalStaticData.URL_HOST)
                        .addConverterFactory(GsonConverterFactory.create());

        private static Retrofit retrofit = builder.build();

        private static OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        public static <S> S createService(
                Class<S> serviceClass) {
            return retrofit.create(serviceClass);
        }
}
