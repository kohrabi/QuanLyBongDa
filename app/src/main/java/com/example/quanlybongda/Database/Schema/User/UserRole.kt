package com.example.quanlybongda.Database.Schema.User

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRole(
    @PrimaryKey(autoGenerate = true)
    val roleId : Int,
    val roleName : String,
    val viewablePage : String,
    val canEdit : Boolean,
)