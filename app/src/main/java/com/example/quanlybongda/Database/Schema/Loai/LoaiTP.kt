package com.example.quanlybongda.Database.Schema.Loai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(Index(value = arrayOf("maLTP"), name="LoaiTP_maLTP_unique", unique = true)),
)
data class LoaiTP(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val maLTP : Int,
    val tenLTP : String,
    val soThePhatToiDa: Int,
)


@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="LoaiTPBackup_BackupID_unique", unique = true))
)
data class LoaiTPBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,

    val maLTP : Int,
    val tenLTP : String,
    val soThePhatToiDa: Int,
)
