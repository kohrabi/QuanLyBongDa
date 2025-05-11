package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.Loai.SanNha
import kotlinx.coroutines.flow.Flow

@Dao
interface SanNhaDAO {
    @Upsert
    suspend fun upsertSanNha(vararg sanNha : SanNha);

    @Delete
    suspend fun deleteSanNha(vararg sanNha: SanNha);

    @Query("SELECT * FROM SanNha")
    suspend fun selectAllSanNha() : List<SanNha>;

    @Query("SELECT * FROM SanNha WHERE maSan=:maSan LIMIT 1")
    suspend fun selectSanNhaMaSan(maSan : Int) : SanNha?;
    
}