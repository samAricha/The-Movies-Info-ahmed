package com.ahmedapps.themovies.feature_movie_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahmedapps.themovies.feature_movie_list.presentation.components.FocusedTopBar
import com.ahmedapps.themovies.feature_movie_list.presentation.components.MediaItem
import com.ahmedapps.themovies.theme.BigRadius
import com.ahmedapps.themovies.util.Constants
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    navController: NavController,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(nestedScrollConnection)
    ) {

        LazyVerticalGrid(
            contentPadding = PaddingValues(top = BigRadius.dp),
            columns = GridCells.Fixed(2),
        ) {

            items(mediaScreenState.searchList.size) {
                MediaItem(
                    isSearchScreen = true,
                    media = mediaScreenState.searchList[it],
                    navController = navController,
                    mediaScreenState = mediaScreenState,
                    onEvent = onEvent
                )

                if (it >= mediaScreenState.searchList.size - 1 && !mediaScreenState.isLoading) {
                    onEvent(MediaHomeScreenEvents.OnPaginate(Constants.searchScreen))
                }
            }
        }

        FocusedTopBar(
            toolbarOffsetHeightPx = toolbarOffsetHeightPx.value.roundToInt(),
            mediaScreenState = mediaScreenState
        ) {
            onEvent(MediaHomeScreenEvents.OnSearchQueryChanged(it))
        }

    }
}

