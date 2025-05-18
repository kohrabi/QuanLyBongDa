package com.example.quanlybongda.Database.Schema

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ThamSo(
    @PrimaryKey
    val tenThamSo: String,
    val giaTri : Int,
)