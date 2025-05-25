package com.example.quanlybongda.Database.Schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quanlybongda.Database.Schema.Loai.SanNha
import java.time.LocalDateTime

@Entity(

    indices = arrayOf(
        Index(value = arrayOf("maDoi"), name="DoiBong_maDoi", unique = true),
        Index(value = arrayOf("maDoi"), name="DoiBong_maDoi_unique", unique = true),
    ),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = SanNha::class,
            parentColumns = ["maSan"],
            childColumns = ["maSan"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = MuaGiai::class,
            parentColumns = ["maMG"],
            childColumns = ["maMG"],
            onDelete = ForeignKey.CASCADE)
    )
)
data class DoiBong(
    @PrimaryKey(autoGenerate = true)
    val maDoi : Int,
    val tenDoi : String,
    val maSan : Int,
    val maMG : Int,
    @ColumnInfo(defaultValue = "false")
    val deleted : Boolean?,
) {
    @Ignore var tenSan : String = ""
}

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="DoiBongBackup_BackupID_unique", unique = true))
)
data class DoiBongBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,
    val maDoi : Int,
    val tenDoi : String,
    val maSan : Int,
    val maMG : Int,
)
