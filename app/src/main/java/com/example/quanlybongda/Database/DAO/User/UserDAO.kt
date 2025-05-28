package com.example.quanlybongda.Database.DAO.User

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.quanlybongda.Database.ReturnTypes.SessionValidationResult
import com.example.quanlybongda.Database.Schema.User.Session
import com.example.quanlybongda.Database.Schema.User.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface UserDAO {
    @Upsert
    suspend fun upsertUser(vararg user : User);

    @Upsert
    suspend fun upsertSession(vararg session: Session);

    @Query("DELETE FROM Session WHERE sessionId=:id")
    suspend fun deleteSession(id : String);

    @Query("DELETE FROM Session WHERE userId=:userId")
    suspend fun deleteAllUserSession(userId : Int);

    @Query(
        """
        SELECT User.*, Session.* FROM Session
        INNER JOIN User ON Session.userId=User.id
        WHERE Session.sessionId=:sessionId
    """
    )
    suspend fun selectUserSession(sessionId: String) : List<SessionValidationResult>;

    @Query("""UPDATE Session SET expiresAt=:expiresAt WHERE sessionId=:sessionId """)
    suspend fun updateSessionExpiration(sessionId: String, expiresAt : LocalDateTime);

    @Query("DELETE FROM User WHERE id=:userId")
    suspend fun deleteUser(userId : Int);

    @Query("SELECT * FROM User WHERE email=:email LIMIT 1")
    suspend fun selectUserFromEmail(email : String) : User?;

    @Query("SELECT * FROM User WHERE username=:username LIMIT 1")
    suspend fun selectUserFromUsername(username : String) : User?;

    @Query("SELECT passwordHash FROM User WHERE id=:id LIMIT 1")
    suspend fun selectUserPasswordHash(id: Int) : String?;

    @Query("SELECT * FROM User")
    suspend fun selectAllUsers() : List<User>;

    @Query("SELECT * FROM User")
    fun selectAllUserFlow() : Flow<List<User>>;

    @Query("UPDATE User SET passwordHash=:passwordHash WHERE id=:userId")
    suspend fun updateUserPasswordHash(userId: Int, passwordHash : String);

}