package example.com.totalnba.data.network.model

import com.google.gson.annotations.SerializedName
import example.com.totalnba.data.base.BaseApiModel
import example.com.totalnba.data.model.Result
import example.com.totalnba.util.DateUtil.asInstant

data class ResultDto(

    @SerializedName("id")
    val id: Int,

    @SerializedName("match_id")
    val matchId: String = "",

    @SerializedName("league_id")
    val leagueId: String? = null,

    @SerializedName("league_name")
    val leagueName: Any? = null,

    @SerializedName("quarter_count")
    val quarterCount: Int? = null,

    @SerializedName("match_time")
     val matchTime: String? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("home_name")
    val homeName: String = "",

    @SerializedName("away_name")
    val awayName: String = "",

    @SerializedName("home_score")
    val homeScore: Int = 0,

    @SerializedName("away_score")
    val awayScore: Int = 0,

    @SerializedName("explain")
    val explain: String? = null,

    @SerializedName("neutral")
    val neutral: Boolean? = null
) : BaseApiModel<Result> {
    override fun map(): Result =
        Result(
            id = id,
            matchTime = matchTime.let {
                it ?: ""
            }.asInstant,
            matchId = matchId,
            awayName = awayName,
            homeName = homeName,
            awayScore = awayScore,
            homeScore = homeScore
        )
}