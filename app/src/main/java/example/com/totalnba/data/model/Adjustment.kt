package example.com.totalnba.data.model

import example.com.totalnba.data.base.BaseModel

data class Adjustment(
    val id: Int,
    val team: String,
    val teamAbbreviation: String,
    val pointsPerGame: Double? = 0.0,
    val allowedPointsPerGame: Double? = 0.0,
    val wins: Int?,
    val losses: Int?,
    val awayWinPct: Double?,
    val homeWinPct: Double?,
    val lastTenStreak: Double?,
    val totalPct: Double?
) : BaseModel