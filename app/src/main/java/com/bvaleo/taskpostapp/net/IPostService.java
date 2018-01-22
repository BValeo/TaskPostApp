package com.bvaleo.taskpostapp.net;

import com.bvaleo.taskpostapp.model.Post;
import com.bvaleo.taskpostapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IPostService {
    @GET("/posts")
    Call<List<Post>> getPosts();

    @GET("/users/{userId}")
    Call<User> getUserById(@Path("userId") int id);
}