package com.ahmedapps.themovies.feature_movie_details.presentation

import com.ahmedapps.themovies.feature_movie_list.domain.models.Genre
import com.ahmedapps.themovies.feature_movie_list.domain.models.Media


data class MediaDetailsScreenState(

    var isLoading: Boolean = false,

    var media: Media? = null,
    var currentMovieId: Int = 0,
    var videoToPlay: String = "",

    var similarMediaList: List<Media> = emptyList(),
    var videosList: List<String> = emptyList(),
    var moviesGenresList: List<Genre> = emptyList(),
    var tvGenresList: List<Genre> = emptyList()

)