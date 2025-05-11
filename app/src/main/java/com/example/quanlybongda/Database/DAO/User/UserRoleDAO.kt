package com.example.quanlybongda.Database.DAO.User

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.quanlybongda.Database.Schema.User.HasRole
import com.example.quanlybongda.Database.Schema.User.UserRole

@Dao
interface UserRoleDAO {
    @Upsert
    suspend fun upsertRole(vararg roles : UserRole);

    @Delete
    suspend fun deleteRole(vararg roles : UserRole);

    @Query("""
        SELECT UserRole.* FROM HasRole
        INNER JOIN UserRole ON UserRole.roleId = HasRole.roleId
        WHERE HasRole.groupId=:groupId
        GROUP BY UserRole.roleId
    """)
    fun selectRolesInGroup(groupId : Int) : LiveData<List<UserRole>>;

    @Query("SELECT HasRole.roleId FROM HasRole WHERE HasRole.groupId=:groupId")
    suspend fun selectRoleIdsInGroup(groupId : Int) : List<Int>;

    @Insert
    suspend fun upsertHasRole(hasRoles: List<HasRole>);

    @Insert
    suspend fun upsertHasRole(vararg hasRoles: HasRole);

    @Delete
    suspend fun deleteHasRole(hasRoles : List<HasRole>);
}