package com.example.quanlybongda.Services.Data

import java.time.LocalDate
import java.time.LocalDateTime

data class TeamResponse(
    val count: Int,
    val filters: Filters,
    val competition: CompetitionInfo,
    val season: Season,
    val teams: List<Team>,
)

data class Team(
    val id: Int,
    val area: AreaInfo,
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
    val coach: Coach,
    val marketValue: Long?,
    val squad: List<Player>,
    val staff: List<Staff>?,
    val lastUpdated: LocalDateTime
)

data class CompetitionInfo(
    val id: Int,
    val name: String,
    val code: String,
    val type: String,
    val emblem: String?
)

data class Coach(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val name: String,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val contract: Contract
)

data class Player(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val name: String,
    val position: String?,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val shirtNumber: Int?,
    val marketValue: Long?,
    val contract: Contract?
)

data class Staff(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val name: String,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val contract: Contract
)

data class Contract(
    val start: String,
    val until: String
)
