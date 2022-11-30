package example.com.totalnba.data.model

import example.com.totalnba.data.base.BaseModel

data class Adjustment(
    override val id: Int,
    val team: String,
    val teamAbbreviation: String,
    val pointsPerGame: Double,
    val allowedPointsPerGame: Double,
    val wins: Int?,
    val losses: Int?
) : BaseModel