package com.example.quanlybongda.Services.Data

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime

data class ResultSet(
    val count: Int,
    val first: LocalDate,
    val last: LocalDate,
    val played: Int
)

data class MatchResponse(
    val filters: Filters,
    val resultSet: ResultSet,
    val competition: CompetitionInfo,
    val matches: List<Match>,
)


data class Match(
    val id: Int,
    val area: AreaInfo,
    val competition: CompetitionInfo,
    val season: Season,
    val utcDate: LocalDateTime,
    val status: String,
    val minute: Int?,
    val injuryTime: Int?,
    val attendance: Int?,
    val venue: String?,
    val matchday: Int?,
    val stage: String,
    val group: String?,
    val lastUpdated: LocalDateTime,
    val homeTeam: MatchTeam,
    val awayTeam: MatchTeam,
    val score: Score,
    val goals: List<Goal>?,
    val penalties: List<Penalty>?,
    val bookings: List<Booking>,
    val substitutions: List<Substitution>,
    val odds: Odds?,
    val referees: List<Referee>?
)

data class MatchTeam(
    val id: Int,
    val name: String,
    val shortName: String?,
    val tla: String?,
    val crest: String?,
    val coach: MatchCoach?,
    val leagueRank: Int?,
    val formation: String?,
    val lineup: List<MatchPlayer>?,
    val bench: List<MatchPlayer>?,
    val statistics: TeamStatistics?
)

data class MatchCoach(
    val id: Int,
    val name: String,
    val nationality: String?
)

data class MatchPlayer(
    val id: Int,
    val name: String,
    val position: String?,
    val shirtNumber: Int?
)

data class TeamStatistics(
    @SerializedName("corner_kicks")
    val cornerKicks: Int?,
    @SerializedName("free_kicks")
    val freeKicks: Int?,
    @SerializedName("goal_kicks")
    val goalKicks: Int?,
    val offsides: Int?,
    val fouls: Int?,
    @SerializedName("ball_possession")
    val ballPossession: Int?,
    val saves: Int?,
    @SerializedName("throw_ins")
    val throwIns: Int?,
    val shots: Int?,
    @SerializedName("shots_on_goal")
    val shotsOnGoal: Int?,
    @SerializedName("shots_off_goal")
    val shotsOffGoal: Int?,
    @SerializedName("yellow_cards")
    val yellowCards: Int?,
    @SerializedName("yellow_red_cards")
    val yellowRedCards: Int?,
    @SerializedName("red_cards")
    val redCards: Int?
)

data class Score(
    val winner: String?,
    val duration: String,
    val fullTime: ScoreDetail,
    val halfTime: ScoreDetail
)

data class ScoreDetail(
    val home: Int?,
    val away: Int?
)

data class GoalShort(
    val minute: Int,
    val playerName: String,
    val teamCode: String,
    val action: String,
)

data class Goal(
    val minute: Int,
    val injuryTime: Int?,
    val type: String,
    val team: TeamReference,
    val scorer: PersonReference,
    val assist: PersonReference?,
    val score: ScoreDetail
)

data class Penalty(
    val player: PersonReference,
    val team: TeamReference,
    val scored: Boolean
)

data class Booking(
    val minute: Int,
    val team: TeamReference,
    val player: PersonReference,
    val card: String
)

data class Substitution(
    val minute: Int,
    val team: TeamReference,
    val playerOut: PersonReference,
    val playerIn: PersonReference
)

data class TeamReference(
    val id: Int?,
    val name: String?
)

data class PersonReference(
    val id: Int,
    val name: String
)

data class Odds(
    var homeWin: Double?,
    var draw: Double?,
    var awayWin: Double?
)

data class Referee(
    val id: Int,
    val name: String,
    val type: String,
    val nationality: String?
)
