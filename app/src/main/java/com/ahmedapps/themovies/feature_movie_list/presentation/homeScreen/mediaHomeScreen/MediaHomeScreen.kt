package com.ahmedapps.themovies.feature_movie_list.presentation.homeScreen.mediaHomeScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.feature_movie_list.presentation.components.AutoSwipeSection
import com.ahmedapps.themovies.feature_movie_list.presentation.components.NonFocusedTopBar
import com.ahmedapps.themovies.feature_movie_list.presentation.components.shimmerEffect
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenEvents
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenState
import com.ahmedapps.themovies.theme.MediumRadius
import com.ahmedapps.themovies.theme.BigRadius
import com.ahmedapps.themovies.ui.theme.font
import com.ahmedapps.themovies.util.ConnectivityObserver
import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.util.Constants.recommendedListScreen
import com.ahmedapps.themovies.util.Constants.topRatedAllListScreen
import com.ahmedapps.themovies.util.Constants.trendingAllListScreen
import com.ahmedapps.themovies.util.Constants.tvSeriesScreen
import com.ahmedapps.themovies.feature_movie_list.presentation.components.ShouldShowMediaHomeScreenSectionOrShimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaHomeScreen(
    connectivityStatus: ConnectivityObserver.ConnectivityStatus,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mediaScreenState: MediaHomeScreenState,
    onEvent: (MediaHomeScreenEvents) -> Unit
) {

    val toolbarHeightPx = with(LocalDensity.current) { BigRadius.dp.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val context = LocalContext.current
    BackHandler(
        enabled = true
    ) {
        (context as Activity).finish()
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    val noInternetConnection =
        (connectivityStatus == ConnectivityObserver.ConnectivityStatus.Unavailable ||
                connectivityStatus == ConnectivityObserver.ConnectivityStatus.Lost)

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)

        if (!noInternetConnection) {
            onEvent(MediaHomeScreenEvents.Refresh(type = Constants.homeScreen))
        }

        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .pullRefresh(refreshState),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = BigRadius.dp),
        ) {

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = trendingAllListScreen,
                showShimmer = mediaScreenState.trendingAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mediaScreenState = mediaScreenState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            if (mediaScreenState.specialList.isEmpty()) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(id = R.string.special),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = font,
                    fontSize = 20.sp
                )
                Box(
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth(0.9f)
                        .padding(
                            top = 20.dp,
                            bottom = 12.dp
                        )
                        .clip(RoundedCornerShape(MediumRadius))
                        .shimmerEffect(false)
                        .align(CenterHorizontally)
                )
            } else {
                AutoSwipeSection(
                    type = stringResource(id = R.string.special),
                    navController = navController,
                    mediaScreenState = mediaScreenState
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = tvSeriesScreen,
                showShimmer = mediaScreenState.tvSeriesList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mediaScreenState = mediaScreenState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = topRatedAllListScreen,
                showShimmer = mediaScreenState.topRatedAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mediaScreenState = mediaScreenState
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = recommendedListScreen,
                showShimmer = mediaScreenState.recommendedAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mediaScreenState = mediaScreenState
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))
        }

        PullRefreshIndicator(
            refreshing,
            refreshState,
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = (BigRadius - 8).dp)
        )
    }

    NonFocusedTopBar(
        toolbarOffsetHeightPx = toolbarOffsetHeightPx.value.roundToInt(),
        navController = navController,
        mediaScreenState = mediaScreenState
    )

}












