package com.example.quanlybongda.Services.Data

import com.google.gson.annotations.SerializedName

data class SearchPlayerResponse(
    val player: List<PlayerSearchItem>?
)

data class PlayerSearchItem(
    @SerializedName("idPlayer")
    val playerId: String,

    @SerializedName("strPlayer")
    val playerName: String,

    @SerializedName("idTeam")
    val teamId: String?,

    @SerializedName("strTeam")
    val teamName: String?,

    @SerializedName("dateBorn")
    val dateOfBirth: String?,

    @SerializedName("strThumb")
    val thumbnailUrl: String?,

    @SerializedName("strSport")
    val sportName: String?,

    @SerializedName("strCutout")
    val cutoutUrl: String?,

    @SerializedName("strNationality")
    val nationality: String?,

    @SerializedName("strStatus")
    val status: String?,

    @SerializedName("strGender")
    val gender: String?,

    @SerializedName("strPosition")
    val position: String?,

    @SerializedName("relevance")
    val relevance: String?
)
