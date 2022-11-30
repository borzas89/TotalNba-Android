package example.com.totalnba.data.remote

import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.data.model.Overall
import example.com.totalnba.data.network.model.AdjustmentDto
import io.reactivex.Maybe
import io.reactivex.Single

interface AdjustmentService {
    fun getOverallsByTeams(homeName: String, awayName: String): Single<List<Overall>>
    fun getAdjustmentByTeam(teamName: String): Single<Adjustment>
}