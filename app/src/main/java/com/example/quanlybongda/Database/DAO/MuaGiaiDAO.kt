package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.MuaGiai
import kotlinx.coroutines.flow.Flow

@Dao
interface MuaGiaiDAO {
    @Upsert
    suspend fun upsertDSMuaGiai(vararg dsMuaGiai : MuaGiai);

    @Delete
    suspend fun deleteDSMuaGiai(vararg dsMuaGiai: MuaGiai);

    @Query("SELECT * FROM MuaGiai")
    suspend fun selectAllMuaGiai() : List<MuaGiai>;
}