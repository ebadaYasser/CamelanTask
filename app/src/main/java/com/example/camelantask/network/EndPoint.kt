package com.example.camelantask.network

import com.example.camelantask.entities.images.ImageResponse
import com.example.camelantask.entities.places.PlacesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EndPoint {
    @GET("venues/explore?")
    fun getPlaces(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("ll") longLat: String,
        @Query("limit") limit: Int,
        @Query("radius") radius: Int,
        @Query("v") version: String
    ): Single<PlacesResponse>


    @GET("venues/{id}/photos?")
    fun getPhoto(
        @Path("id") id: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") version: String
    ): Single<ImageResponse>
}