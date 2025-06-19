package com.example.quanlybongda.Services.Data

import com.google.gson.annotations.SerializedName

data class Area(
    val id: Int,
    val name: String,
    val code: String,
    val flag: String?,
    val parentAreaId: Int?,
    val parentArea: String?,
    val childAreas: List<ChildArea>? = null
)

data class ChildArea(
    val id: Int,
    val name: String,
    @SerializedName("countryCode")
    val code: String?,
    val flag: String?,
    val parentAreaId: Int?,
    val parentArea: String?
)
