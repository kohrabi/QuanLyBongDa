package com.example.quanlybongda.Database.DAO.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.User.CaDo
import com.example.quanlybongda.Database.Schema.User.YeuThichCauThu
import com.example.quanlybongda.Database.Schema.User.YeuThichDoiBong

@Dao
interface CaDoDAO {
    @Query("""
        SELECT * FROM CaDo
        WHERE userId=:userId
    """)
    suspend fun selectCaDo(userId: Int): List<CaDo>;

    @Query("""
        SELECT * FROM CaDo
        WHERE maTD=:maTD
    """)
    suspend fun selectCaDoByMaTD(maTD: Int): List<CaDo>;

    @Query("""
        SELECT * FROM CaDo
        WHERE maTD=:maTD AND doiCuoc=:doiCuoc
    """)
    suspend fun selectCaDoByMaTDDoiCuoc(maTD: Int, doiCuoc: Int?): List<CaDo>;

    @Query("""
        SELECT count(*) FROM CaDo
        WHERE maTD=:maTD
        GROUP BY maTD
        LIMIT 1
    """)
    suspend fun countCaDoByMaTD(maTD: Int) : Int;

    @Delete
    suspend fun deleteCaDo(caDo: CaDo);

    @Upsert
    suspend fun upsertCaDo(caDo: CaDo);
}