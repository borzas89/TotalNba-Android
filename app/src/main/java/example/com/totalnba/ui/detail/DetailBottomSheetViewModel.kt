package example.com.totalnba.ui.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.arch.BaseViewModel
import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.data.model.Overall
import example.com.totalnba.data.remote.AdjustmentService
import example.com.totalnba.util.disposedBy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class DetailBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private var adjustmentService: AdjustmentService
) : BaseViewModel() {
    val homeOverall = ObservableField<Overall>()
    val awayOverall = ObservableField<Overall>()
    val overalls = ObservableField<Pair<Overall, Overall>>()
    val awayAdjustment = ObservableField<Adjustment>()
    val homeAdjustment = ObservableField<Adjustment>()
    val matchTitle = ObservableField<String>()
    val homeWinPct = ObservableField<Double>()
    val awayWinPct = ObservableField<Double>()

    init {
        if (savedStateHandle.contains("id")) {
            val homeTeam = savedStateHandle.getLiveData("homeName", "").value
            val awayTeam = savedStateHandle.getLiveData("awayName", "").value

            if (!homeTeam.isNullOrEmpty() && !awayTeam.isNullOrEmpty()) {
                matchTitle.set("$awayTeam @ $homeTeam")
                getOverallsByTeams(homeTeam, awayTeam)
                getAdjustmentByTeams(homeTeam,awayTeam)
            }
        }
    }

    private fun getOverallsByTeams(homeName: String, awayName: String) {
        adjustmentService.getOverallsByTeams(homeName, awayName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { overall ->
                overalls.set(Pair(overall[0],overall[1]))
                homeOverall.set(overall[0])
                awayOverall.set(overall[1])
            }
            .disposedBy(compositeDisposable)
    }

    private fun getAdjustmentByTeams(homeName: String, awayName: String) {
      Single.zip(
          adjustmentService.getAdjustmentByTeam(awayName),
          adjustmentService.getAdjustmentByTeam(homeName)
      ){ away, home  ->
          awayAdjustment.set(away)
          homeAdjustment.set(home)
          calculateWinPercentage(away, home)
      }.subscribe().disposedBy(compositeDisposable)
    }

    private fun log5(home: Double, away: Double): Double {
        return (home - home*away ) / ( home+away -2 * home * away )
    }

    private fun calculateWinPercentage(away: Adjustment, home: Adjustment) {
        val calculatedHomeWinPct =((0.6*log5(home.homeWinPct ?: 0.0,
            away.awayWinPct ?: 0.0) + 0.2*log5( home.totalPct ?: 0.0,
            away.totalPct ?: 0.0)
               + 0.2 *log5(home.lastTenStreak ?: 0.0,away.lastTenStreak ?: 0.0)
                )*100)
        homeWinPct.set(calculatedHomeWinPct)
        awayWinPct.set(100 - calculatedHomeWinPct)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}