package example.com.totalnba.ui.result

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.R
import example.com.totalnba.arch.BaseViewModel
import example.com.totalnba.arch.Event
import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.data.model.Result
import example.com.totalnba.data.remote.AdjustmentService
import example.com.totalnba.data.remote.ResultService
import example.com.totalnba.util.backgroundResolverId
import example.com.totalnba.util.disposedBy
import example.com.totalnba.util.imageResolverId
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Instant
import timber.log.Timber
import javax.inject.Inject

private const val CURRENT_SEASON_START = "2023-10-24T00:00:00.63Z"
@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resultService: ResultService,
    private val adjustmentService: AdjustmentService
) : BaseViewModel(), ResultViewModelUiActions {
    private val errorTitle = ObservableField<String>()

    val teamColorState: MutableState<Int> = mutableStateOf(R.color.team_purple)
    val teamImageState: MutableState<Int> = mutableStateOf(R.drawable.splash_icon)
    val teamResultListState: MutableState<List<Result>> = mutableStateOf(emptyList())
    val teamNameState: MutableState<String> = mutableStateOf("")
    val adjustment: MutableState<Adjustment?> = mutableStateOf(null)
    val navigateBack = MutableLiveData<Event<String>>()

    init {
        if (savedStateHandle.contains("teamName")) {
            savedStateHandle.getLiveData("teamName", "").value?.let { teamName ->
                teamNameState.value = teamName
                teamColorState.value = backgroundResolverId(teamName)
                teamImageState.value = imageResolverId(teamName)
                getTeamResults(teamName)
                getTeamAdjustments(teamName)
            }
        }
    }

    fun filterTeamResult(){
        teamResultListState.value.filter {
            it.awayName == teamNameState.value
        }
    }

    private fun getTeamResults(teamName: String) {
        resultService.updateResultsByTeam(teamName)
            .subscribeOn(Schedulers.io())
            .andThen(resultService.observeResultsByTeam(teamName))
            .subscribeBy(
                onNext = { result ->
                    teamResultListState.value = result.filter {
                        it.matchTime
                            .isAfter(Instant.parse(CURRENT_SEASON_START))
                    }
                },
                onError = {
                    errorTitle.set("Something went wrong...")
                }
            ).addTo(compositeDisposable)
    }

    private fun getTeamAdjustments(teamName: String) {
        adjustmentService.getAdjustmentByTeam(teamName)
            .subscribeBy(
                onSuccess = { adjustment.value = it },
                onError = { Timber.d("API_ADJUSTMENT %s", it.message.toString()) }
            )
            .disposedBy(compositeDisposable)
    }

    override val navigateUp: () -> Unit = {
        navigateBack.value = Event("back")
    }
}

interface ResultViewModelUiActions {
    val navigateUp: () -> Unit
}