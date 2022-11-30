package example.com.totalnba.data.remote

import example.com.totalnba.data.model.PredictedMatch
import io.reactivex.Single

interface PredictionService {
    fun getPredictedMatchesByDay(dayName: String) : Single<List<PredictedMatch>>
}