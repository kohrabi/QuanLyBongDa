package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.Loai.SanNha
import com.example.quanlybongda.Database.Schema.ThamGiaDB
import com.example.quanlybongda.Database.Schema.ThamGiaTD
import kotlinx.coroutines.flow.Flow


@Dao
interface ThamGiaTDDAO {
    @Upsert
    suspend fun upsertThamGiaTD(vararg thamGiaTD: ThamGiaTD);

    @Delete
    suspend fun deleteThamGiaTD(vararg thamGiaTD: ThamGiaTD);

    @Query("""SELECT CT.* FROM ThamGiaTD AS TGTD
        INNER JOIN CauThu AS CT ON CT.maCT=TGTD.maCT
        WHERE TGTD.maDoi=:maDoi AND TGTD.maTD=:maTD""")
    suspend fun selectCauThuTranDau(maTD: Int, maDoi: Int) : Int;
}