package example.com.totalnba.data.network.model

import com.google.gson.annotations.SerializedName
import example.com.totalnba.data.base.BaseApiModel
import example.com.totalnba.data.model.Overall
import example.com.totalnba.data.model.PlayerStat
import kotlin.random.Random

data class OverallDto(
    @SerializedName("teamName")
    val teamName: String? = null,

    @SerializedName("overall")
    val overall: Double? = null,

    @SerializedName("awayOverall")
    val awayOverall: Double? = null,

    @SerializedName("homeOverall")
    val homeOverall: Double? = null,

    @SerializedName("teamAvg")
    val teamAvg: Double? = null,

    @SerializedName("teamHomeAvg")
    val teamHomeAvg: Double? = null,

    @SerializedName("teamAwayAvg")
    val teamAwayAvg: Double? = null,
): BaseApiModel<Overall>{
    override fun map(): Overall = Overall(
        // TODO id from backend instead of this...
        id = Random.nextInt(),
        teamName = teamName,
        overall = overall,
        awayOverall = awayOverall,
        homeOverall = homeOverall,
        teamAvg = teamAvg,
        teamHomeAvg = teamHomeAvg,
        teamAwayAvg = teamAwayAvg
    )
}