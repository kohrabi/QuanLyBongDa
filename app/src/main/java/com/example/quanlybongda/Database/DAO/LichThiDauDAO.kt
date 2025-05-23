package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.LichThiDau
import com.example.quanlybongda.Database.Schema.Loai.VongTD
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime


@Dao
interface LichThiDauDAO {
    @Upsert
    suspend fun upsertLichThiDau(vararg lichThiDau : LichThiDau);

    @Delete
    suspend fun deleteLichThiDau(vararg lichThiDau: LichThiDau);

    @Query("SELECT * FROM LichThiDau ORDER BY maTD")
    suspend fun selectAllLichThiDau() : List<LichThiDau>;

    @Query("SELECT * FROM LichThiDau WHERE maTD=:maTD LIMIT 1")
    suspend fun selectLichThiDauMaTD(maTD: Int) : LichThiDau?;

    @Query("SELECT * FROM VongTD")
    suspend fun selectAllVongTD() : List<VongTD>;

    @Query("""SELECT LTD.* FROM LichThiDau AS LTD
           INNER JOIN DoiBong AS DB1 ON DB1.maDoi=LTD.doiMot
           INNER JOIN DoiBong AS DB2 ON DB2.maDoi=LTD.doiHai
           INNER JOIN DoiBong AS DBT ON DBT.maDoi=LTD.doiThang
           WHERE LTD.maMG=:maMG
           GROUP BY LTD.maTD""")
    suspend fun selectAllLichThiDauWithName(maMG: Int) : List<LichThiDau>;

    @Query("""
        SELECT * FROM LichThiDau AS LTD
        WHERE date(LTD.ngayGioThucTe)=date(:localDate)
    """)
    suspend fun selectLichThiDauTrongNgay(localDate : LocalDate) : List<LichThiDau>;


    @Query("""
        SELECT count(*) FROM LichThiDau AS LTD
        WHERE date(LTD.ngayGioThucTe)=date(:localDate) AND (LTD.doiMot=:maDoi OR LTD.doiHai=:maDoi)
    """)
    suspend fun countLichThiDauMaDoi(maDoi : Int, localDate: LocalDate) : Int;


    @Query("""
        SELECT count(*) FROM LichThiDau AS LTD
        WHERE date(LTD.ngayGioThucTe)=date(:localDate) AND LTD.doiThang = :maDoi
    """)
    suspend fun countLichThiDauThangMaDoi(maDoi : Int, localDate: LocalDate) : Int;


    @Query("""
        SELECT count(*) FROM LichThiDau AS LTD
        WHERE date(LTD.ngayGioThucTe)=date(:localDate) AND 
                (LTD.doiMot=:maDoi OR LTD.doiHai=:maDoi) AND
                (LTD.doiThang IS NOT NULL AND LTD.doiThang<>:maDoi)
    """)
    suspend fun countLichThiDauThuaMaDoi(maDoi : Int, localDate: LocalDate) : Int;

}