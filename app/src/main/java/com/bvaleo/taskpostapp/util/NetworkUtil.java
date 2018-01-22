package com.bvaleo.taskpostapp.util;

import com.bvaleo.taskpostapp.net.IPostService;
import com.bvaleo.taskpostapp.net.RetrofitClient;

public class NetworkUtil {
    public static IPostService getPostService(){
        return RetrofitClient.getInstance(Constants.BASE_URL).create(IPostService.class);
    }
}