package com.ahmedapps.themovies.feature_movie_list.data.repository

import com.ahmedapps.themovies.feature_movie_list.data.local.media.MediaDatabase
import com.ahmedapps.themovies.feature_movie_list.data.mappers.toMedia
import com.ahmedapps.themovies.feature_movie_list.data.mappers.toMediaEntity
import com.ahmedapps.themovies.feature_movie_list.data.remote.api.MediaApi
import com.ahmedapps.themovies.feature_movie_list.domain.models.Media
import com.ahmedapps.themovies.feature_movie_list.domain.repository.MediaRepository
import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.util.Constants.TRENDING
import com.ahmedapps.themovies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val mediaApi: MediaApi,
    mediaDb: MediaDatabase
) : MediaRepository {

    private val mediaDao = mediaDb.mediaDao

    override suspend fun insertItem(media: Media) {
        val mediaEntity = media.toMediaEntity()

        mediaDao.insertMediaItem(
            mediaItem = mediaEntity
        )
    }

    override suspend fun getItem(
        id: Int,
        type: String,
        category: String,
    ): Media {
        return mediaDao.getMediaById(id).toMedia(
            category = category,
            type = type
        )
    }

    override suspend fun updateItem(media: Media) {
        val mediaEntity = media.toMediaEntity()

        mediaDao.updateMediaItem(
            mediaItem = mediaEntity
        )

    }

    override suspend fun getMoviesAndTvSeriesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        category: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMediaList = mediaDao.getMediaListByTypeAndCategory(type, category)

            val shouldJustLoadFromCache =
                localMediaList.isNotEmpty() && !fetchFromRemote && !isRefresh
            if (shouldJustLoadFromCache) {

                emit(Resource.Success(
                    data = localMediaList.map {
                        it.toMedia(
                            type = type,
                            category = category
                        )
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteMediaByTypeAndCategory(type, category)
                searchPage = 1
            }

            val remoteMediaList = try {
                mediaApi.getMoviesAndTvSeriesList(
                    type, category, searchPage, apiKey
                ).results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteMediaList?.let { mediaList ->
                val media = mediaList.map {
                    it.toMedia(
                        type = type,
                        category = category
                    )
                }

                val entities = mediaList.map {
                    it.toMediaEntity(
                        type = type,
                        category = category,
                    )
                }

                mediaDao.insertMediaList(entities)

                emit(
                    Resource.Success(
                        data = media
                    )
                )
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getTrendingList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        time: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMediaList = mediaDao.getTrendingMediaList(TRENDING)

            val shouldJustLoadFromCache = localMediaList.isNotEmpty() && !fetchFromRemote
            if (shouldJustLoadFromCache) {

                emit(Resource.Success(
                    data = localMediaList.map {
                        it.toMedia(
                            type = it.media_type ?: Constants.unavailable,
                            category = TRENDING
                        )
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page

            if (isRefresh) {
                mediaDao.deleteTrendingMediaList(TRENDING)
                searchPage = 1
            }

            val remoteMediaList = try {
                mediaApi.getTrendingList(
                    type, time, searchPage, apiKey
                ).results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteMediaList?.let { mediaList ->

                val media = mediaList.map {
                    it.toMedia(
                        type = it.media_type ?: Constants.unavailable,
                        category = TRENDING
                    )
                }

                val entities = mediaList.map {
                    it.toMediaEntity(
                        type = it.media_type ?: Constants.unavailable,
                        category = TRENDING
                    )
                }

                mediaDao.insertMediaList(entities)

                emit(
                    Resource.Success(
                        data = media
                    )
                )
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getSearchList(
        fetchFromRemote: Boolean,
        query: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {

        return flow {
            emit(Resource.Loading(true))

            val remoteMediaList = try {
                mediaApi.getSearchList(query, page, apiKey).results.map { media ->
                    media.toMedia(
                        type = media.media_type ?: Constants.unavailable,
                        category = media.category ?: Constants.unavailable
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            emit(Resource.Success(remoteMediaList))

            emit(Resource.Loading(false))
        }
    }
}










