package com.example.quanlybongda.Database.Schema.Loai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(

    indices = arrayOf(Index(value = arrayOf("maLCT"), name="LoaiCT_maLCT_unique", unique = true)),
)
data class LoaiCT(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val maLCT : Int = 0,
    val tenLCT : String,
    val soCauThuToiDa : Int,
)

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="LoaiCTBackup_BackupID_unique", unique = true))
)
data class LoaiCTBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,

    val maLCT : Int,
    val tenLCT : String,
    val soCauThuToiDa : Int,
)
