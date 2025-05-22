package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.BanThang
import com.example.quanlybongda.Database.Schema.LichThiDau
import com.example.quanlybongda.Database.Schema.Loai.LoaiBT
import kotlinx.coroutines.flow.Flow

@Dao
interface BanThangDAO {
    @Upsert
    suspend fun upsertBanThang(vararg banThang : BanThang);

    @Delete
    suspend fun deleteBanThang(vararg banThang: BanThang);

    @Query("SELECT * FROM BanThang")
    suspend fun selectAllBanThang() : List<BanThang>;

    @Query("SELECT * FROM BanThang WHERE maTD=:maTD")
    suspend fun selectBanThang(maTD: Int) : List<BanThang>;

    @Query("""
        SELECT SUM(CAST(LBT.diemBT AS INT)) FROM BanThang AS BT
        INNER JOIN ThamGiaTD AS TGTD ON TGTD.maTD=BT.maTD AND TGTD.maDoi=:maDoi
        INNER JOIN LoaiBT AS LBT ON LBT.maLBT=BT.maLBT
        GROUP BY BT.maTD
    """)
    suspend fun selectSoBanThangDoi(maDoi: Int) : Int;


    @Query("""
        SELECT COUNT(LBT.diemBT) FROM BanThang AS BT
        INNER JOIN CauThu AS CT ON CT.maCT=BT.maCT AND CT.maDoi=:maDoi
        INNER JOIN LoaiBT AS LBT ON LBT.maLBT=BT.maLBT
        WHERE BT.maTD = :maTD AND LBT.diemBT > 0
        GROUP BY BT.maTD
    """)
    suspend fun selectSoBanThangTranDauDoi(maTD : Int, maDoi: Int) : Int;

    @Query("""
        SELECT COUNT(LBT.diemBT) FROM BanThang AS BT
        INNER JOIN CauThu AS CT ON CT.maCT=BT.maCT AND CT.maDoi=:maDoi
        INNER JOIN LoaiBT AS LBT ON LBT.maLBT=BT.maLBT
        WHERE BT.maTD = :maTD AND LBT.diemBT <= 0
        GROUP BY BT.maTD
    """)
    suspend fun selectSoBanThangPhanLuoiTranDauDoi(maTD : Int, maDoi: Int) : Int;

    @Query("""SELECT * FROM LoaiBT""")
    suspend fun selectAllLoaiBT() : List<LoaiBT>;
}