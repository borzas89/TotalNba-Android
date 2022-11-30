package example.com.totalnba.ui.player

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.arch.BaseViewModel
import example.com.totalnba.data.model.PlayerStat
import example.com.totalnba.data.remote.PlayerStatService
import example.com.totalnba.util.toFlowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PlayerSearchViewModel @Inject constructor(
    private val playerStatService: PlayerStatService
) : BaseViewModel() {
    val searchText = ObservableField("")
    private val _playerStatList = MutableLiveData<List<PlayerStat>>()
    val playerStatList: LiveData<List<PlayerStat>> = _playerStatList

    private val suggestions =
        searchText
            .toFlowable()
            .debounce(125L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .switchMap { query ->
                playerStatService.searchPlayers(query)
            }.observeOn(AndroidSchedulers.mainThread())

    init {
        playerStatService.updatePlayerStats()
            .subscribe().addTo(compositeDisposable)

        searchText.toFlowable()
            .subscribe()
            .addTo(compositeDisposable)

        suggestions.subscribeBy {
            _playerStatList.value = it
            refreshStates()
        }.addTo(compositeDisposable)

    }

    private fun refreshStates() {
        playerStatList.value?.let {
            if (it.isEmpty()) {
                listViewVisible.set(false)
                emptyViewVisible.set(true)
            } else {
                listViewVisible.set(true)
                emptyViewVisible.set(false)
            }
        }
    }
}