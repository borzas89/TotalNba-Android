package example.com.totalnba.data.remote

import example.com.totalnba.data.model.PlayerStat
import io.reactivex.*

interface PlayerStatService {
    fun getPlayerStats() : Single<List<PlayerStat>>
    fun observePlayerStats() : Observable<List<PlayerStat>>
    fun getPlayerStatsByName(name: String) : Maybe<PlayerStat>
    fun updatePlayerStats(): Completable
    fun searchPlayers(query: String): Flowable<List<PlayerStat>>
}