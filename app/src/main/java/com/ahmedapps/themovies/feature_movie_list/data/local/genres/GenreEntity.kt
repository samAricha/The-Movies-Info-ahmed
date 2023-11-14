package com.ahmedapps.themovies.feature_movie_list.data.local.genres

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String
)