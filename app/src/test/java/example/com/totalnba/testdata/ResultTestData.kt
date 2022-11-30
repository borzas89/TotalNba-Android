package example.com.totalnba.testdata

import example.com.totalnba.data.network.model.ResultDto
import example.com.totalnba.data.model.Result
import example.com.totalnba.util.DateUtil.asInstant
import org.threeten.bp.Instant

object ResultTestData {
    val default = createResult()
    val models = listOf(createResult())
    val dtos = listOf(createResultDto())
}

fun createResult(
    id: Int = 1,
    matchId: String = "20e10265-2408-48fd-950f-5709453a1c97",
    matchTime: Instant = "2022-10-20T00:00:00Z".asInstant,
    homeName: String = "Minnesota Timberwolves",
    awayName: String = "Oklahoma City Thunder",
    homeScore: Int = 115,
    awayScore: Int = 108
): Result =
    Result(
        id = id,
        matchId = matchId,
        matchTime = matchTime,
        homeName = homeName,
        awayName = awayName,
        homeScore = homeScore,
        awayScore = awayScore,
    )

fun createResultDto(
    id: Int = 1,
    matchId: String =  "20e10265-2408-48fd-950f-5709453a1c97",
    matchTime: String = "2022-10-20T00:00:00Z",
    homeName: String = "Minnesota Timberwolves",
    awayName: String = "Oklahoma City Thunder",
    homeScore: Int = 115,
    awayScore: Int = 108
): ResultDto =
    ResultDto(
        id = id,
        matchId = matchId,
        matchTime = matchTime,
        homeName = homeName,
        awayName = awayName,
        homeScore = homeScore,
        awayScore = awayScore
    )