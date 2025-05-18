package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.ThePhat
import kotlinx.coroutines.flow.Flow

@Dao
interface ThePhatDAO {
    @Upsert
    suspend fun upsertThePhat(vararg thePhat : ThePhat);

    @Delete
    suspend fun deleteThePhat(vararg thePhat: ThePhat);

    @Query("SELECT * FROM ThePhat")
    suspend fun selectAllThePhat() : List<ThePhat>;

    @Query("SELECT * FROM ThePhat WHERE maTD=:maTD LIMIT 1")
    suspend fun selectThePhat(maTD: Int) : List<ThePhat>;
}