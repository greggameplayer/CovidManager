package com.epsi.covidmanager.model.webservice;

import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {

    public static String sendGetOkHttpRequest(String url) throws Exception{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if(response.code() != HttpURLConnection.HTTP_OK){
            throw new Exception("Response incorrecte : " + response.code());
        }
        else {
            return response.body().string();
        }

    }
}
