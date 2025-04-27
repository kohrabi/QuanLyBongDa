package com.example.quanlybongda.Database.Schema.User

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserGroup(
    @PrimaryKey(autoGenerate = true)
    val groupId : Int,
    val groupName : String,
)