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
import example.com.totalnba.ui.detail.DetailBottomSheetDialogFragment
import example.com.totalnba.ui.theme.AppTheme
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ModernPredictionListFragment : Fragment() {

    private val viewModel: PredictionListViewModel by viewModels()

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
                        onGameClick = { prediction ->
                            val id = prediction.commonMatchId
                            val awayTeam = prediction.awayTeam ?: ""
                            val homeTeam = prediction.homeTeam ?: ""
                            openDetailDialog(id, homeTeam, awayTeam)
                        },
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

    private fun openDetailDialog(id: String, homeName: String, awayName: String) {
        DetailBottomSheetDialogFragment.newInstance(id, homeName, awayName)
            .show(requireFragmentManager(), "DetailBottomSheetDialog")
    }
}

@Composable
fun ModernPredictionScreen(
    viewModel: PredictionListViewModel,
    onGameClick: (PredictedMatch) -> Unit,
    onTeamClick: (String, String) -> Unit,
    onPlayerSearchClick: () -> Unit,
    onStandingsClick: () -> Unit
) {
    val predictions by viewModel.predictionList.observeAsState(emptyList())
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Get error title from ObservableField
    val errorTitle = viewModel.errorTitle.get() ?: ""

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
                                    onGameClick = { onGameClick(prediction) },
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