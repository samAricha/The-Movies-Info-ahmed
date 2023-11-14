package com.ahmedapps.themovies.feature_movie_list.presentation.homeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenState
import com.ahmedapps.themovies.feature_movie_list.presentation.components.AutoSwipeSection
import com.ahmedapps.themovies.feature_movie_list.presentation.components.NonFocusedTopBar
import com.ahmedapps.themovies.theme.BigRadius
import kotlin.math.roundToInt

@Composable
fun FavoriteScreen(
    selectedItem: MutableState<Int>,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mediaScreenState: MediaHomeScreenState
) {

    BackHandler(
        enabled = true
    ) {
        selectedItem.value = 0
        bottomBarNavController.navigate("media_home_screen")
    }


    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = BigRadius.dp)
        ) {

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            AutoSwipeSection(
                type = stringResource(id = R.string.watch_list),
                navController = navController,
                mediaScreenState = mediaScreenState
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

            AutoSwipeSection(
                type = stringResource(id = R.string.favorites),
                navController = navController,
                mediaScreenState = mediaScreenState
            )

            Spacer(modifier = Modifier.padding(vertical = 16.dp))

        }
    }

    NonFocusedTopBar(
        toolbarOffsetHeightPx = toolbarOffsetHeightPx.floatValue.roundToInt(),
        navController = navController,
        mediaScreenState = mediaScreenState
    )
}