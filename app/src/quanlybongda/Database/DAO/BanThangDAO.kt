package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.BanThang
import com.example.quanlybongda.Database.Schema.LichThiDau
import kotlinx.coroutines.flow.Flow

@Dao
interface BanThangDAO {
    @Upsert
    suspend fun upsertBanThang(vararg banThang : BanThang);

    @Delete
    suspend fun deleteBanThang(vararg banThang: BanThang);

    @Query("SELECT * FROM BanThang")
    suspend fun selectAllBanThang() : List<BanThang>;

    @Query("SELECT * FROM BanThang")
    fun selectAllBanThangFlow() : Flow<List<BanThang>>;

    @Query("SELECT * FROM BanThang WHERE maTD=:maTD LIMIT 1")
    suspend fun selectBanThang(maTD: Int) : List<BanThang>;

    @Query("""
        SELECT SUM(LBT.diemBT) FROM BanThang AS BT
        INNER JOIN ThamGiaTD AS TGTD ON TGTD.maTD=BT.maTD AND TGTD.maDoi=:maDoi
        INNER JOIN LoaiBT AS LBT ON LBT.maLBT=BT.maLBT
        GROUP BY BT.maTD, BT.thoiDiem
    """)
    suspend fun selectSoBanThangDoi(maDoi: Int) : Int;
}