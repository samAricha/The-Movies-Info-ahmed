package com.ahmedapps.themovies.feature_movie_list.data.mappers

import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.feature_movie_list.data.local.media.MediaEntity
import com.ahmedapps.themovies.feature_movie_list.data.remote.respond.MediaDto
import com.ahmedapps.themovies.feature_movie_list.domain.models.Media

fun MediaEntity.toMedia(
    type: String,
    category: String
): Media {
    return Media(
        backdrop_path = backdrop_path ?: Constants.unavailable,
        original_language = original_language ?: Constants.unavailable,
        overview = overview ?: Constants.unavailable,
        poster_path = poster_path ?: Constants.unavailable,
        release_date = release_date ?: first_air_date ?: Constants.unavailable,
        title = title ?: Constants.unavailable,
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        genre_ids = try {
            genre_ids?.split(",")!!.map { it.toInt() }
        } catch (e: Exception) {
            listOf(12, 22)
        },
        id = id ?: 1,
        adult = adult ?: false,
        media_type = type,
        origin_country = try {
            origin_country?.split(",")!!.map { it }
        } catch (e: Exception) {
            listOf("12", "22")
        },
        original_title = original_title ?: original_name ?: Constants.unavailable,
        category = category,
        runtime = runtime ?: 0,
        status = status ?: "",
        tagline = tagline ?: "",
        videos = try {
            videos?.split(",")?.map { it }
        } catch (e: Exception) {
            listOf("-1", "-2")
        },
//        cast = cast,
        similarMediaList = try {
            similarMediaList?.split(",")!!.map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        },
    )
}

fun MediaDto.toMediaEntity(
    type: String,
    category: String,
): MediaEntity {
    return MediaEntity(
        backdrop_path = backdrop_path ?: Constants.unavailable,
        original_language = original_language ?: Constants.unavailable,
        overview = overview ?: Constants.unavailable,
        poster_path = poster_path ?: Constants.unavailable,
        release_date = release_date,
        title = title ?: name ?: Constants.unavailable,
        original_name = original_name ?: Constants.unavailable,
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        genre_ids = try {
            genre_ids?.joinToString(",")
        } catch (e: Exception) {
            "12,22"
        },
        id = id ?: 1,
        adult = adult ?: false,
        media_type = type,
        category = category,
        origin_country = try {
            origin_country?.joinToString(",")
        } catch (e: Exception) {
            "12,22"
        },
        original_title = original_title ?: original_name ?: Constants.unavailable,
        videos = try {
            videos?.joinToString(",")
        } catch (e: Exception) {
            "12,22"
        },
//        cast = cast,
        similarMediaList = try {
            similarMediaList?.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
    )
}


fun MediaDto.toMedia(
    type: String,
    category: String,
): Media {
    return Media(
        backdrop_path = backdrop_path ?: Constants.unavailable,
        original_language = original_language ?: Constants.unavailable,
        overview = overview ?: Constants.unavailable,
        poster_path = poster_path ?: Constants.unavailable,
        release_date = release_date ?: Constants.unavailable,
        title = title ?: name ?: Constants.unavailable,
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        genre_ids = genre_ids ?: emptyList(),
        id = id ?: 1,
        adult = adult ?: false,
        media_type = type,
        category = category,
        origin_country = origin_country ?: emptyList(),
        original_title = original_title ?: original_name ?: Constants.unavailable,
        runtime = null,
        status = null,
        tagline = null,
        videos = videos,
//        cast = cast,
        similarMediaList = similarMediaList ?: emptyList()
    )
}

fun Media.toMediaEntity(): MediaEntity {
    return MediaEntity(
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        genre_ids = try {
            genre_ids.joinToString(",")
        } catch (e: Exception) {
            "12,22"
        },
        id = id,
        adult = adult,
        media_type = media_type,
        origin_country = try {
            origin_country.joinToString(",")
        } catch (e: Exception) {
            "12,22"
        },
        original_title = original_title,
        category = category,
        videos = try {
            videos?.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
//        cast = cast,
        similarMediaList = try {
            similarMediaList.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
    )
}






