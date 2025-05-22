package com.example.quanlybongda.Database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.ReturnTypes.CauThuBanThang
import com.example.quanlybongda.Database.ReturnTypes.CauThuViTri
import com.example.quanlybongda.Database.Schema.CauThu

@Dao
interface CauThuDAO {
    @Upsert
    suspend fun upsertCauThu(vararg cauThu: CauThu);

    @Delete
    suspend fun deleteCauThu(vararg cauThu: CauThu);

    @Query("SELECT * FROM CauThu")
    suspend fun selectAlLCauThu() : List<CauThu>;

    @Query("""SELECT CT.*, SUM(CASE WHEN LBT.diemBT > 0 THEN LBT.diemBT END ) as banThang FROM CauThu AS CT
        LEFT JOIN BanThang AS BT ON BT.maCT=CT.maCT
        LEFT JOIN LoaiBT AS LBT ON LBT.maLBT=BT.maLBT
        GROUP BY CT.maCT
    """)
    fun selectAllCauThuWithBanThang() : LiveData<List<CauThu>>;

    @Query("""SELECT CT.*, SUM(LBT.diemBT) as banThang FROM CauThu AS CT
        INNER JOIN BanThang AS BT ON BT.maCT=CT.maCT
        INNER JOIN LoaiBT AS LBT ON LBT.maLBT=BT.maLBT
        WHERE CT.maDoi = :maDoi AND LBT.diemBT > 0
        GROUP BY CT.maCT
    """)
    suspend fun selectCauThuDoiBongWithBanThang(maDoi: Int) : List<CauThuBanThang>;

    @Query("""
        SELECT CT.*, VT.* FROM CauThu AS CT
        INNER JOIN ThamGiaTD AS TGTD ON TGTD.maTD = :maTD AND TGTD.maDoi = :maDoi AND TGTD.maCT = CT.maCT
        INNER JOIN ViTri AS VT ON VT.maVT = TGTD.maVT
        GROUP BY CT.maCT
    """)
    fun selectCauThuTGTDDB(maTD : Int, maDoi : Int) : LiveData<List<CauThuViTri>>;

    @Query("SELECT * FROM CauThu WHERE maDoi = :maDoi")
    suspend fun selectCauThuDoiBong(maDoi: Int) : List<CauThu>;


    @Query("""
        SELECT CT.* FROM CauThu AS CT
        INNER JOIN ThamGiaTD AS TGTD ON TGTD.maTD = :maTD AND TGTD.maCT = CT.maCT
        GROUP BY CT.maCT
    """)
    suspend fun selectCauThuTGTD(maTD : Int) : List<CauThu>;


}