package example.com.totalnba.data.remote

import example.com.totalnba.data.model.PlayerStat
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface PlayerStatService {
    fun getPlayerStats() : Single<List<PlayerStat>>
    fun observePlayerStats() : Observable<List<PlayerStat>>
    fun getPlayerStatsByName(name: String) : Maybe<PlayerStat>
    fun updatePlayerStats(): Completable
    fun searchPlayers(query: String): Flowable<List<PlayerStat>>
}