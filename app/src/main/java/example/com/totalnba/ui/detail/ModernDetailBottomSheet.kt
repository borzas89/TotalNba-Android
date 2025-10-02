package example.com.totalnba.ui.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import example.com.totalnba.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModernDetailBottomSheet(
    matchTitle: String,
    homeWinPct: Double,
    awayWinPct: Double,
    homeTeamStats: TeamStats,
    awayTeamStats: TeamStats,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded
    )

    LaunchedEffect(bottomSheetState.targetValue) {
        if (bottomSheetState.targetValue == ModalBottomSheetValue.Hidden) {
            onDismiss()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            DetailBottomSheetContent(
                matchTitle = matchTitle,
                homeWinPct = homeWinPct,
                awayWinPct = awayWinPct,
                homeTeamStats = homeTeamStats,
                awayTeamStats = awayTeamStats,
                onClose = onDismiss
            )
        },
        modifier = Modifier.fillMaxSize(),
        content = {}
    )
}

@Composable
fun DetailBottomSheetContent(
    matchTitle: String,
    homeWinPct: Double,
    awayWinPct: Double,
    homeTeamStats: TeamStats,
    awayTeamStats: TeamStats,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 100.dp) // Extra padding to avoid floating menu overlap
    ) {
        // Header with close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Match Analysis",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Match title
        Text(
            text = matchTitle,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Win probability section
        WinProbabilitySection(
            homeWinPct = homeWinPct,
            awayWinPct = awayWinPct
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Team statistics comparison
        TeamStatsComparison(
            homeTeamStats = homeTeamStats,
            awayTeamStats = awayTeamStats
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun WinProbabilitySection(
    homeWinPct: Double,
    awayWinPct: Double
) {
    Column {
        Text(
            text = "Win Probability",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Win percentage numbers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${awayWinPct.toInt()}%",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = ModernColors.WinRed
            )
            Text(
                text = "${homeWinPct.toInt()}%",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = ModernColors.WinGreen
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Modern progress bar
        ModernWinProgressBar(
            homeWinPct = homeWinPct,
            awayWinPct = awayWinPct
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ModernWinProgressBar(
    homeWinPct: Double,
    awayWinPct: Double,
    modifier: Modifier = Modifier
) {
    val animatedHomeProgress by animateFloatAsState(
        targetValue = (homeWinPct / 100.0).toFloat(),
        animationSpec = tween(durationMillis = 1200),
        label = "home_progress"
    )

    // Create gradient brushes for more visual appeal
    val redGradient = Brush.horizontalGradient(
        colors = listOf(ModernColors.WinRed, ModernColors.WinRed.copy(alpha = 0.8f))
    )
    val greenGradient = Brush.horizontalGradient(
        colors = listOf(ModernColors.WinGreen.copy(alpha = 0.8f), ModernColors.WinGreen)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(redGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedHomeProgress)
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(greenGradient)
                .align(Alignment.CenterEnd)
        )

        // Add a subtle inner highlight
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedHomeProgress)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(Color.White.copy(alpha = 0.3f))
                .align(Alignment.TopEnd)
                .padding(top = 2.dp)
        )
    }
}

@Composable
private fun TeamStatsComparison(
    homeTeamStats: TeamStats,
    awayTeamStats: TeamStats
) {
    Column {
        Text(
            text = "Team Statistics",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats card container
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                StatRow("Points Per Game", awayTeamStats.pointsPerGame, homeTeamStats.pointsPerGame)
                StatRow("Points Allowed", awayTeamStats.pointsAllowed, homeTeamStats.pointsAllowed, isReversed = true)
                StatRow("Overall Rating", awayTeamStats.overall, homeTeamStats.overall)
                StatRow("Home/Away Rating", awayTeamStats.homeAwayRating, homeTeamStats.homeAwayRating)
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    awayValue: Double,
    homeValue: Double,
    isReversed: Boolean = false
) {
    val betterAway = if (isReversed) awayValue < homeValue else awayValue > homeValue
    val betterHome = !betterAway

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = String.format("%.1f", awayValue),
            style = MaterialTheme.typography.body1,
            fontWeight = if (betterAway) FontWeight.Bold else FontWeight.Normal,
            color = if (betterAway) ModernColors.StatBetter else MaterialTheme.colors.onSurface,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Start
        )

        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.Center
        )

        Text(
            text = String.format("%.1f", homeValue),
            style = MaterialTheme.typography.body1,
            fontWeight = if (betterHome) FontWeight.Bold else FontWeight.Normal,
            color = if (betterHome) ModernColors.StatBetter else MaterialTheme.colors.onSurface,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
}

// Data class for team statistics
data class TeamStats(
    val pointsPerGame: Double,
    val pointsAllowed: Double,
    val overall: Double,
    val homeAwayRating: Double
)

// Modern color scheme with better accessibility and visual appeal
object ModernColors {
    val WinGreen = Color(0xFF27AE60) // Rich emerald green
    val WinRed = Color(0xFFE67E22)   // Warm orange-red (more pleasant than pure red)
    val StatBetter = Color(0xFF3498DB) // Modern blue for better stats
    val CardBackground = Color(0xFFF8F9FA) // Light neutral background
    val TextPrimary = Color(0xFF2C3E50) // Dark blue-gray for text
    val TextSecondary = Color(0xFF7F8C8D) // Medium gray for secondary text
}

@Preview
@Composable
fun ModernDetailBottomSheetPreview() {
    AppTheme {
        DetailBottomSheetContent(
            matchTitle = "Los Angeles Lakers @ Golden State Warriors",
            homeWinPct = 67.5,
            awayWinPct = 32.5,
            homeTeamStats = TeamStats(
                pointsPerGame = 118.2,
                pointsAllowed = 112.8,
                overall = 115.5,
                homeAwayRating = 117.3
            ),
            awayTeamStats = TeamStats(
                pointsPerGame = 114.1,
                pointsAllowed = 116.4,
                overall = 112.8,
                homeAwayRating = 111.9
            ),
            onClose = {}
        )
    }
}