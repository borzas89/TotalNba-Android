package example.com.totalnba.data.remote

import example.com.totalnba.data.base.map
import example.com.totalnba.data.database.dao.PlayerStatDao
import example.com.totalnba.data.model.PlayerStat
import example.com.totalnba.data.network.TotalNbaApi
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlayerStatServiceImpl @Inject constructor(
    private var totalNbaApi: TotalNbaApi,
    private var playerStatDao: PlayerStatDao
) : PlayerStatService {

    override fun getPlayerStats(): Single<List<PlayerStat>> =
        totalNbaApi.getPlayerStats()
            .map {
                it.map()
            }.subscribeOn(Schedulers.io())

    override fun observePlayerStats(): Observable<List<PlayerStat>> =
        playerStatDao.observeAll().subscribeOn(Schedulers.io())

    override fun getPlayerStatsByName(name: String): Maybe<PlayerStat> =
        playerStatDao.getByName(name)
            .subscribeOn(Schedulers.io())

    override fun updatePlayerStats(): Completable =
        totalNbaApi.getPlayerStats()
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { items ->
                playerStatDao.save(items.map())
            }

    override fun searchPlayers(query: String): Flowable<List<PlayerStat>> {
        if (query.isBlank() || query.length < 3) {
            return Flowable.empty()
        }

        return playerStatDao.getByNameOrTeam(query)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }

}