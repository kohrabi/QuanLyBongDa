package com.example.quanlybongda.Database.ReturnTypes

import androidx.room.Embedded

data class UserGroupRoles(
    @Embedded
    val groupId : Int,
    @Embedded
    val groupName : String,
    @Embedded
    val roles : List<Int>,
)
