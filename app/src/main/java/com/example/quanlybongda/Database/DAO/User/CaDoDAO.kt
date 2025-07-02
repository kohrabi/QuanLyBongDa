package com.example.quanlybongda.Database.DAO.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.User.CaDo

@Dao
interface CaDoDAO {
    @Query("""
        SELECT * FROM CaDo
        WHERE userId=:userId
    """)
    suspend fun selectCaDoByUserID(userId: Int): List<CaDo>;

    @Query("""
        SELECT * FROM CaDo
        WHERE userId=:userId AND maTD=:maTD
        LIMIT 1
    """)
    suspend fun selectCaDoByUserIDMaTD(userId: Int, maTD: Int): CaDo?;

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
    """)
    suspend fun countCaDoByMaTD(maTD: Int) : Int?;

    @Delete
    suspend fun deleteCaDo(caDo: CaDo);

    @Upsert
    suspend fun upsertCaDo(caDo: CaDo);
}