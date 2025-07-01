package com.example.quanlybongda.Database.DAO.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.User.YeuThichCauThu
import com.example.quanlybongda.Database.Schema.User.YeuThichDoiBong

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

    @Delete
    suspend fun deleteCauThuYeuThich(yeuThichCauThu: YeuThichCauThu);

    @Delete
    suspend fun deleteDoiBongYeuThich(yeuThichCauThu: YeuThichDoiBong);

    @Upsert
    suspend fun upsertCauThuYeuThich(yeuThichCauThu: YeuThichCauThu);

    @Upsert
    suspend fun upsertDoiBongYeuThich(yeuThichDoiBong: YeuThichDoiBong);
}