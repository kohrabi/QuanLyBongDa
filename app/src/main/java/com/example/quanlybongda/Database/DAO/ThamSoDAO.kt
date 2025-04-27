package com.example.quanlybongda.Database.DAO

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.ThamSo

@Dao
interface ThamSoDAO {
//    @Query("UPDATE ThamSo SET giaTri=:giaTri WHERE tenThamSo=:tenThamSo")
//    suspend fun updateThamSo(tenThamSo: String, giaTri: String);

    @Query("SELECT * FROM ThamSo WHERE tenThamSo=:tenThamSo LIMIT 1")
    suspend fun selectThamSo(tenThamSo: String) : ThamSo?;

}