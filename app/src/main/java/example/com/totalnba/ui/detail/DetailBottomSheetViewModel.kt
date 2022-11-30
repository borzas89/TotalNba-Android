package example.com.totalnba.ui.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.arch.BaseViewModel
import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.data.model.Overall
import example.com.totalnba.data.remote.AdjustmentService
import example.com.totalnba.util.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class DetailBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private var adjustmentService: AdjustmentService
) : BaseViewModel() {
    val homeOverall = ObservableField<Overall>()
    val awayOverall = ObservableField<Overall>()
    val awayAdjustment = ObservableField<Adjustment>()
    val homeAdjustment = ObservableField<Adjustment>()
    val matchTitle = ObservableField<String>()

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
                homeOverall.set(overall[0])
                awayOverall.set(overall[1])
            }
            .disposedBy(compositeDisposable)
    }

    private fun getAdjustmentByTeams(homeName: String, awayName: String) {
        adjustmentService.getAdjustmentByTeam(homeName)
            .subscribeBy {
                homeAdjustment.set(it)
            }.disposedBy(compositeDisposable)

        adjustmentService.getAdjustmentByTeam(awayName)
            .subscribeBy {
                awayAdjustment.set(it)
            }.disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}