package com.example.quanlybongda.Database.Exceptions

class UsernameFormat : RuntimeException("UsernameFormat")
class EmailFormat : RuntimeException("EmailFormat")

class EmailAvailability : RuntimeException("EmailAvailability")
class IncorrectUsername : RuntimeException("IncorrectUsername")
class IncorrectPassword : RuntimeException("IncorrectPassword")