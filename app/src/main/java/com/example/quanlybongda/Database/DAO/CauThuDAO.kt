package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.CauThu

@Dao
interface CauThuDAO {
    @Upsert
    suspend fun upsertCauThu(vararg cauThu: CauThu);

    @Delete
    suspend fun deleteCauThu(vararg cauThu: CauThu);

    @Query("SELECT * FROM CauThu")
    suspend fun selectAlLCauThu() : List<CauThu>;

//    @Query("SELECT * FROM CauThu")
//    suspend fun selectAllCauThuWithBanThang() : List<CauThu>;


}