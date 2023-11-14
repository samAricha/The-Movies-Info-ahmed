package com.ahmedapps.themovies.feature_movie_list.data.local.media

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmedapps.themovies.feature_movie_details.domain.models.Video

@Entity
data class MediaEntity(
    val adult: Boolean? = null,
    val backdrop_path: String? = null,
    val first_air_date: String? = null,
    val genre_ids: String? = null,
    @PrimaryKey val id: Int? = null,
    var media_type: String? = null,
    val origin_country: String? = null,
    val original_language: String? = null,
    val original_name: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null,
    var category: String? = null,
    var runtime: Int? = null,
    var videos: String? = null,
    var status: String? = null,
    var tagline: String? = null,
    var similarMediaList: String? = null,
)
