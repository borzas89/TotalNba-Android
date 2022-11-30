package example.com.totalnba.data.model

import example.com.totalnba.data.base.BaseModel
import java.util.*

data class PredictedMatch(
    override val id: Int,
    val matchTitle: String? = null,
    val homeTeam: String? = null,
    val awayTeam: String? = null,
    val homeAlias: String? = null,
    val awayAlias: String? = null,
    val homeScore: Double? = null,
    val awayScore: Double? = null,
    val total: Double? = null,
    val spread: Double? = null,
    val weekName: String? = null,
    val weekNumber: Int? = null,
    val matchDate: Date? = null
): BaseModel