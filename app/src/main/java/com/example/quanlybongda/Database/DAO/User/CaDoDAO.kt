package com.example.quanlybongda.Database.DAO.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.User.CaDo
import com.example.quanlybongda.Database.Schema.User.YeuThichCauThu
import com.example.quanlybongda.Database.Schema.User.YeuThichDoiBong

@Dao
interface CaDoDAO {
    @Query("""
        SELECT * FROM CaDo
        WHERE userId=:userId
    """)
    suspend fun selectCaDo(userId: Int): List<CaDo>;

    @Delete
    suspend fun deleteCaDo(caDo: CaDo);

    @Upsert
    suspend fun upsertCaDo(caDo: CaDo);
}