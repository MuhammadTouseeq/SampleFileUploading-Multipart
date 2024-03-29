package com.pakdev.fileuploading.api;


import android.content.Context;



import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServiceFactory

{

    public static final Dispatcher dispatcher = new Dispatcher();
    private static WebService mInstance;

    private WebServiceFactory() {
        // Exists only to defeat instantiation.

    }


    public static WebService getInstance(Context activity,String ipaddress) {

        if (mInstance == null) {

// set your desired log level
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpclient = new OkHttpClient.Builder();
//            httpclient.readTimeout(25, TimeUnit.SECONDS);
            /**
             * To avoid time out exception
             */
            httpclient.connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS);


            httpclient.addInterceptor(logging);
            httpclient.dispatcher(dispatcher);


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ipaddress+WebServiceConstants.LIVE_SERVER)
                    .client(httpclient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            mInstance = retrofit.create(WebService.class);

        }
        return mInstance;
    }

}
