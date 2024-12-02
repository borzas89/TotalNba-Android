package example.com.totalnba.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import example.com.totalnba.data.base.BaseModel
import example.com.totalnba.util.DateUtil.stringWithMonthAndDay
import org.threeten.bp.Instant

@Entity(tableName = "result")
data class Result (
    @PrimaryKey
    val id: Int,
    val matchId: String = "",
    val matchTime: Instant,
    val homeName: String = "",
    val awayName: String = "",
    val homeScore: Int = 0,
    val awayScore: Int = 0
) : BaseModel {
    val highLightAwayWin get() = awayScore > homeScore
    val formattedMatchDate get() = matchTime.stringWithMonthAndDay
}