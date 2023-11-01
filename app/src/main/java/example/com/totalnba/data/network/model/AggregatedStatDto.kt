package example.com.totalnba.data.network.model

import com.google.gson.annotations.SerializedName

data class AggregatedStatDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("player")
    val player: String,
    @SerializedName("team")
    val team: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("two_points_made")
    val twoPointsMade: Double,
    @SerializedName("three_points_made")
    val threePointsMade: Double,
    @SerializedName("pra")
    val pra: Double,
    @SerializedName("points_assists")
    val pointsAssists: Double
)