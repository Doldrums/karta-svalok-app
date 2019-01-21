package com.teamtwothree.kartasvalokapp.api

import com.teamtwothree.kartasvalokapp.model.point.Point
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface KSApi {

    @GET("port_query/{id}")
    fun getPointDetails(@Path("id") id: String): Deferred<PointDetails>

    @GET("points")
    fun getAllPoints(): Deferred<List<Point>>

    @POST("requests")
    fun postReport(@Body multipartBody: MultipartBody): Deferred<String>

    /* FOR TESTING PURPOSES ONLY */
    @DELETE("cleardb")
    fun deleteAllPoints(): Call<Void>
}