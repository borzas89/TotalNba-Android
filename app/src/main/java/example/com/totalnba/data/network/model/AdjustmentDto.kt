package example.com.totalnba.data.network.model

import com.google.gson.annotations.SerializedName
import example.com.totalnba.data.base.BaseApiModel
import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.util.teamNameResolverByAbbreviation

data class AdjustmentDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("team")
    val team: String,

    @SerializedName("away_off_eff")
    val awayOffEff: Double,

    @SerializedName("home_off_eff")
    val homeOffEff: Double,

    @SerializedName("away_def_eff")
    val awayDefEff: Double,

    @SerializedName("home_def_eff")
    val homeDefEff: Double,

    @SerializedName("away_tempo")
    val awayTempo: Double,

    @SerializedName("home_tempo")
    val homeTempo: Double,

    @SerializedName("away_ed")
    val awayEd: Double,

    @SerializedName("home_ed")
    val homeEd: Double,

    @SerializedName("points_per_game")
    val pointsPerGame: Double,

    @SerializedName("allowed_points_per_game")
    val allowedPointsPerGame: Double,

    @SerializedName("wins")
    val wins: Int?,

    @SerializedName("losses")
    val losses: Int?

): BaseApiModel<Adjustment> {
    override fun map(): Adjustment =
        Adjustment(
            id = id,
            team = teamNameResolverByAbbreviation(team),
            teamAbbreviation = team,
            pointsPerGame = pointsPerGame,
            allowedPointsPerGame = allowedPointsPerGame,
            wins = wins?: 0,
            losses = losses ?: 0
        )
}