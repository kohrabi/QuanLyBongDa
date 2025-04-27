package com.example.quanlybongda.Database.Schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quanlybongda.Database.Schema.Loai.LoaiTP
import java.time.LocalDateTime

@Entity(
    primaryKeys = arrayOf("maTD", "maCT", "thoiDiem"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = LichThiDau::class,
            parentColumns = ["maTD"],
            childColumns = ["maTD"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CauThu::class,
            parentColumns = ["maCT"],
            childColumns = ["maCT"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LoaiTP::class,
            parentColumns = ["maLTP"],
            childColumns = ["maLTP"],
            onDelete = ForeignKey.CASCADE
        ),
    )
)
data class ThePhat(
    val maTD: Int,
    val maCT: Int,
    val thoiDiem: Float,
    val maLTP: Int,
    @ColumnInfo(defaultValue = "false")
    val deleted : Boolean?,
)

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="ThePhatBackup_BackupID_unique", unique = true))
)
data class ThePhatBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,
    val maTD: Int,
    val maCT: Int,
    val thoiDiem: Float,
    val maLTP: Int,
)