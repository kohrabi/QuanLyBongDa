package com.example.quanlybongda.Database.Schema.Loai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(Index(value = arrayOf("maVT"), name="ViTri_maVT_unique", unique = true)),
    )
data class ViTri(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val maVT : Int = 0,
    val tenVT : String,
)


@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="ViTriBackup_BackupID_unique", unique = true))
)
data class ViTriBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,

    val maVT : Int,
    val tenVT : String,
)
