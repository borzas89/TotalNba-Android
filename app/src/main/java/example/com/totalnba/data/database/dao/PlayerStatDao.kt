package example.com.totalnba.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import example.com.totalnba.data.model.PlayerStat
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface PlayerStatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(playerStat: List<PlayerStat>) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(playerStat: PlayerStat) : Completable

    @Query("SELECT * FROM player_stat WHERE id = :id")
    fun getById(id: Int): Maybe<PlayerStat>

    @Query("SELECT * FROM player_stat")
    fun observeAll(): Observable<List<PlayerStat>>

    @Query("DELETE FROM player_stat")
    fun deleteAll(): Completable

    @Query("SELECT * FROM player_stat WHERE fullName LIKE :fullName")
    fun getByName(fullName: String): Maybe<PlayerStat>

    @Query("SELECT * FROM player_stat WHERE fullName LIKE '%' || :query || '%' OR  team LIKE '%' || :query || '%'")
    fun getByNameOrTeam(query: String): Flowable<List<PlayerStat>>
}