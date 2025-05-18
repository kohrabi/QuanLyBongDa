package com.example.quanlybongda.Database.Schema

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quanlybongda.Database.Schema.Loai.ViTri
import java.time.LocalDateTime

@Entity(
    primaryKeys = ["maTD", "maCT"],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = DoiBong::class,
            parentColumns = ["maDoi"],
            childColumns = ["maDoi"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CauThu::class,
            parentColumns = ["maCT"],
            childColumns = ["maCT"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LichThiDau::class,
            parentColumns = ["maTD"],
            childColumns = ["maTD"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ViTri::class,
            parentColumns = ["maVT"],
            childColumns = ["maVT"],
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class ThamGiaTD(
    val maTD : Int,
    val maCT : Int,
    val maDoi: Int,
    val maVT: Int,
)

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="ThamGiaTDBackup_BackupID_unique", unique = true))
)
data class ThamGiaTDBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID: Int,
    val modifiedDate : Int,

    val maTD : Int,
    val maCT : Int,
    val maDoi: Int,
    val maVT: Int,
)