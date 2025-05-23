package com.example.quanlybongda.Database.ReturnTypes

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.quanlybongda.Database.Schema.CauThu
import java.time.LocalDate

data class CauThuBanThang(
    @Embedded
    val cauThu: CauThu,
    @ColumnInfo
    val banThang: Int,
)
