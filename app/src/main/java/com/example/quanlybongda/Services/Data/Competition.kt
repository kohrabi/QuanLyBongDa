package com.example.quanlybongda.Services.Data

import java.time.LocalDate
import java.time.LocalDateTime

data class Competition(
    val id: Int,
    val area: AreaInfo,
    val name: String,
    val code: String,
    val type: String,
    val emblem: String?,
    val currentSeason: Season,
    val seasons: List<Season>,
    val lastUpdated: LocalDateTime
)

data class AreaInfo(
    val id: Int,
    val name: String,
    val code: String,
    val flag: String?
)

data class Season(
    val id: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val currentMatchday: Int?,
    val winner: Team?,
    val stages: List<String>
)
