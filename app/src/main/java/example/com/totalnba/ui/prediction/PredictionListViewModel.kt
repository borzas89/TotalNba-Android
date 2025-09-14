package example.com.totalnba.ui.prediction

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.arch.BaseViewModel
import example.com.totalnba.arch.Event
import example.com.totalnba.data.model.PredictedMatch
import example.com.totalnba.data.remote.PredictionService
import example.com.totalnba.util.formattedToday
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PredictionListViewModel @Inject constructor(
    private val predictionService: PredictionService
) : BaseViewModel(), PredictionListener {
    private val _predictionList = MutableLiveData<List<PredictedMatch>>()
    val predictionList: LiveData<List<PredictedMatch>> = _predictionList
    val errorTitle = ObservableField("No events today")
    val showDetail = MutableLiveData<Event<PredictedMatch>>()
    val showResult = MutableLiveData<Event<String>>()
    val filterDay: BehaviorSubject<String> = BehaviorSubject.createDefault(
        formattedToday()
    )

    init {
        filterDay.subscribe {
            gettingMatchesByDay()
        }.addTo(compositeDisposable)
    }

    private fun gettingMatchesByDay() {
        errorTitle.set("")
        filterDay.value?.let { filteredDay ->
            predictionService.getPredictedMatchesByDay(filteredDay)
                .simpleSubscribe(
                    onSuccess = {
                        _predictionList.value = it
                        refreshStates()
                    },
                    onError = {
                        errorTitle.set("Something went wrong try again later...")
                        Log.d("TOTAL_NBA_API_DAY", it.message.toString())
                    }
                ).addTo(compositeDisposable)

        }
    }


    private fun refreshStates() {
        predictionList.value?.let {
            if (it.isEmpty()) {
                listViewVisible.set(false)
                emptyViewVisible.set(true)
                errorTitle.set("No events today")
            } else {
                listViewVisible.set(true)
                emptyViewVisible.set(false)
            }
        }
    }

    fun filterToday() {
        val format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
        val formatter = format1.format(LocalDate.now().atStartOfDay())
        filterDay.onNext(formatter)
    }

    override fun onPredictionClicked(prediction: PredictedMatch) {
        showDetail.value = Event(prediction)
    }

    override fun onTeamClicked(teamName: String, opponentName: String) {
        showResult.value = Event("$teamName|$opponentName")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}