package com.example.quanlybongda.Database.Schema.Loai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(

    indices = arrayOf(Index(value = arrayOf("maLBT"), name="LoaiBT_maLBT_unique", unique = true)),
)
data class LoaiBT(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val maLBT : Int,
    val tenLBT : String,
    @ColumnInfo(defaultValue = "1")
    val diemBT : Int,
)


@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="LoaiBTBackup_BackupID_unique", unique = true))
)
data class LoaiBTBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,
    val maLBT : Int,
    val tenLBT : String,
    val diemBT : Int,
)
