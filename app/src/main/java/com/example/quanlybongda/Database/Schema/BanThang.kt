package com.example.quanlybongda.Database.Schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quanlybongda.Database.Schema.Loai.LoaiBT

@Entity(
    primaryKeys = arrayOf("maTD", "thoiDiem"),
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
            entity = LoaiBT::class,
            parentColumns = ["maLBT"],
            childColumns = ["maLBT"],
            onDelete = ForeignKey.CASCADE
        ),
    )
)
data class BanThang(
    val maTD: Int,
    val thoiDiem: Float,

    val maCT: Int,
    val maLBT: Int,
    @ColumnInfo(defaultValue = "false")
    val deleted : Boolean? = false,
) {
    @Ignore var side : String = "";
    @Ignore var tenCT : String = "";
    @Ignore var tenLBT : String = "";
}

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="BanThangBackup_BackupID_unique", unique = true))
)
data class BanThangBackup(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    val BackupID : Int,
    val modifiedDate : Int,
    val maTD: Int,
    val thoiDiem: Float,

    val maCT: Int,
    val maLBT: Int,
)