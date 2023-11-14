package com.ahmedapps.themovies

import android.app.Application
import com.ahmedapps.themovies.util.SharedPrefs
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        SharedPrefs.init(applicationContext)
        resetScrollPositions()
    }

    private fun resetScrollPositions() {
        SharedPrefs.putValue("POPULAR_MOVIES_scroll_position", 0)
        SharedPrefs.putValue("TOP_RATED_MOVIES_scroll_position", 0)
        SharedPrefs.putValue("TV_scroll_position", 0)
    }
}