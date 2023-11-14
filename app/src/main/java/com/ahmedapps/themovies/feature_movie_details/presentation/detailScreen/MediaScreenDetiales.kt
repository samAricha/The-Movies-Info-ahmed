package com.ahmedapps.themovies.feature_movie_details.presentation.detailScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ahmedapps.themovies.R
import com.ahmedapps.themovies.feature_movie_details.presentation.MediaDetailsScreenEvents
import com.ahmedapps.themovies.feature_movie_details.presentation.MediaDetailsScreenState
import com.ahmedapps.themovies.feature_movie_list.data.remote.api.MediaApi
import com.ahmedapps.themovies.feature_movie_list.domain.models.Media
import com.ahmedapps.themovies.feature_movie_list.presentation.components.genresProvider
import com.ahmedapps.themovies.theme.SmallRadius
import com.ahmedapps.themovies.ui.theme.font
import com.ahmedapps.themovies.util.ConnectivityObserver
import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.util.RatingBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ShouldMediaScreeDetail(
    navController: NavController,
    mediaDetailsScreenState: MediaDetailsScreenState,
    connectivityStatus: ConnectivityObserver.ConnectivityStatus,
    backStackEntry: NavBackStackEntry,
    onEvent: (MediaDetailsScreenEvents) -> Unit
) {

    val id = backStackEntry.arguments?.getInt("id")
    val type = backStackEntry.arguments?.getString("type")
    val category = backStackEntry.arguments?.getString("category")

    mediaDetailsScreenState.currentMovieId = id ?: 0

    if (id != null && type != null && category != null) {

        LaunchedEffect(key1 = id) {
            onEvent(
                MediaDetailsScreenEvents.GoToDetails(id, type, category)
            )
        }

        val media = mediaDetailsScreenState.media

        if (media != null) {
            MediaScreeDetail(
                navController = navController,
                media = media,
                mediaDetailsScreenState = mediaDetailsScreenState,
                connectivityStatus = connectivityStatus,
                onEvent = onEvent
            )
        } else {
            Timber.tag(Constants.GET_TAG).d("SomethingWentWrong 1")
            SomethingWentWrong()
        }
    } else {
        Timber.tag(Constants.GET_TAG).d("SomethingWentWrong 2")
        SomethingWentWrong()
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaScreeDetail(
    navController: NavController,
    media: Media,
    mediaDetailsScreenState: MediaDetailsScreenState,
    connectivityStatus: ConnectivityObserver.ConnectivityStatus,
    onEvent: (MediaDetailsScreenEvents) -> Unit
) {

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    val noInternetConnection =
        (connectivityStatus == ConnectivityObserver.ConnectivityStatus.Unavailable || connectivityStatus == ConnectivityObserver.ConnectivityStatus.Lost)

    fun refresh() = refreshScope.launch {
        refreshing = true
        mediaDetailsScreenState.isLoading = true
        delay(1500)

        if (!noInternetConnection) {
            onEvent(
                MediaDetailsScreenEvents.Refresh(
                    type = media.media_type,
                    id = mediaDetailsScreenState.currentMovieId,
                    category = media.category
                )
            )
        }
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    val imageUrl = "${MediaApi.IMAGE_BASE_URL}${media.backdrop_path}"

    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl).size(Size.ORIGINAL)
            .build()
    )

    val surface = MaterialTheme.colorScheme.surface
    var averageColor by remember {
        mutableStateOf(surface)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .pullRefresh(refreshState)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                VideoSection(
                    navController = navController,
                    mediaDetailsScreenState = mediaDetailsScreenState,
                    media = media,
                    imageState = imagePainter.state
                ) { color ->
                    averageColor = color
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {

                    Poster(media = media) {}

                    Spacer(modifier = Modifier.width(12.dp))

                    Info(
                        media = media,
                        mediaDetailsScreenState = mediaDetailsScreenState
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Overview(media = media)

            SimilarMediaSection(
                navController = navController,
                media = media,
                mediaDetailsScreenState = mediaDetailsScreenState
            )


            Spacer(modifier = Modifier.height(16.dp))

        }

        PullRefreshIndicator(
            refreshing, refreshState, Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun VideoSection(
    navController: NavController,
    mediaDetailsScreenState: MediaDetailsScreenState,
    media: Media,
    imageState: AsyncImagePainter.State,
    onImageLoaded: (color: Color) -> Unit
) {

    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable {

                if (mediaDetailsScreenState.videosList.isNotEmpty()) {

                    mediaDetailsScreenState.videoToPlay =
                        mediaDetailsScreenState.videosList.shuffled()[0]

                    navController.navigate("watch_video_screen")
                } else {
                    Toast
                        .makeText(
                            context,
                            "No video is available at the moment. 🥺",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }

            },
        shape = RoundedCornerShape(0),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

            MovieImage(
                imageState = imageState,
                description = media.title,
                noImageId = null,
            ) { color ->
                onImageLoaded(color)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .size(50.dp)
                    .alpha(0.7f)
                    .background(Color.LightGray)
            )
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = stringResource(id = R.string.watch_trailer),
                tint = Color.Black,
                modifier = Modifier.size(35.dp)
            )

        }
    }
}

@Composable
fun Poster(
    media: Media,
    onImageLoaded: (color: Color) -> Unit
) {

    val posterUrl = "${MediaApi.IMAGE_BASE_URL}${media.poster_path}"
    val posterPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(posterUrl).size(Size.ORIGINAL)
            .build()
    )
    val posterState = posterPainter.state


    Column {
        Spacer(modifier = Modifier.height(200.dp))

        Card(
            modifier = Modifier
                .width(180.dp)
                .height(250.dp)
                .padding(start = 16.dp),
            shape = RoundedCornerShape(SmallRadius),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                MovieImage(
                    imageState = posterState,
                    description = media.title,
                    noImageId = Icons.Rounded.ImageNotSupported
                ) { color ->
                    onImageLoaded(color)
                }
            }
        }
    }
}

@Composable
fun Info(
    media: Media,
    mediaDetailsScreenState: MediaDetailsScreenState,
) {

    val genres = genresProvider(
        genre_ids = media.genre_ids,
        allGenres = if (media.media_type == Constants.MOVIE) mediaDetailsScreenState.moviesGenresList
        else mediaDetailsScreenState.tvGenresList
    )

    Column {
        Spacer(modifier = Modifier.height(260.dp))

        Text(
            text = media.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            fontFamily = font,
            fontSize = 19.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBar(
                modifier = Modifier,
                starsModifier = Modifier.size(18.dp),
                rating = media.vote_average.div(2)
            )

            Text(
                modifier = Modifier.padding(
                    horizontal = 4.dp
                ),
                text = media.vote_average.toString().take(3),
                fontFamily = font,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {


            Text(
                text =
                if (media.release_date != Constants.unavailable)
                    media.release_date.take(4)
                else "",

                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = font,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 0.5.dp),
                text = if (media.adult) "+18" else "-12",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = font,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = genres,
            fontFamily = font,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = minutesToReadableTime(media.runtime ?: 0),
            fontFamily = font,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )
    }
}


@Composable
fun Overview(
    media: Media
) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            text = "\"${media.tagline ?: ""}\"",
            fontFamily = font,
            fontSize = 17.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            text = stringResource(R.string.overview),
            fontFamily = font,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            text = media.overview,
            fontFamily = font,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )

    }
}


@Composable
fun SomethingWentWrong() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Something went wrong",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            fontFamily = font,
            fontSize = 19.sp
        )
    }
}

fun minutesToReadableTime(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return String.format("%02d hr %02d min", hours, remainingMinutes)
}





