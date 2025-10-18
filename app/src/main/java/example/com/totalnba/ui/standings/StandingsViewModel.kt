package example.com.totalnba.ui.standings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.data.network.TotalNbaApi
import example.com.totalnba.util.Conference
import example.com.totalnba.util.TeamConferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

data class StandingTeam(
    val rank: Int,
    val teamName: String,
    val teamAbbr: String,
    val wins: Int,
    val losses: Int,
    val winPercentage: Double,
    val gamesBack: String
)

@HiltViewModel
class StandingsViewModel @Inject constructor(
    private val totalNbaApi: TotalNbaApi
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _easternStandings = MutableLiveData<List<StandingTeam>>()
    val easternStandings: LiveData<List<StandingTeam>> = _easternStandings

    private val _westernStandings = MutableLiveData<List<StandingTeam>>()
    val westernStandings: LiveData<List<StandingTeam>> = _westernStandings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchStandings()
    }

    fun fetchStandings() {
        _isLoading.value = true
        _errorMessage.value = null

        totalNbaApi.getAllAdjustments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { adjustmentDtos ->
                    Log.d("StandingsViewModel", "Fetched ${adjustmentDtos.size} teams")

                    // Map to domain model
                    val adjustments = adjustmentDtos.map { it.map() }

                    // Separate by conference
                    val easternTeams = adjustments.filter {
                        TeamConferenceHelper.isEasternConference(it.teamAbbreviation)
                    }
                    val westernTeams = adjustments.filter {
                        TeamConferenceHelper.isWesternConference(it.teamAbbreviation)
                    }

                    Log.d("StandingsViewModel", "Eastern: ${easternTeams.size}, Western: ${westernTeams.size}")

                    // Process and sort standings
                    _easternStandings.value = processStandings(easternTeams)
                    _westernStandings.value = processStandings(westernTeams)

                    _isLoading.value = false
                },
                { error ->
                    Log.e("StandingsViewModel", "Error fetching standings", error)
                    _errorMessage.value = "Failed to load standings: ${error.message}"
                    _isLoading.value = false
                }
            ).let { compositeDisposable.add(it) }
    }

    private fun processStandings(teams: List<Adjustment>): List<StandingTeam> {
        // Sort by wins (descending), then by win percentage
        val sortedTeams = teams.sortedWith(
            compareByDescending<Adjustment> { it.wins }
                .thenByDescending { it.totalPct }
        )

        // Get first place team for games back calculation
        val firstPlaceWins = sortedTeams.firstOrNull()?.wins ?: 0
        val firstPlaceLosses = sortedTeams.firstOrNull()?.losses ?: 0

        return sortedTeams.mapIndexed { index, adjustment ->
            val gamesBack = if (index == 0) {
                "-"
            } else {
                calculateGamesBack(
                    leaderWins = firstPlaceWins,
                    leaderLosses = firstPlaceLosses,
                    teamWins = adjustment.wins,
                    teamLosses = adjustment.losses
                )
            }

            StandingTeam(
                rank = index + 1,
                teamName = adjustment.team,
                teamAbbr = adjustment.teamAbbreviation,
                wins = adjustment.wins,
                losses = adjustment.losses,
                winPercentage = adjustment.totalPct,
                gamesBack = gamesBack
            )
        }
    }

    private fun calculateGamesBack(
        leaderWins: Int,
        leaderLosses: Int,
        teamWins: Int,
        teamLosses: Int
    ): String {
        val gb = ((leaderWins - teamWins) + (teamLosses - leaderLosses)) / 2.0
        return if (gb == 0.0) {
            "-"
        } else {
            String.format("%.1f", gb)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
