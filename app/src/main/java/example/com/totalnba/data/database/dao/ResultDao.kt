package example.com.totalnba.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import example.com.totalnba.data.model.Result

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(result: List<Result>) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(result: Result) : Completable

    @Query("SELECT * FROM result WHERE id = :id")
    fun getById(id: Int): Maybe<Result>

    @Query("SELECT * FROM result")
    fun observeAll(): Observable<List<Result>>

    @Query("DELETE FROM result")
    fun deleteAll(): Completable

    @Query("SELECT * FROM result WHERE awayName LIKE :teamName OR homeName LIKE :teamName")
    fun observeByTeamName(teamName: String): Observable<List<Result>>
}