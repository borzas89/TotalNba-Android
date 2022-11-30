package example.com.totalnba.data.remote

import example.com.totalnba.data.base.map
import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.data.model.Overall
import example.com.totalnba.data.network.TotalNbaApi
import example.com.totalnba.util.abbreviationResolverByTeamName
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AdjustmentServiceImpl @Inject constructor(
    private var totalNbaApi: TotalNbaApi
): AdjustmentService{

    override fun getOverallsByTeams(homeName: String, awayName: String): Single<List<Overall>> =
        totalNbaApi.getOverallByTeams(homeName,awayName)
            .map {
                it.map()
            }.subscribeOn(Schedulers.io())

    override fun getAdjustmentByTeam(teamName: String): Single<Adjustment> =
        totalNbaApi.getAdjustmentByTeamName(
            abbreviationResolverByTeamName(teamName)).map {
            it.map()
        }.subscribeOn(Schedulers.io())

}