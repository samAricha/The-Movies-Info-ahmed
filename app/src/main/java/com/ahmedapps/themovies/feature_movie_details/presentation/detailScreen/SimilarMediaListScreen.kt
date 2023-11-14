package com.ahmedapps.themovies.feature_movie_details.presentation.detailScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.ahmedapps.themovies.feature_movie_details.presentation.MediaDetailsScreenState
import com.ahmedapps.themovies.feature_movie_list.presentation.components.ListShimmerEffect
import com.ahmedapps.themovies.feature_movie_list.presentation.components.header
import com.ahmedapps.themovies.theme.SmallRadius
import com.ahmedapps.themovies.ui.theme.font
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimilarMediaListScreen(
    navController: NavController,
    mediaDetailsScreenState: MediaDetailsScreenState,
    backStackEntry: NavBackStackEntry,
) {
    val name = backStackEntry.arguments?.getString("title")
    val title = "Similar to\n✨ $name ✨"

    val mediaList = mediaDetailsScreenState.similarMediaList

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        mediaDetailsScreenState.isLoading = true
        delay(1500)
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .pullRefresh(refreshState)
    ) {

        if (mediaList.isEmpty()) {
            ListShimmerEffect(
                title = title,
                SmallRadius
            )
        } else {

            val listState = rememberLazyGridState()

            LazyVerticalGrid(
                state = listState,
                contentPadding = PaddingValues(top = SmallRadius.dp),
                columns = GridCells.Adaptive(190.dp),
            ) {

                header {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    vertical = 16.dp,
                                    horizontal = 22.dp
                                ),
                            textAlign = TextAlign.Center,
                            text = title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = font,
                            fontSize = 20.sp
                        )
                    }

                }

                items(mediaList.size) { i ->
                    SimilarMediaItem(
                        media = mediaList[i],
                        navController = navController,
                        mediaDetailsScreenState = mediaDetailsScreenState
                    )
                }

            }
        }


        PullRefreshIndicator(
            refreshing,
            refreshState,
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = (SmallRadius - 8).dp)
        )

    }
}
