package com.ahmedapps.themovies.feature_movie_list.data.remote.respond

import com.ahmedapps.themovies.feature_movie_list.domain.models.Genre

data class GenresListDto(
    val genres: List<Genre>
)