package com.example.quanlybongda.Database.ReturnTypes

import androidx.room.Embedded
import com.example.quanlybongda.Database.Schema.User.Session
import com.example.quanlybongda.Database.Schema.User.User


data class SessionValidationResult(
    @Embedded
    val user: User? = null,
    @Embedded
    val session: Session? = null,
)
