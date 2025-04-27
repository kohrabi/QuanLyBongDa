package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.LichThiDau
import kotlinx.coroutines.flow.Flow


@Dao
interface LichThiDauDAO {
    @Upsert
    suspend fun upsertLichThiDau(vararg lichThiDau : LichThiDau);

    @Delete
    suspend fun deleteLichThiDau(vararg lichThiDau: LichThiDau);

    @Query("SELECT * FROM LichThiDau")
    suspend fun selectAllLichThiDau() : List<LichThiDau>;

    @Query("SELECT * FROM LichThiDau")
    fun selectAllLichThiDauFlow() : Flow<List<LichThiDau>>;

    @Query("SELECT * FROM LichThiDau WHERE maTD=:maTD LIMIT 1")
    suspend fun selectLichThiDauMaTD(maTD: Int) : LichThiDau?;

    @Query("SELECT LTD.* FROM LichThiDau AS LTD " +
            "INNER JOIN DoiBong AS DB1 ON DB1.maDoi=LTD.doiMot " +
            "INNER JOIN DoiBong AS DB2 ON DB2.maDoi=LTD.doiHai  " +
            "INNER JOIN DoiBong AS DBT ON DBT.maDoi=LTD.doiThang  " +
            "WHERE LTD.maMG=:maMG " +
            "GROUP BY LTD.maTD ")
    suspend fun selectAllLichThiDauWithName(maMG: Int) : List<LichThiDau>;
}