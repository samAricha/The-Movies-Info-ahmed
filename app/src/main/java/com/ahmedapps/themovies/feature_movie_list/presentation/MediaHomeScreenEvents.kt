package com.ahmedapps.themovies.feature_movie_list.presentation

import com.ahmedapps.themovies.feature_movie_list.domain.models.Media

sealed class MediaHomeScreenEvents {
    data class Refresh(val type: String) : MediaHomeScreenEvents()
    object BackOnline : MediaHomeScreenEvents()
    data class OnPaginate(val type: String) : MediaHomeScreenEvents()
    data class OnSearchQueryChanged(val query: String) : MediaHomeScreenEvents()
    data class OnSearchedItemClick(val media: Media) : MediaHomeScreenEvents()
}