package com.ahmedapps.themovies.feature_movie_list.presentation.homeScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenEvents
import com.ahmedapps.themovies.feature_movie_list.presentation.MediaHomeScreenState
import com.ahmedapps.themovies.feature_movie_list.presentation.homeScreen.mediaHomeScreen.MediaHomeScreen
import com.ahmedapps.themovies.ui.theme.font
import com.ahmedapps.themovies.util.ConnectivityObserver
import com.ahmedapps.themovies.util.Constants

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun MediaMainScreen(
    connectivityStatus: ConnectivityObserver.ConnectivityStatus,
    navController: NavController,
    mediaScreenState: MediaHomeScreenState,
    onEvent: (MediaHomeScreenEvents) -> Unit
) {

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Popular",
            selectedIcon = Icons.Filled.LocalFireDepartment,
            unselectedIcon = Icons.Outlined.LocalFireDepartment,
        ),
        BottomNavigationItem(
            title = "Tv Series",
            selectedIcon = Icons.Filled.LiveTv,
            unselectedIcon = Icons.Outlined.LiveTv
        ),
        BottomNavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder
        )
    )

    val selectedItem = rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomBarNavController = rememberNavController()

    Scaffold(
        content = { paddingValues ->
            BottomNavigationScreens(
                connectivityStatus = connectivityStatus,
                selectedItem = selectedItem,
                modifier = Modifier
                    .padding(
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mediaScreenState = mediaScreenState,
                onEvent = onEvent
            )
        },

        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem.intValue == index,
                        onClick = {
                            selectedItem.intValue = index

                            when (selectedItem.intValue) {
                                0 -> {
                                    bottomBarNavController.navigate("media_home_screen")
                                }

                                1 -> bottomBarNavController.navigate(
                                    "media_list_screen/${Constants.popularScreen}"
                                )

                                2 -> bottomBarNavController.navigate(
                                    "media_list_screen/${Constants.tvSeriesScreen}"
                                )

                                3 -> bottomBarNavController.navigate(
                                    "favorite_screen"
                                )
                            }

                        },

                        label = {
                            Text(
                                text = item.title,
                                fontFamily = font,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItem.intValue) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }

        }
    )

}

@Composable
fun BottomNavigationScreens(
    connectivityStatus: ConnectivityObserver.ConnectivityStatus,
    selectedItem: MutableState<Int>,
    modifier: Modifier = Modifier,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mediaScreenState: MediaHomeScreenState,
    onEvent: (MediaHomeScreenEvents) -> Unit
) {

    NavHost(
        modifier = modifier,
        navController = bottomBarNavController,
        startDestination = "media_home_screen"
    ) {

        composable("media_home_screen") {
            MediaHomeScreen(
                connectivityStatus = connectivityStatus,
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mediaScreenState = mediaScreenState,
                onEvent = onEvent
            )
        }

        composable(
            "media_list_screen/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            MediaListScreen(
                connectivityStatus = connectivityStatus,
                selectedItem = selectedItem,
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                navBackStackEntry = navBackStackEntry,
                mediaScreenState = mediaScreenState,
                onEvent = onEvent
            )
        }

        composable("favorite_screen") {
            FavoriteScreen(
                selectedItem = selectedItem,
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mediaScreenState = mediaScreenState,
            )
        }

    }
}









