package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.ThamGiaDB
import kotlinx.coroutines.flow.Flow


@Dao
interface ThamGiaDBDAO {
    @Upsert
    suspend fun upsertThamGiaDB(vararg thamGiaDB : ThamGiaDB);

    @Delete
    suspend fun deleteThamGiaDB(vararg thamGiaDB: ThamGiaDB);

    @Query("SELECT * FROM ThamGiaDB")
    suspend fun selectAllThamGiaDB() : List<ThamGiaDB>;

    @Query("SELECT * FROM ThamGiaDB")
    fun selectAllThamGiaDBFlow() : Flow<List<ThamGiaDB>>;

//    @Query("SELECT COUNT(*) FROM ThamGiaDB WHERE maMG=:maMG AND maDoi=:maDoi")
//    suspend fun countThamGiaDB(maMG: Int, maDoi: Int) : Int;
//
//    @Query("SELECT COUNT(*) FROM ThamGiaDB " +
//            "INNER JOIN CauThu ON CauThu.maCT=ThamGiaDB.maCT " +
//            "WHERE maMG=:maMG AND maDoi=:maDoi AND maLCT=2"
//    )
//    suspend fun countThamGiaDBNuocNgoai(maMG: Int, maDoi: Int) : Int;
}