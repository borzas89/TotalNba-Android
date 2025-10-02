package example.com.totalnba.ui.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.data.model.PredictedMatch
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.ui.components.*
import example.com.totalnba.ui.detail.DetailBottomSheetContent
import example.com.totalnba.ui.detail.DetailBottomSheetViewModel
import example.com.totalnba.ui.detail.TeamStats
import example.com.totalnba.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ModernPredictionListFragment : Fragment() {

    private val viewModel: PredictionListViewModel by viewModels()
    private val detailViewModel: DetailBottomSheetViewModel by viewModels()

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    ModernPredictionScreen(
                        viewModel = viewModel,
                        detailViewModel = detailViewModel,
                        onTeamClick = { teamName, opponentName ->
                            navigator.navigateToResults(teamName, opponentName)
                        },
                        onPlayerSearchClick = {
                            navigator.navigateToPlayerSearch()
                        },
                        onStandingsClick = {
                            navigator.navigateToStandings()
                        }
                    )
                }
            }
        }
    }

    // This method is no longer needed - using Compose modal instead
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModernPredictionScreen(
    viewModel: PredictionListViewModel,
    detailViewModel: DetailBottomSheetViewModel,
    onTeamClick: (String, String) -> Unit,
    onPlayerSearchClick: () -> Unit,
    onStandingsClick: () -> Unit
) {
    val predictions by viewModel.predictionList.observeAsState(emptyList())
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Bottom sheet state for game details
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var selectedGame by remember { mutableStateOf<PredictedMatch?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Get error title from ObservableField
    val errorTitle = viewModel.errorTitle.get() ?: ""

    // Create state that forces recomposition when data changes
    var homeWinPct by remember { mutableStateOf(50.0) }
    var awayWinPct by remember { mutableStateOf(50.0) }
    var homeTeamStats by remember { mutableStateOf(TeamStats(0.0, 0.0, 0.0, 0.0)) }
    var awayTeamStats by remember { mutableStateOf(TeamStats(0.0, 0.0, 0.0, 0.0)) }

    // Observe changes in ViewModel ObservableFields
    LaunchedEffect(selectedGame) {
        if (selectedGame != null) {
            // Poll for updates (ObservableField doesn't trigger Compose recomposition)
            kotlinx.coroutines.delay(100) // Small delay for API call
            while (selectedGame != null) {
                homeWinPct = detailViewModel.homeWinPct.get() ?: 50.0
                awayWinPct = detailViewModel.awayWinPct.get() ?: 50.0

                // Update team stats from Overall data
                detailViewModel.homeOverall.get()?.let { home ->
                    detailViewModel.awayOverall.get()?.let { away ->
                        homeTeamStats = TeamStats(
                            pointsPerGame = home.teamAvg ?: 0.0,
                            pointsAllowed = detailViewModel.homeAdjustment.get()?.allowedPointsPerGame ?: 0.0,
                            overall = home.overall ?: 0.0,
                            homeAwayRating = (home.homeOverall ?: 0.0)
                        )
                        awayTeamStats = TeamStats(
                            pointsPerGame = away.teamAvg ?: 0.0,
                            pointsAllowed = detailViewModel.awayAdjustment.get()?.allowedPointsPerGame ?: 0.0,
                            overall = away.overall ?: 0.0,
                            homeAwayRating = (away.awayOverall ?: 0.0)
                        )
                    }
                }

                kotlinx.coroutines.delay(200) // Poll every 200ms
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            selectedGame?.let { game ->
                // Use win percentages and stats from DetailBottomSheetViewModel
                DetailBottomSheetContent(
                    matchTitle = "${game.awayTeam} @ ${game.homeTeam}",
                    homeWinPct = homeWinPct,
                    awayWinPct = awayWinPct,
                    homeTeamStats = homeTeamStats,
                    awayTeamStats = awayTeamStats,
                    onClose = {
                        selectedGame = null
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...")
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                // Header
                ModernHeader(
                    onCalendarClick = {
                        val today = LocalDate.now()
                        selectedDate = today
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                        viewModel.filterDay.onNext(formatter.format(today))
                    }
                )

                // Date Selector
                ModernDateSelector(
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                        viewModel.filterDay.onNext(formatter.format(date))
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Content
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    when {
                        predictions.isNotEmpty() -> {
                            LazyColumn(
                                contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(predictions) { prediction ->
                                    ModernGameCard(
                                        match = prediction,
                                        onGameClick = {
                                            selectedGame = prediction
                                            // Setup the DetailViewModel with the selected game
                                            detailViewModel.setupGame(
                                                gameId = prediction.commonMatchId,
                                                homeTeam = prediction.homeTeam ?: "",
                                                awayTeam = prediction.awayTeam ?: ""
                                            )
                                            coroutineScope.launch {
                                                bottomSheetState.show()
                                            }
                                        },
                                        onTeamClick = onTeamClick
                                    )
                                }
                            }
                        }
                        errorTitle.isNotEmpty() -> {
                            EmptyStateCard(message = errorTitle)
                        }
                        else -> {
                            LoadingStateCard()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernHeader(
    onCalendarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Game Predictions",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onCalendarClick) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Go to today",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun LoadingStateCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No events available",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}