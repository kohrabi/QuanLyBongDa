package com.example.quanlybongda.Database.DAO.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.User.UserGroup
import com.example.quanlybongda.Database.Schema.User.UserRole

@Dao
interface UserGroupDAO {
    @Upsert
    suspend fun upsertGroup(vararg groups : UserGroup);

    @Delete
    suspend fun deleteGroup(vararg groups : UserGroup);

    @Query("SELECT * FROM UserGroup")
    suspend fun selectAllUserGroup() : List<UserGroup>;
}