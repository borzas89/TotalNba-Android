package example.com.totalnba.data.remote

import example.com.totalnba.data.base.map
import example.com.totalnba.data.database.dao.ResultDao
import example.com.totalnba.data.model.Result
import example.com.totalnba.data.network.TotalNbaApi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ResultServiceImpl @Inject constructor(
    private var totalNbaApi: TotalNbaApi,
    private var resultsDao: ResultDao
) : ResultService {
    override fun observeResultsByTeam(teamName: String): Observable<List<Result>> =
        resultsDao.observeByTeamName(teamName).subscribeOn(Schedulers.io())

    override fun updateResultsByTeam(teamName: String): Completable =
        totalNbaApi.getResultsByTeamName(teamName)
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { items ->
                resultsDao.save(items.map())
            }
}