package example.com.totalnba.ui.preview

import example.com.totalnba.data.model.Result
import example.com.totalnba.util.DateUtil.asInstant

object ResultSample {

    val results = listOf(
        Result(
            id = 10,
            matchId = "20e10265-2408-48fd-950f-5709453a1c97",
            matchTime = "2022-10-20T00:00:00Z".asInstant,
            homeName = "Minnesota Timberwolves",
            awayName = "Oklahoma City Thunder",
            homeScore = 115,
            awayScore = 108
        )
    )
}