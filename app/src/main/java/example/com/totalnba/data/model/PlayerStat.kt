package example.com.totalnba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import example.com.totalnba.data.base.BaseModel
import example.com.totalnba.util.backgroundResolverByAbbreviation

@Entity(tableName = "player_stat")
data class PlayerStat(
    @PrimaryKey
    override val id: Int = 0,
    val fullName: String = "",
    val team: String = "",
    val position: String = "",
    val gamePlayed: Int = 0,
    val pointsPerGame: Double = 0.0,
    val reboundsPerGame: Double = 0.0,
    val assistsPerGame: Double = 0.0,
    val stealsPerGame: Double = 0.0,
    val playerPicsId: String? = "",
    val pointsReboundsAssists: Double? = 0.0,
    val twoPointsMade: Double? = 0.0,
    val threePointsMade: Double? = 0.0,
    val pointsAssists: Double? = 0.0,
) : BaseModel {
    val jerseyTint get() = backgroundResolverByAbbreviation(team)
    val imageUrl get() = "https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/$playerPicsId.png"
}
