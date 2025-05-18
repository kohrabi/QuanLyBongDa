package com.example.quanlybongda.Database.DAO.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.User.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Upsert
    suspend fun upsertUser(vararg user : User);

    @Delete
    suspend fun deleteUser(vararg user: User);

//    @Query("UPDATE User SET passwordHash=:passwordHash WHERE id=:id")
//    suspend fun updateUserPasswordHash(id: Int, passwordHash: String);

    @Query("SELECT * FROM User WHERE email=:email LIMIT 1")
    suspend fun selectUserFromEmail(email : String) : User?;

    @Query("SELECT passwordHash FROM User WHERE id=:id LIMIT 1")
    suspend fun selectUserPasswordHash(id: Int) : String?;

    @Query("SELECT * FROM User")
    suspend fun selectAllUsers() : List<User>;

    @Query("SELECT * FROM User")
    fun selectAllUserFlow() : Flow<List<User>>;
}