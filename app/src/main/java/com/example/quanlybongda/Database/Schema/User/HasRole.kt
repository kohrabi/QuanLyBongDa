package com.example.quanlybongda.Database.Schema.User

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.quanlybongda.Database.Schema.MuaGiai

@Entity(
    primaryKeys = arrayOf("roleId", "groupId"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = UserRole::class,
            parentColumns = ["roleId"],
            childColumns = ["roleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserGroup::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),

    )
)
data class HasRole(
    val roleId : Int,
    val groupId : Int,
)