package com.example.quanlybongda.Database.Schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(
        Index(value = arrayOf("maMG"), name="MuaGiai_maMG", unique = true),
        Index(value = arrayOf("maMG"), name="MuaGiai_maMG_unique", unique = true),
    ),
)
data class MuaGiai (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val maMG : Int = 0,
    val tenMG : String,
    val ngayDienRa : LocalDate = LocalDate.now(),
    val ngayKetThuc : LocalDate = LocalDate.now(),
    @ColumnInfo(defaultValue = "false")
    val deleted : Boolean? = false,
    val imageURL : String? = "",
)

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="MuaGiaiBackup_BackupID_unique", unique = true))
)
data class MuaGiaiBackup (
    @PrimaryKey(autoGenerate = true)
    val BackupID: Int,
    val modifiedDate : Int,
    val maMG : Int,
    val tenMG : String,
    val ngayDienRa : LocalDate = LocalDate.now(),
    val ngayKetThuc : LocalDate = LocalDate.now(),
    val imageURL : String? = "",
)