package com.ahmedapps.themovies.di

import com.ahmedapps.themovies.feature_movie_details.data.repository.DetailsRepositoryImpl
import com.ahmedapps.themovies.feature_movie_details.data.repository.ExtraDetailsRepositoryImpl
import com.ahmedapps.themovies.feature_movie_details.domain.repository.DetailsRepository
import com.ahmedapps.themovies.feature_movie_details.domain.repository.ExtraDetailsRepository
import com.ahmedapps.themovies.feature_movie_list.data.repository.GenreRepositoryImpl
import com.ahmedapps.themovies.feature_movie_list.data.repository.MediaRepositoryImpl
import com.ahmedapps.themovies.feature_movie_list.domain.repository.GenreRepository
import com.ahmedapps.themovies.feature_movie_list.domain.repository.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(
        mediaRepositoryImpl: MediaRepositoryImpl
    ): MediaRepository

    @Binds
    @Singleton
    abstract fun bindGenreRepository(
        genreRepositoryImpl: GenreRepositoryImpl
    ): GenreRepository


    @Binds
    @Singleton
    abstract fun bindDetailsRepository(
        detailsRepositoryImpl: DetailsRepositoryImpl
    ): DetailsRepository

    @Binds
    @Singleton
    abstract fun bindExtraDetailsRepository(
       extraDetailsRepositoryImpl: ExtraDetailsRepositoryImpl
    ): ExtraDetailsRepository

}
