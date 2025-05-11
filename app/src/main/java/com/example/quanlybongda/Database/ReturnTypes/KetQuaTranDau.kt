package com.example.quanlybongda.Database.ReturnTypes

import com.example.quanlybongda.Database.Schema.DoiBong

data class KetQuaTranDau(
    val doiMot: DoiBong,
    val doiHai: DoiBong,
    val tySo: String,
    val san : String,
    val ngayGio: String
)
