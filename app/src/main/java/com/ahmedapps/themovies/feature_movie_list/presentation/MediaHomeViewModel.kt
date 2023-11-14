package com.ahmedapps.themovies.feature_movie_list.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedapps.themovies.feature_movie_list.data.remote.api.MediaApi.Companion.API_KEY
import com.ahmedapps.themovies.feature_movie_list.domain.models.Media
import com.ahmedapps.themovies.feature_movie_list.domain.repository.GenreRepository
import com.ahmedapps.themovies.feature_movie_list.domain.repository.MediaRepository
import com.ahmedapps.themovies.util.ConnectivityObserver
import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.util.Constants.ALL
import com.ahmedapps.themovies.util.Constants.MOVIE
import com.ahmedapps.themovies.util.Constants.NOW_PLAYING
import com.ahmedapps.themovies.util.Constants.POPULAR
import com.ahmedapps.themovies.util.Constants.TOP_RATED
import com.ahmedapps.themovies.util.Constants.TRENDING_TIME
import com.ahmedapps.themovies.util.Constants.TV
import com.ahmedapps.themovies.util.NetworkConnectivityObserver
import com.ahmedapps.themovies.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaHomeViewModel @Inject constructor(
    application: Application,
    private val mediaRepository: MediaRepository,
    private val genreRepository: GenreRepository
) : AndroidViewModel(application) {

    var mediaScreenState by mutableStateOf(MediaHomeScreenState())
    private var searchJob: Job? = null


    var connectivityObserver: ConnectivityObserver
    val showSplashScreen = mutableStateOf(true)

    init {
        connectivityObserver = NetworkConnectivityObserver(application)
        load()
        viewModelScope.launch {
            delay(500)
            showSplashScreen.value = false
        }
    }

    private fun load(fetchFromRemote: Boolean = false) {
        loadPopularMovies(fetchFromRemote)
        loadTopRatedMovies(fetchFromRemote)
        loadNowPlayingMovies(fetchFromRemote)

        loadTopRatedTvSeries(fetchFromRemote)
        loadPopularTvSeries(fetchFromRemote)

        loadTrendingAll(fetchFromRemote)

        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = true
        )
        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = false
        )
    }


    fun onEvent(event: MediaHomeScreenEvents) {
        when (event) {

            is MediaHomeScreenEvents.OnSearchedItemClick -> {
                viewModelScope.launch {
                    mediaRepository.insertItem(event.media)
                }
            }

            is MediaHomeScreenEvents.BackOnline -> {
                if (mediaScreenState.moviesGenresList.isEmpty()) {
                    loadGenres(
                        fetchFromRemote = true,
                        isMovies = true
                    )
                }

                if (mediaScreenState.tvGenresList.isEmpty()) {
                    loadGenres(
                        fetchFromRemote = true,
                        isMovies = false
                    )
                }

                if (mediaScreenState.popularMoviesList.isEmpty()) {
                    loadPopularMovies(true)
                }

                if (mediaScreenState.topRatedMoviesList.isEmpty()) {
                    loadTopRatedMovies(true)
                }

                if (mediaScreenState.nowPlayingMoviesList.isEmpty()) {
                    loadNowPlayingMovies(true)
                }

                if (mediaScreenState.popularTvSeriesList.isEmpty()) {
                    loadPopularTvSeries(true)
                }

                if (mediaScreenState.topRatedTvSeriesList.isEmpty()) {
                    loadTopRatedTvSeries(true)
                }

                if (mediaScreenState.trendingAllList.isEmpty()) {
                    loadTrendingAll(true)
                }

            }

            is MediaHomeScreenEvents.Refresh -> {
                loadGenres(
                    fetchFromRemote = true,
                    isMovies = true
                )

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = false
                )


                when (event.type) {

                    Constants.homeScreen -> {

                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.popularScreen -> {
                        loadPopularMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.trendingAllListScreen -> {
                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.tvSeriesScreen -> {
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.topRatedAllListScreen -> {
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )

                        // calling it to have equal top rated and tv series items,
                        // because loadTopRatedMovies() and loadPopularTvSeries() are already
                        // used to create the tvSeries list. So if i don't call it here I we will
                        // have more TopRated items than Popular ones.
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.recommendedListScreen -> {
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                }
            }

            is MediaHomeScreenEvents.OnSearchQueryChanged -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)

                    mediaScreenState.searchQuery = event.query
                    mediaScreenState.searchList = emptyList()

                    loadSearchList()
                }
            }

            is MediaHomeScreenEvents.OnPaginate -> {

                when (event.type) {

                    Constants.trendingAllListScreen -> {
                        loadTrendingAll(true)
                    }

                    Constants.topRatedAllListScreen -> {
                        loadTopRatedMovies(true)
                        loadTopRatedTvSeries(true)

                        // calling it to have equal top rated and tv series items,
                        // because loadTopRatedMovies() and loadPopularTvSeries() are already
                        // used to create the tvSeries list. So if i don't call it here I we will
                        // have more TopRated items than Popular ones.
                        loadPopularTvSeries(true)
                    }

                    Constants.popularScreen -> {

                        Timber.tag(Constants.GET_TAG).d("onOnPaginate: popularScreen")
                        loadPopularMovies(true)
                    }

                    Constants.tvSeriesScreen -> {
                        loadPopularTvSeries(true)
                        loadTopRatedTvSeries(true)
                    }

                    Constants.recommendedListScreen -> {
                        loadNowPlayingMovies(true)
                        loadTopRatedTvSeries(true)
                    }

                    Constants.searchScreen -> {
                        mediaScreenState.searchPage++
                        loadSearchList()
                    }

                }
            }
        }
    }

    private fun loadGenres(
        fetchFromRemote: Boolean,
        isMovies: Boolean
    ) {
        viewModelScope.launch {

            if (isMovies) {
                genreRepository
                    .getGenres(fetchFromRemote, MOVIE, API_KEY)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { genresList ->
                                    mediaScreenState = mediaScreenState.copy(
                                        moviesGenresList = genresList
                                    )
                                }
                            }

                            is Resource.Error -> Unit

                            is Resource.Loading -> Unit
                        }
                    }
            } else {
                genreRepository
                    .getGenres(fetchFromRemote, TV, API_KEY)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { genresList ->
                                    mediaScreenState = mediaScreenState.copy(
                                        tvGenresList = genresList
                                    )
                                }
                            }

                            is Resource.Error -> Unit

                            is Resource.Loading -> Unit
                        }
                    }
            }


        }
    }


    private fun loadPopularMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    POPULAR,
                    mediaScreenState.popularMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    mediaScreenState.popularMoviesList = shuffledMediaList.toList()
                                    mediaScreenState.popularMoviesPage = 1
                                } else {
                                    mediaScreenState.popularMoviesList += shuffledMediaList.toList()
                                    mediaScreenState.popularMoviesPage++
                                }

                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaScreenState = mediaScreenState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }


    private fun loadTopRatedMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    TOP_RATED,
                    mediaScreenState.topRatedMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    mediaScreenState.topRatedMoviesList = shuffledMediaList.toList()
                                    mediaScreenState.topRatedMoviesPage = 1
                                } else {
                                    mediaScreenState.topRatedMoviesList += shuffledMediaList.toList()
                                    mediaScreenState.topRatedMoviesPage++
                                }

                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaScreenState = mediaScreenState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }


    private fun loadNowPlayingMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    NOW_PLAYING,
                    mediaScreenState.nowPlayingMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    mediaScreenState.nowPlayingMoviesList =
                                        shuffledMediaList.toList()
                                    mediaScreenState.nowPlayingMoviesPage = 1
                                } else {
                                    mediaScreenState.nowPlayingMoviesList += shuffledMediaList.toList()
                                    mediaScreenState.nowPlayingMoviesPage++
                                }

                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaScreenState = mediaScreenState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun loadTopRatedTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    TOP_RATED,
                    mediaScreenState.topRatedTvSeriesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()


                                if (isRefresh) {
                                    mediaScreenState.topRatedTvSeriesList =
                                        shuffledMediaList.toList()
                                    mediaScreenState.topRatedTvSeriesPage = 1
                                } else {
                                    mediaScreenState.topRatedTvSeriesList += shuffledMediaList.toList()
                                    mediaScreenState.topRatedTvSeriesPage++
                                }


                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaScreenState = mediaScreenState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun loadPopularTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    POPULAR,
                    mediaScreenState.popularTvSeriesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()


                                if (isRefresh) {
                                    mediaScreenState.popularTvSeriesList =
                                        shuffledMediaList.toList()
                                    mediaScreenState.popularTvSeriesPage = 1
                                } else {
                                    mediaScreenState.popularTvSeriesList += shuffledMediaList.toList()
                                    mediaScreenState.popularTvSeriesPage++
                                }


                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaScreenState = mediaScreenState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun loadTrendingAll(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getTrendingList(
                    fetchFromRemote,
                    isRefresh,
                    ALL,
                    TRENDING_TIME,
                    mediaScreenState.trendingAllPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()


                                if (isRefresh) {
                                    mediaScreenState.trendingAllList = shuffledMediaList.toList()
                                    mediaScreenState.trendingAllPage = 1
                                } else {
                                    mediaScreenState.trendingAllList += shuffledMediaList.toList()
                                    mediaScreenState.trendingAllPage++
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaScreenState = mediaScreenState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun loadSearchList() {

        viewModelScope.launch {


            mediaRepository
                .getSearchList(
                    fetchFromRemote = true,
                    query = mediaScreenState.searchQuery,
                    page = mediaScreenState.searchPage,
                    apiKey = API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                mediaScreenState.searchList += mediaList
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            mediaScreenState = mediaScreenState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun createSpecialList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ) {

        if (isRefresh) {
            mediaScreenState.specialList = emptyList()
        }

        if (mediaScreenState.specialList.size >= 7) {
            return
        }


        val shuffledMediaList = mediaList.take(7).toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            mediaScreenState.specialList = shuffledMediaList
        } else {
            mediaScreenState.specialList += shuffledMediaList
        }

        for (item in mediaScreenState.specialList) {
            Timber.tag("special_list").d(item.title)
        }
    }

    private fun createTvSeriesList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            mediaScreenState.tvSeriesList = shuffledMediaList.toList()
        } else {
            mediaScreenState.tvSeriesList += shuffledMediaList.toList()
        }
    }

    private fun createTopRatedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            mediaScreenState.topRatedAllList = shuffledMediaList.toList()
        } else {
            mediaScreenState.topRatedAllList += shuffledMediaList.toList()
        }
    }

    private fun createRecommendedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            mediaScreenState.recommendedAllList = shuffledMediaList.toList()
        } else {
            mediaScreenState.recommendedAllList += shuffledMediaList.toList()
        }

        createSpecialList(
            mediaList = mediaList,
            isRefresh = isRefresh
        )
    }

}






