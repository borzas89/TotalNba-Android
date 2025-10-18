package example.com.totalnba.data.remote

import example.com.totalnba.data.database.dao.PlayerStatDao
import example.com.totalnba.data.network.TotalNbaApi
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NbaPlayerIdService @Inject constructor(
    private val totalNbaApi: TotalNbaApi,
    private val playerStatDao: PlayerStatDao
) {

    /**
     * Fetch player ID from backend API by player name and update the database
     */
    fun fetchAndUpdatePlayerId(playerName: String): Completable {
        Timber.d("Fetching player ID from backend API: $playerName")
        return totalNbaApi.searchNbaPlayers(playerName)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { players ->
                val matchingPlayer = players.firstOrNull()

                if (matchingPlayer != null) {
                    Timber.d("Found player ID from backend for $playerName: ${matchingPlayer.personId}")

                    // Update the player in database
                    playerStatDao.updatePlayerPicsId(
                        fullName = playerName,
                        picsId = matchingPlayer.personId.toString()
                    )
                } else {
                    Timber.w("Player not found in backend: $playerName")
                    Completable.complete()
                }
            }
            .onErrorComplete { error ->
                Timber.e(error, "Error fetching player ID from backend for $playerName")
                true // Complete anyway, don't crash the app
            }
    }
}
