package example.com.totalnba.data.network

import example.com.totalnba.data.network.model.*
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TotalNbaApi {

    @GET("api/prediction/all-prediction/")
    fun getPredictedMatches(): Single<List<PredictedMatchDto>>

    @GET("api/prediction/week/{week}/")
    fun getPredictedMatchesByWeek(@Path("week") weekName: String): Single<List<PredictedMatchDto>>

    @GET("api/prediction/day/{day}/")
    fun getPredictedMatchesByDay(@Path("day") dayName: String): Single<List<PredictedMatchDto>>

    @GET("api/api/results/all-overalls/")
    fun getAverageOveralls(): Single<List<OverallDto>>

    @GET("api/overalls")
    fun getOverallByTeams(
        @Query("homeName") homeName: String,
        @Query("awayName") awayName: String
    ): Single<List<OverallDto>>

    @GET("api/results/homeTeam")
    fun getHomeResultsByTeamName(@Query("homeTeam") homeName: String): Single<List<ResultDto>>

    @GET("api/results/awayTeam")
    fun getAwayResultsByTeamName(@Query("awayTeam") awayName: String): Single<List<ResultDto>>

    @GET("api/results/all-results-by-team")
    fun getResultsByTeamName(@Query("teamName") teamName: String): Single<List<ResultDto>>

    @GET("api/v2/player-stat/all-player-stat/")
    fun getPlayerStats(): Single<List<PlayerStatDto>>

    @GET("api/v2/player-stat/name/")
    fun getPlayerStatsByName(@Query("name") name: String): Maybe<PlayerStatDto>

    @GET("api/v2/player-stat/team/")
    fun getPlayerStatsByTeam(@Query("team") teamName: String): Single<List<PlayerStatDto>>

    @GET("api/v2/player-stat/aggregatedByPlayerName/")
    fun getAggregatedStatsByName(@Query("player") player: String): Maybe<AggregatedStatDto>

    @GET("api/v2/player-stat/all-aggregated-stats/")
    fun getAggregatedStats(): Single<List<AggregatedStatDto>>

    @GET("api/adjustments")
    fun getAdjustmentByTeamName(@Query("teamName") teamName: String): Single<AdjustmentDto>

}