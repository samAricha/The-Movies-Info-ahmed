package com.ahmedapps.themovies.feature_movie_details.presentation

sealed class MediaDetailsScreenEvents {

    data class Refresh(
        val id: Int, val type: String, val category: String
    ) : MediaDetailsScreenEvents()

    data class GoToDetails(
        val id: Int, val type: String, val category: String
    ) : MediaDetailsScreenEvents()
}