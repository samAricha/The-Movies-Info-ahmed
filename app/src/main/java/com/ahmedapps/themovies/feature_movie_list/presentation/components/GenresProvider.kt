package com.ahmedapps.themovies.feature_movie_list.presentation.components

import androidx.compose.runtime.Composable
import com.ahmedapps.themovies.feature_movie_list.domain.models.Genre

@Composable
fun genresProvider(
    genre_ids: List<Int>,
    allGenres: List<Genre>
): String {


    var genres = ""

    for (i in allGenres.indices) {
        for (j in genre_ids.indices) {
            if (allGenres[i].id == genre_ids[j]) {
                genres += "${allGenres[i].name} - "
            }
        }
    }

    return genres.dropLastWhile { it == ' ' || it == '-' }
}













