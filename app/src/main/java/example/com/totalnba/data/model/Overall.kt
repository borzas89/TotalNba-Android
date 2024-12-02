package example.com.totalnba.data.model

import example.com.totalnba.data.base.BaseModel

data class Overall(
    val id: Int,
    val teamName: String? = null,
    val overall: Double? = null,
    val awayOverall: Double? = null,
    val homeOverall: Double? = null,
    val teamAvg: Double? = null,
    val teamHomeAvg: Double? = null,
    val teamAwayAvg: Double? = null
): BaseModel