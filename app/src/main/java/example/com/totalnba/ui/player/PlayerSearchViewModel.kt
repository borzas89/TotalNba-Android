package example.com.totalnba.ui.player

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.arch.BaseViewModel
import example.com.totalnba.data.model.PlayerStat
import example.com.totalnba.data.remote.NbaPlayerIdService
import example.com.totalnba.data.remote.PlayerStatService
import example.com.totalnba.util.toFlowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PlayerSearchViewModel @Inject constructor(
    private val playerStatService: PlayerStatService,
    private val nbaPlayerIdService: NbaPlayerIdService
) : BaseViewModel() {
    val searchText = ObservableField("")
    private val _playerStatList = MutableLiveData<List<PlayerStat>>()
    val playerStatList: LiveData<List<PlayerStat>> = _playerStatList

    // Track which players we've already tried to fetch IDs for
    private val fetchedPlayerIds = mutableSetOf<String>()

    private val suggestions =
        searchText
            .toFlowable()
            .debounce(125L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .switchMap { query ->
                playerStatService.searchPlayers(query)
            }.observeOn(AndroidSchedulers.mainThread())

    init {
        playerStatService.updatePlayerStats()
            .subscribe()
           .addTo(compositeDisposable)

        searchText.toFlowable()
            .subscribe()
            .addTo(compositeDisposable)

        suggestions.subscribeBy {
            _playerStatList.value = it
            refreshStates()

            // Fetch player IDs for players without pics_id (but only once per player)
            it.forEach { player ->
                val needsFetch = (player.playerPicsId.isNullOrEmpty() || player.playerPicsId == "no_image")
                val notYetFetched = !fetchedPlayerIds.contains(player.fullName)

                if (needsFetch && notYetFetched) {
                    fetchedPlayerIds.add(player.fullName)
                    fetchPlayerIdInBackground(player.fullName)
                }
            }
        }.addTo(compositeDisposable)
    }

    /**
     * Fetch player ID from API and update database
     * Runs in background without blocking UI
     */
    private fun fetchPlayerIdInBackground(playerName: String) {
        nbaPlayerIdService.fetchAndUpdatePlayerId(playerName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    Timber.d("Successfully fetched player ID for $playerName - refreshing list")
                    // Force refresh by re-querying with current search text
                    val currentQuery = searchText.get() ?: ""
                    if (currentQuery.isNotBlank()) {
                        // Trigger observable update
                        searchText.notifyChange()
                    }
                },
                onError = { error ->
                    Timber.e(error, "Failed to fetch player ID for $playerName")
                }
            )
            .addTo(compositeDisposable)
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