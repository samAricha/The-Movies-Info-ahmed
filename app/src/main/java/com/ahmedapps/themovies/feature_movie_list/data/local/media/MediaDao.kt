package com.ahmedapps.themovies.feature_movie_list.data.local.media

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaList(
        mediaEntities: List<MediaEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItem(
        mediaItem: MediaEntity
    )

    @Update
    suspend fun updateMediaItem(
        mediaItem: MediaEntity
    )

    @Query(
        """
            DELETE FROM mediaentity 
            WHERE media_type = :mediaType AND category = :category
        """
    )
    suspend fun deleteMediaByTypeAndCategory(mediaType: String, category: String)

    @Query("SELECT * FROM mediaentity WHERE id = :id")
    suspend fun getMediaById(id: Int): MediaEntity

    @Query(
        """
            SELECT * 
            FROM mediaentity 
            WHERE media_type = :mediaType AND category = :category
        """
    )
    suspend fun getMediaListByTypeAndCategory(
        mediaType: String, category: String
    ): List<MediaEntity>

    @Query(
        """
            DELETE FROM mediaentity 
            WHERE category = :category
        """
    )
    suspend fun deleteTrendingMediaList(category: String)


    @Query(
        """
            SELECT * 
            FROM mediaentity 
            WHERE category = :category
        """
    )
    suspend fun getTrendingMediaList(category: String): List<MediaEntity>


}