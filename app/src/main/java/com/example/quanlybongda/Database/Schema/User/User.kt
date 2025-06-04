package com.example.quanlybongda.Database.Schema.User

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = arrayOf(
        Index(value = arrayOf("email"), unique = true),
        Index(value = arrayOf("id"), name="User_id", unique = true),
        Index(value = arrayOf("email"), name="User_email_unique", unique = true),
        Index(value = arrayOf("groupId"), name="User_Group_id")
    ),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = UserGroup::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.SET_DEFAULT
        )
    )
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email : String,
    val passwordHash : String,
    val username : String,
    @ColumnInfo(defaultValue = "1")
    val groupId : Int = 1,
) {
    @Ignore var groupName : String = "";
}

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Session(
    @PrimaryKey
    val sessionId: String,
    val userId: Int,
    var expiresAt: LocalDateTime = LocalDateTime.now(),
)