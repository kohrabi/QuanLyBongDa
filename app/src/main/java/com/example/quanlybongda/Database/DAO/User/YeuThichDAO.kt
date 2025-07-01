package com.example.quanlybongda.Database.DAO.User

import androidx.room.Dao
import androidx.room.Query

@Dao
interface YeuThichDAO {
    @Query("""
        SELECT maCT FROM YeuThichCauThu
        WHERE userId=:userId
    """)
    suspend fun selectCauThuYeuThich(userId: Int): List<Int>;

    @Query("""
        SELECT maDoi FROM YeuThichDoiBong
        WHERE userId=:userId
    """)
    suspend fun selectDoiBongYeuThich(userId: Int): List<Int>;
}