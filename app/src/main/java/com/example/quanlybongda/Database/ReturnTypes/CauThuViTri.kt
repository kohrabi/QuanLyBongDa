package com.example.quanlybongda.Database.ReturnTypes

import androidx.room.Embedded
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.Database.Schema.Loai.ViTri

data class CauThuViTri(
    @Embedded
    val cauThu: CauThu,
    @Embedded
    val viThu: ViTri
)