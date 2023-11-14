package com.ahmedapps.themovies.feature_movie_list.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ahmedapps.themovies.feature_movie_list.domain.models.Genre
import com.ahmedapps.themovies.feature_movie_list.domain.models.Media

data class MediaHomeScreenState(

    var popularMoviesPage: Int = 1,
    var topRatedMoviesPage: Int = 1,
    var nowPlayingMoviesPage: Int = 1,

    var popularTvSeriesPage: Int = 1,
    var topRatedTvSeriesPage: Int = 1,

    var trendingAllPage: Int = 1,
    var searchPage: Int = 1,

    var isLoading: Boolean = false,
    var isRefreshing: Boolean = false,
    var areListsToBuildSpecialListEmpty: Boolean = true,

    var searchQuery: String = "",

    var previousConnectivityState: MutableState<String> = mutableStateOf(""),
    var backOnlineStarted: Boolean = false,

    var popularMoviesList: List<Media> = emptyList(),
    var topRatedMoviesList: List<Media> = emptyList(),
    var nowPlayingMoviesList: List<Media> = emptyList(),

    var popularTvSeriesList: List<Media> = emptyList(),
    var topRatedTvSeriesList: List<Media> = emptyList(),


    var trendingAllList: List<Media> = emptyList(),
    var searchList: List<Media> = emptyList(),

    // popularTvSeriesList + topRatedTvSeriesList
    var tvSeriesList: List<Media> = emptyList(),

    // topRatedTvSeriesList + topRatedMoviesList
    var topRatedAllList: List<Media> = emptyList(),


    // nowPlayingTvSeriesList + nowPlayingMoviesList
    var recommendedAllList: List<Media> = emptyList(),


    // matching items in:
    // recommendedAllList and trendingAllList
    var specialList: List<Media> = emptyList(),

    var moviesGenresList: List<Genre> = emptyList(),
    var tvGenresList: List<Genre> = emptyList(),

)