package com.diamond.avenue.retrofit;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface MoreLessonsService {
    @GET("/articles.json")
    Call<MoreLessons> getLessons();
}
