package com.example.quanlybongda.Database.Schema

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    primaryKeys = ["maCT", "maMG"],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = DoiBong::class,
            parentColumns = ["maDoi"],
            childColumns = ["maDoi"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = CauThu::class,
            parentColumns = ["maCT"],
            childColumns = ["maCT"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = MuaGiai::class,
            parentColumns = ["maMG"],
            childColumns = ["maMG"],
            onDelete = ForeignKey.CASCADE)
    )
)
data class ThamGiaDB(
    val maDoi : Int,
    val maCT : Int,
    val maMG: Int,
)

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="ThamGiaDBBackup_BackupID_unique", unique = true))
)
data class ThamGiaDBBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID: Int,
    val modifiedDate : Int,
    val maDoi : Int,
    val maCT : Int,
    val maMG: Int,
)