package com.ahmedapps.themovies.feature_movie_list.data.remote.respond

data class MediaListDto(
    val page: Int,
    val results: List<MediaDto>,
    val total_pages: Int,
    val total_results: Int
)