package com.example.quanlybongda.Database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.ReturnTypes.DoiBongTenSan
import com.example.quanlybongda.Database.Schema.DoiBong

@Dao
interface DoiBongDAO {
    @Upsert
    suspend fun upsertDoiBong(vararg doiBong: DoiBong);

    @Delete
    suspend fun deleteDoiBong(vararg doiBong: DoiBong);

    @Query("SELECT * FROM DoiBong")
    suspend fun selectAllDoiBong() : List<DoiBong>;

    @Query("SELECT * FROM DoiBong WHERE maDoi = :maDoi")
    suspend fun selectDoiBongMaDoi(maDoi: Int) : DoiBong?;

    @Query("SELECT * FROM DoiBong WHERE maMG = :maMG")
    suspend fun selectDoiBongMuaGiai(maMG: Int) : DoiBong;

    @Query("SELECT DoiBong.*, SanNha.* FROM DoiBong INNER JOIN SanNha ON SanNha.maSan=DoiBong.maSan")
    fun selectAllDoiBongWithTenSan() : LiveData<List<DoiBongTenSan>>;

    @Query("SELECT * FROM DoiBong WHERE tenDoi LIKE :tenDoi")
    suspend fun selectDoiBongTen(tenDoi: String) : List<DoiBong>;
}