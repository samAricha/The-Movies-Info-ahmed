package com.ahmedapps.themovies.feature_movie_list.domain.repository

import com.ahmedapps.themovies.util.Resource
import com.ahmedapps.themovies.feature_movie_list.domain.models.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

    suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String,
        apiKey: String
    ): Flow<Resource<List<Genre>>>

}










