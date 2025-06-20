package com.example.quanlybongda.Services.Data

data class StandingsResponse(
    val filters: Filters,
    val area: AreaInfo,
    val competition: CompetitionInfo,
    val season: Season,
    val standings: List<Standing>
)

data class Filters(
    val season: String
)

data class Standing(
    val stage: String,
    val type: String,
    val group: String?,
    val table: List<TeamStandingInfo>
)

data class TeamStandingInfo(
    val position: Int,
    val team: StandingTeam,
    val playedGames: Int,
    val form: String,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int
)

data class StandingTeam(
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String,
    val crest: String?
)
