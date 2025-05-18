package com.example.quanlybongda.Database.ReturnTypes

import androidx.room.Embedded
import androidx.room.Relation
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.Database.Schema.Loai.SanNha

data class DoiBongTenSan(
    @Embedded
    val doiBong: DoiBong,
    @Relation(parentColumn = "maSan", entityColumn = "maSan")
    val sanNha : SanNha
)
