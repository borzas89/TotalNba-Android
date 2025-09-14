package example.com.totalnba.ui.result

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.R
import example.com.totalnba.arch.BaseViewModel
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

private const val CURRENT_SEASON_START = "2024-10-24T00:00:00.63Z"
@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resultService: ResultService,
    private val adjustmentService: AdjustmentService
) : BaseViewModel() {
    private val errorTitle = ObservableField<String>()
    private var allResults: List<Result> = emptyList()
    private var opponentTeamName: String = ""

    val teamColorState: MutableState<Int> = mutableStateOf(R.color.team_purple)
    val teamImageState: MutableState<Int> = mutableStateOf(R.drawable.splash_icon)
    val teamResultListState: MutableState<List<Result>> = mutableStateOf(emptyList())
    val teamNameState: MutableState<String> = mutableStateOf("")
    val opponentNameState: MutableState<String> = mutableStateOf("")
    val adjustment: MutableState<Adjustment?> = mutableStateOf(null)
    val currentFilter: MutableState<FilterType> = mutableStateOf(FilterType.ALL_GAMES)
    val showFilterDialog: MutableState<Boolean> = mutableStateOf(false)

    init {
        if (savedStateHandle.contains("teamName")) {
            savedStateHandle.getLiveData("teamName", "").value?.let { teamName ->
                teamNameState.value = teamName
                teamColorState.value = backgroundResolverId(teamName)
                teamImageState.value = imageResolverId(teamName)

                // Get opponent team name if available
                savedStateHandle.getLiveData("opponentName", "").value?.let { opponent ->
                    opponentTeamName = opponent
                    opponentNameState.value = opponent
                }

                getTeamResults(teamName)
                getTeamAdjustments(teamName)
            }
        }
    }

    private fun applyFilter() {
        val filteredResults = when (currentFilter.value) {
            FilterType.ALL_GAMES -> allResults
            FilterType.LAST_TEN_GAMES -> allResults
                .sortedByDescending { it.matchTime }
                .take(10)
            FilterType.HOME_GAMES -> allResults.filter { result ->
                result.homeName == teamNameState.value
            }
            FilterType.AWAY_GAMES -> allResults.filter { result ->
                result.awayName == teamNameState.value
            }
            FilterType.HEAD_TO_HEAD -> allResults.filter { result ->
                (result.homeName == teamNameState.value && result.awayName == opponentTeamName) ||
                (result.awayName == teamNameState.value && result.homeName == opponentTeamName)
            }
        }
        teamResultListState.value = filteredResults
    }

    fun onFilterSelected(filterType: FilterType) {
        currentFilter.value = filterType
        applyFilter()
        showFilterDialog.value = false
    }

    fun showFilterDialog() {
        showFilterDialog.value = true
    }

    fun hideFilterDialog() {
        showFilterDialog.value = false
    }

    private fun getTeamResults(teamName: String) {
        resultService.updateResultsByTeam(teamName)
            .subscribeOn(Schedulers.io())
            .andThen(resultService.observeResultsByTeam(teamName))
            .subscribeBy(
                onNext = { result ->
                    allResults = result.filter {
                        it.matchTime
                            .isAfter(Instant.parse(CURRENT_SEASON_START))
                    }
                    applyFilter()
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

}