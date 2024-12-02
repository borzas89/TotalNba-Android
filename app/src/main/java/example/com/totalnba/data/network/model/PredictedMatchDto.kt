package example.com.totalnba.data.network.model

import com.google.gson.annotations.SerializedName;
import example.com.totalnba.data.base.BaseApiModel
import example.com.totalnba.data.model.PredictedMatch
import java.util.*

data class PredictedMatchDto(

    @SerializedName("commonMatchId")
    val commonMatchId: String,

    @SerializedName("homeTeamName")
    val homeTeam: String? = null,

    @SerializedName("awayTeamName")
    val awayTeam: String? = null,

    @SerializedName("homeTeamAlias")
    val homeAlias: String? = null,

    @SerializedName("awayTeamAlias")
    val awayAlias: String? = null,

    @SerializedName("predictedScore")
    val predictedScore: Double? = null,

    @SerializedName("predictedHomeScore")
    val predictedHomeScore: Double? = null,

    @SerializedName("predictedAwayScore")
    val predictedAwayScore: Double? = null,

    @SerializedName("predictedTotal")
    val predictedTotal: Double? = null,

    @SerializedName("spread")
    val spread: Double? = null,

    @SerializedName("weekName")
    val weekName: String? = null,

    @SerializedName("weekNumber")
    val weekNumber: Int? = null,

    @SerializedName("matchDate")
    val matchDate: Date? = null

) : BaseApiModel<PredictedMatch>{
    override fun map(): PredictedMatch = PredictedMatch(
        commonMatchId = commonMatchId,
        matchTitle  = "$awayTeam @ $homeTeam",
        awayTeam = awayTeam,
        homeTeam = homeTeam,
        homeAlias = homeAlias,
        awayAlias = awayAlias,
        total = predictedTotal,
        awayScore = predictedAwayScore,
        homeScore = predictedHomeScore,
        spread = spread,
        weekName = weekName,
        weekNumber = weekNumber,
        matchDate = matchDate
    )
}