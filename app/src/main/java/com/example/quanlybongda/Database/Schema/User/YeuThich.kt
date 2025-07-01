package com.example.quanlybongda.Database.Schema.User

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        )
    )
)
data class YeuThichDoiBong(
    @PrimaryKey
    val userId: Int,
    val maDoi: Int,
)

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        )
    )
)
data class YeuThichCauThu(
    @PrimaryKey
    val userId: Int,
    val maCT: Int,
)