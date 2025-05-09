package com.example.quanlybongda.Database.Schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quanlybongda.Database.Schema.Loai.LoaiBT
import com.example.quanlybongda.Database.Schema.Loai.LoaiCT
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(
        Index(value = arrayOf("maCT"), name="CauThu_maCT", unique = true),
        Index(value = arrayOf("maCT"), name="CauThu_maCT_unique", unique = true),
    ),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = LoaiCT::class,
            parentColumns = ["maLCT"],
            childColumns = ["maLCT"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DoiBong::class,
            parentColumns = ["maDoi"],
            childColumns = ["maDoi"],
            onDelete = ForeignKey.CASCADE
        ),
    )
)
data class CauThu(
    @PrimaryKey(autoGenerate = true)
    val maCT: Int,
    val tenCT: String,
    val ngaySinh: LocalDate = LocalDate.now(),
    val ghiChu: String,
    val maLCT: Int,
    val maDoi: Int,
    val soAo: Int,
    @ColumnInfo(defaultValue = "false")
    val deleted : Boolean?,
//    @Ignore val banThang: Int,
)

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="CauThuBackup_BackupID_unique", unique = true))
)
data class CauThuBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID: Int,
    val modifiedDate: Int,
    val maCT: Int,
    val tenCT: String,
    val ngaySinh: LocalDate = LocalDate.now(),
    val ghiChu: String,
    val maLCT: Int,
    val maDoi: Int,
    val soAo: Int,
)
