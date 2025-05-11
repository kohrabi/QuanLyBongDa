package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.ThamGiaDB
import kotlinx.coroutines.flow.Flow


@Dao
interface ThamGiaDBDAO {

    @Query("SELECT COUNT(*) FROM CauThu WHERE maDoi=:maDoi")
    suspend fun countThamGiaDB(maDoi: Int) : Int;

    @Query("""SELECT COUNT(*) FROM CauThu     
        WHERE maDoi=:maDoi AND maLCT=:maLCT""")
    suspend fun countThamGiaDBLoaiCT(maLCT: Int, maDoi: Int) : Int;
}