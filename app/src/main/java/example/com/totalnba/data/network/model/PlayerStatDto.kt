package example.com.totalnba.data.network.model

import com.google.gson.annotations.SerializedName
import example.com.totalnba.data.base.BaseApiModel
import example.com.totalnba.data.model.PlayerStat

data class PlayerStatDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("full_name")
    val fullName: String = "",
    @SerializedName("team")
    val team: String = "",
    @SerializedName("position")
    val position: String = "",
    @SerializedName("game_played")
    val gamePlayed: Int = 0,
    @SerializedName("points_per_game")
    val pointsPerGame: Double = 0.0,
    @SerializedName("rebounds_per_game")
    val reboundsPerGame: Double = 0.0,
    @SerializedName("assists_per_game")
    val assistsPerGame: Double = 0.0,
    @SerializedName("steals_per_game")
    val stealsPerGame: Double? = 0.0,
    @SerializedName("pics_id")
    val picsId: String? = "",
) : BaseApiModel<PlayerStat> {
    override fun map(): PlayerStat =
        PlayerStat(
            id = id,
            fullName = fullName,
            team = team,
            position = position,
            gamePlayed = gamePlayed,
            pointsPerGame = pointsPerGame,
            reboundsPerGame = reboundsPerGame,
            assistsPerGame = assistsPerGame,
            stealsPerGame = stealsPerGame ?: 0.0,
            playerPicsId = picsId ?: "no_image"
        )
}
