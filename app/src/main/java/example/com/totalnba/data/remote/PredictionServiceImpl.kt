package example.com.totalnba.data.remote

import example.com.totalnba.data.base.map
import example.com.totalnba.data.model.PredictedMatch
import example.com.totalnba.data.network.TotalNbaApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PredictionServiceImpl @Inject constructor(
    private var totalNbaApi: TotalNbaApi
) : PredictionService {
    override fun getPredictedMatchesByDay(dayName: String): Single<List<PredictedMatch>> =
        totalNbaApi.getPredictedMatchesByDay(dayName)
            .map { list ->
                list.map()
            }
            .subscribeOn(Schedulers.io())
}
