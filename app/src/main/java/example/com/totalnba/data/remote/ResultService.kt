package example.com.totalnba.data.remote

import example.com.totalnba.data.model.Result
import io.reactivex.Completable
import io.reactivex.Observable

interface ResultService {
    fun observeResultsByTeam(teamName: String): Observable<List<Result>>
    fun updateResultsByTeam(teamName: String): Completable
}