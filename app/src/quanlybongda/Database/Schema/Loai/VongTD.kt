package com.example.quanlybongda.Database.Schema.Loai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(Index(value = arrayOf("maVTD"), name="VongTD_maVTD_unique", unique = true)),
)
data class VongTD(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val maVTD : Int,
    val tenVTD : String,
)

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="VongTDBackup_BackupID_unique", unique = true))
)
data class VongTDBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,

    val maVTD : Int,
    val tenVTD : String,
)