package com.ahmedapps.themovies.feature_movie_list.presentation.homeScreen.mediaHomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenState
import com.ahmedapps.themovies.ui.theme.font
import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.util.Item

@Composable
fun MediaHomeScreenSection(
    type: String,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mediaScreenState: MediaHomeScreenState,
) {

    val title = when (type) {
        Constants.trendingAllListScreen -> {
            stringResource(id = R.string.trending)
        }

        Constants.tvSeriesScreen -> {
            stringResource(id = R.string.tv_series)
        }

        Constants.recommendedListScreen -> {
            stringResource(id = R.string.recommended)
        }

        else -> {
            stringResource(id = R.string.top_rated)
        }
    }

    val mediaList = when (type) {
            Constants.trendingAllListScreen -> {
                mediaScreenState.trendingAllList.take(10)
            }

            Constants.tvSeriesScreen -> {
                mediaScreenState.tvSeriesList.take(10)
            }

            Constants.recommendedListScreen -> {
                mediaScreenState.recommendedAllList.take(10)
            }

            else -> {
                mediaScreenState.topRatedAllList.take(10)
            }
        }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = font,
                fontSize = 20.sp
            )

            Text(
                modifier = Modifier
                    .alpha(0.7f)
                    .clickable {
                        bottomBarNavController.navigate("media_list_screen/${type}")
                    },
                text = stringResource(id = R.string.see_all),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontFamily = font,
                fontSize = 14.sp,
            )
        }

        LazyRow {
            items(mediaList.size) {

                var paddingEnd = 0.dp
                if (it == mediaList.size - 1) {
                    paddingEnd = 16.dp
                }

                Item(
                    media = mediaList[it],
                    navController = navController,
                    modifier = Modifier
                        .height(200.dp)
                        .width(150.dp)
                        .padding(start = 16.dp, end = paddingEnd)
                )
            }
        }
    }

}
