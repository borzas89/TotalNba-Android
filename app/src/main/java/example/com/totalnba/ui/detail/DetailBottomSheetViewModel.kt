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
        val clampedHome = home.coerceIn(0.01, 0.99)
        val clampedAway = away.coerceIn(0.01, 0.99)
        return (clampedHome - clampedHome * clampedAway) / (clampedHome + clampedAway - 2 * clampedHome * clampedAway)
    }

    private fun calculateWinPercentage(away: Adjustment, home: Adjustment) {
        val homeWinPctValue = home.homeWinPct ?: 0.5
        val awayWinPctValue = away.awayWinPct ?: 0.5
        val homeTotalPct = home.totalPct ?: 0.5
        val awayTotalPct = away.totalPct ?: 0.5
        val homeLastTen = home.lastTenStreak ?: 0.5
        val awayLastTen = away.lastTenStreak ?: 0.5

        val calculatedHomeWinPct = ((0.6 * log5(homeWinPctValue, awayWinPctValue) +
                                   0.2 * log5(homeTotalPct, awayTotalPct) +
                                   0.2 * log5(homeLastTen, awayLastTen)) * 100)

        homeWinPct.set(calculatedHomeWinPct.coerceIn(5.0, 95.0))
        awayWinPct.set((100 - calculatedHomeWinPct).coerceIn(5.0, 95.0))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}