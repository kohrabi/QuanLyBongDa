package com.example.quanlybongda.Database.Schema.User

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["userId", "maDoi"],
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
    val userId: Int,
    val maDoi: Int,
)

@Entity(
    primaryKeys = ["userId", "maCT"],
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
    val userId: Int,
    val maCT: Int,
)