package com.example.quanlybongda.Database.Schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(
        Index(value = arrayOf("maTT"), name="TrongTai_maTT", unique = true),
        Index(value = arrayOf("maTT"), name="TrongTai_maTT_unique", unique = true),
    ),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = MuaGiai::class,
            parentColumns = ["maMG"],
            childColumns = ["maMG"],
            onDelete = ForeignKey.CASCADE
        ),
    )
)
data class TrongTai(
    @PrimaryKey(autoGenerate = true)
    val maTT : Int = 0,
    val tenTT : String,
    val ngaySinh : LocalDate = LocalDate.now(),
    val maMG : Int,
    @ColumnInfo(defaultValue = "false")
    val deleted : Boolean? = false,
)


@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="TrongTaiBackup_BackupID_unique", unique = true))
)
data class TrongTaiBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,

    val maTT : Int,
    val tenTT : String,
    val ngaySinh : LocalDateTime = LocalDateTime.now(),
    val maMG : Int
)