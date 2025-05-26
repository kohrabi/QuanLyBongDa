package com.example.quanlybongda.Database.Schema.Loai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quanlybongda.Database.Schema.MuaGiai
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(Index(value = arrayOf("maSan"), name="SanNha_maSan_unique", unique = true)),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = MuaGiai::class,
            parentColumns = ["maMG"],
            childColumns = ["maMG"],
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class SanNha(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val maSan : Int = 0,
    val tenSan : String,
    val diaChi : String,
    val maMG : Int,
)


@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="SanNhaBackup_BackupID_unique", unique = true))
)
data class SanNhaBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,
    val maSan : Int,
    val tenSan : String,
    val diaChi : String,
    val maMG : Int,
)