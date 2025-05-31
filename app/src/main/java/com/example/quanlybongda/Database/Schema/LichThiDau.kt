package com.example.quanlybongda.Database.Schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.quanlybongda.Database.Schema.Loai.SanNha
import com.example.quanlybongda.Database.Schema.Loai.VongTD
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(
        Index(value = arrayOf("maTD"), name="LichThiDau_maTD", unique = true),
        Index(value = arrayOf("maTD"), name="LichThiDau_maTD_unique", unique = true),
    ),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = MuaGiai::class,
            parentColumns = ["maMG"],
            childColumns = ["maMG"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VongTD::class,
            parentColumns = ["maVTD"],
            childColumns = ["maVTD"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SanNha::class,
            parentColumns = ["maSan"],
            childColumns = ["maSan"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = DoiBong::class,
            parentColumns = ["maDoi"],
            childColumns = ["doiMot"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DoiBong::class,
            parentColumns = ["maDoi"],
            childColumns = ["doiHai"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DoiBong::class,
            parentColumns = ["maDoi"],
            childColumns = ["doiThang"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrongTai::class,
            parentColumns = ["maTT"],
            childColumns = ["maTT"],
            onDelete = ForeignKey.CASCADE
        ),
    )
)
data class LichThiDau(
    @PrimaryKey(autoGenerate = true)
    val maTD : Int = 0,
    val maMG: Int,
    val maVTD : Int,
    val maSan : Int,

    val doiMot : Int,
    val doiHai : Int,
    val doiThang : Int? = null,

    val ngayGioDuKien : LocalDateTime = LocalDateTime.now(),
    val ngayGioThucTe : LocalDateTime = LocalDateTime.now(),

    val thoiGianDaThiDau : Float,
    val maTT : Int,
    @ColumnInfo(defaultValue = "false")
    val deleted : Boolean? = false,
) {
    @Ignore var tenDoiMot: String? = null;
    @Ignore var doiMotLogo: String? = null;
    @Ignore var banThangDoiMot: Int = 0;
    @Ignore var tenDoiHai: String? = null;
    @Ignore var doiHaiLogo: String? = null;
    @Ignore var tenDoiThang: String? = null;
    @Ignore var tenMG: String? = null;
    @Ignore var banThangDoiHai: Int = 0;
}

@Entity(
    indices = arrayOf(Index(value=["BackupID"], name="LichThiDauBackup_BackupID_unique", unique = true))
)
data class LichThiDauBackup(
    @PrimaryKey(autoGenerate = true)
    val BackupID : Int,
    val modifiedDate : Int,

    val maTD : Int,
    val maMG: Int,
    val maVTD : Int,
    val maSan : Int,

    val doiMot : Int,
    val doiHai : Int,
    val doiThang : Int?,

    val ngayGioDuKien : LocalDateTime = LocalDateTime.now(),
    val ngayGioThucTe : LocalDateTime = LocalDateTime.now(),

    val thoiGianDaThiDau : Float,
    val maTT : Int,
)
