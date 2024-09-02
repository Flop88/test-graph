package com.mvlikhachev.test_task.data.remote

import com.mvlikhachev.test_task.data.remote.model.PointsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApiService {

    @GET("/api/test/points")
    suspend fun getPoints(@Query("count") count: Int): PointsResponse

}