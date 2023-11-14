package com.ahmedapps.themovies.feature_movie_details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedapps.themovies.feature_movie_details.domain.repository.DetailsRepository
import com.ahmedapps.themovies.feature_movie_details.domain.repository.ExtraDetailsRepository
import com.ahmedapps.themovies.feature_movie_list.data.remote.api.MediaApi.Companion.API_KEY
import com.ahmedapps.themovies.feature_movie_list.domain.repository.MediaRepository
import com.ahmedapps.themovies.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val detailsRepository: DetailsRepository,
    private val extraDetailsRepository: ExtraDetailsRepository
) : ViewModel() {

    var mediaDetailsScreenState by mutableStateOf(MediaDetailsScreenState())

    fun onEvent(event: MediaDetailsScreenEvents) {
        when (event) {
            is MediaDetailsScreenEvents.GoToDetails -> {

                loadMediaItem(
                    type = event.type,
                    category = event.category,
                    id = event.id
                ) {
                    loadDetails(
                        isRefresh = false,
                        type = event.type,
                        id = event.id
                    )

                    loadSimilarMedialList(
                        isRefresh = false,
                        type = event.type,
                        id = event.id
                    )

                    loadVideosList(
                        isRefresh = false,
                        id = event.id
                    )
                }

            }

            is MediaDetailsScreenEvents.Refresh -> {

                loadMediaItem(
                    type = event.type,
                    category = event.category,
                    id = event.id
                ) {
                    loadDetails(
                        isRefresh = true,
                        type = event.type,
                        id = event.id
                    )

                    loadSimilarMedialList(
                        isRefresh = true,
                        type = event.type,
                        id = event.id
                    )

                    loadVideosList(
                        isRefresh = true,
                        id = event.id
                    )
                }

            }
        }
    }

    private fun loadMediaItem(
        type: String,
        category: String,
        id: Int,
        onFinished: () -> Unit
    ) {
        viewModelScope.launch {
            mediaDetailsScreenState.media = mediaRepository.getItem(
                type = type,
                category = category,
                id = id,
            )
            onFinished()
        }
    }

    private fun loadDetails(
        isRefresh: Boolean,
        type: String,
        id: Int,
    ) {

        viewModelScope.launch {

            detailsRepository
                .getDetails(
                    type = type,
                    isRefresh = isRefresh,
                    id = id,
                    apiKey = API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { media ->
                                mediaDetailsScreenState.media?.runtime = media.runtime
                                mediaDetailsScreenState.media?.status = media.status
                                mediaDetailsScreenState.media?.tagline = media.tagline
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaDetailsScreenState = mediaDetailsScreenState.copy(
                                isLoading = result.isLoading
                            )

                        }
                    }
                }
        }
    }

    private fun loadSimilarMedialList(
        isRefresh: Boolean,
        type: String,
        id: Int
    ) {

        viewModelScope.launch {

            extraDetailsRepository
                .getSimilarMediaList(
                    isRefresh = isRefresh,
                    type = type,
                    id = id,
                    page = 1,
                    apiKey = API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { media ->
                                mediaDetailsScreenState = mediaDetailsScreenState.copy(
                                    similarMediaList = media
                                )

                            }
                        }

                        is Resource.Error -> {}

                        is Resource.Loading -> {
                            mediaDetailsScreenState = mediaDetailsScreenState.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
        }
    }

    private fun loadVideosList(
        isRefresh: Boolean,
        id: Int
    ) {

        viewModelScope.launch {

            extraDetailsRepository
                .getVideosList(
                    isRefresh = isRefresh,
                    id = id,
                    apiKey = API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { videosList ->
                                mediaDetailsScreenState = mediaDetailsScreenState.copy(
                                    videosList = videosList
                                )

                            }
                        }

                        is Resource.Error -> {}

                        is Resource.Loading -> {
                            mediaDetailsScreenState = mediaDetailsScreenState.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
        }
    }

}






