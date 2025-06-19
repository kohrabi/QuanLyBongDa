package com.example.quanlybongda.Services.Data

import java.time.LocalDate
import java.time.LocalDateTime

data class Person(
    val id: Int,
    val name: String,
    val firstName: String?,
    val lastName: String?,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val position: String?,
    val shirtNumber: Int?,
    val lastUpdated: LocalDateTime,
    val currentTeam: CurrentTeam?
)

data class CurrentTeam(
    val area: AreaInfo,
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String,
    val crest: String?,
    val address: String,
    val website: String,
    val founded: Int,
    val clubColors: String,
    val venue: String,
    val runningCompetitions: List<CompetitionInfo>,
    val contract: Contract
)
