package com.example.quanlybongda.Database

import androidx.room.TypeConverter
import java.security.MessageDigest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

fun hashPassword(password: String) : String {
    //https://www.samclarke.com/kotlin-hash-strings/
    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest
        .getInstance("SHA-1")
        .digest(password.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }
    return result.toString();
}

fun verifyUsernameInput(userName: String) : Boolean {
    return userName.length > 3 && userName.length < 32 && userName.trim() == userName;
}

fun verifyEmailInput(email: String): Boolean {
    return email.matches(Regex("/^.+@.+..+$/")) && email.length < 256;
}