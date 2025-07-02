package com.example.quanlybongda.Services

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig

val jsonSchema = Schema.obj(
    mapOf(
        "homeTeam" to Schema.string(),
        "awayTeam" to Schema.string(),
        "homeTeamWinPercentage" to Schema.float(),
        "awayTeamWinPercentage" to Schema.float(),
        "drawPercentage" to Schema.float(),
    )
)

data class OddResponse(
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamWinPercentage: Float,
    val awayTeamWinPercentage: Float,
    val drawPercentage: Float
)

object GenerativeModel {
    val prompt = """
        Given homeTeam, awayTeam, and season in a competition, return their winning odds in percentage in JSON format.
        homeTeam: {homeTeam}, awayTeam: {awayTeam}, Competition: {competition}, Season: {season}
    """.trimIndent()
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.0-flash-lite-001", generationConfig {
            responseMimeType = "application/json"
            responseSchema = jsonSchema
        })
}