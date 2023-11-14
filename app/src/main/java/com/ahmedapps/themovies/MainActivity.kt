package com.ahmedapps.themovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmedapps.themovies.feature_movie_details.presentation.MediaDetailsViewModel
import com.ahmedapps.themovies.feature_movie_details.presentation.WatchVideo
import com.ahmedapps.themovies.feature_movie_details.presentation.detailScreen.ShouldMediaScreeDetail
import com.ahmedapps.themovies.feature_movie_details.presentation.detailScreen.SimilarMediaListScreen
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenEvents
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenState
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeViewModel
import com.ahmedapps.themovies.feature_movie_list.presentation.SearchScreen
import com.ahmedapps.themovies.feature_movie_list.presentation.homeScreen.MediaMainScreen
import com.ahmedapps.themovies.ui.theme.TheMoviesTheme
import com.ahmedapps.themovies.util.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TheMoviesTheme {

                val mediaHomeViewModel = hiltViewModel<MediaHomeViewModel>()

                val connectivityStatus by mediaHomeViewModel.connectivityObserver.observe()
                    .collectAsState(
                        initial = ConnectivityObserver.ConnectivityStatus.Unavailable
                    )

                installSplashScreen().apply {
                    setKeepOnScreenCondition {
                        mediaHomeViewModel.showSplashScreen.value
                    }
                }

                Navigation(
                    connectivityStatus = connectivityStatus,
                    mediaScreenState = mediaHomeViewModel.mediaScreenState,
                    onEvent = mediaHomeViewModel::onEvent
                )

            }
        }

    }
}

@Composable
fun Navigation(
    connectivityStatus: ConnectivityObserver.ConnectivityStatus,
    mediaScreenState: MediaHomeScreenState,
    onEvent: (MediaHomeScreenEvents) -> Unit
) {
    val navController = rememberNavController()
    val mediaDetailsViewModel = hiltViewModel<MediaDetailsViewModel>()

    NavHost(
        navController = navController,
        startDestination = "movies_main_screen"
    ) {
        composable("search_screen") {
            SearchScreen(
                navController = navController,
                mediaScreenState = mediaScreenState,
                onEvent = onEvent
            )
        }

        composable("movies_main_screen") {
            MediaMainScreen(
                connectivityStatus = connectivityStatus,
                navController = navController,
                mediaScreenState = mediaScreenState,
                onEvent = onEvent
            )
        }

        composable(
            "similar_media_list_screen/{title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
            )
        ) { backStackEntry ->

            SimilarMediaListScreen(
                navController = navController,
                backStackEntry = backStackEntry,
                mediaDetailsScreenState = mediaDetailsViewModel.mediaDetailsScreenState,
            )
        }

        composable(
            "movie_detail_screen/{id}/{type}/{category}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("type") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            mediaDetailsViewModel.mediaDetailsScreenState.moviesGenresList =
                mediaScreenState.moviesGenresList
            mediaDetailsViewModel.mediaDetailsScreenState.tvGenresList =
                mediaScreenState.tvGenresList


            ShouldMediaScreeDetail(
                navController = navController,
                mediaDetailsScreenState = mediaDetailsViewModel.mediaDetailsScreenState,
                connectivityStatus = connectivityStatus,
                backStackEntry = backStackEntry,
                onEvent = mediaDetailsViewModel::onEvent
            )
        }

        composable("watch_video_screen") {
            WatchVideo(
                lifecycleOwner = LocalLifecycleOwner.current,
                mediaDetailsScreenState = mediaDetailsViewModel.mediaDetailsScreenState,
            )
        }
    }
}













