package com.example.quanlybongda.Database.Schema.User

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["userId", "maTD"],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        )
    )
)
data class CaDo(
    val userId: Int,
    val maTD: Int,
    val soTien: Int = 0,
    val tiSoDoiMot: Int = 0,
    val tiSoDoiHai: Int = 0,
)