package com.ahmedapps.themovies.feature_movie_details.data.remote.api

import com.ahmedapps.themovies.feature_movie_details.data.remote.respond.cast.CastList
import com.ahmedapps.themovies.feature_movie_details.data.remote.respond.details.DetailsDto
import com.ahmedapps.themovies.feature_movie_details.data.remote.respond.videos.VideosList
import com.ahmedapps.themovies.feature_movie_list.data.remote.respond.MediaListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExtraDetailsApi {

    @GET("{type}/{id}")
    suspend fun getDetails(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): DetailsDto

    @GET("{type}/{id}/similar")
    suspend fun getSimilarMediaList(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MediaListDto

    @GET("{type}/{id}/credits")
    suspend fun getCastList(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): CastList

    @GET("{type}/{id}/videos")
    suspend fun getVideosList(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): VideosList

}