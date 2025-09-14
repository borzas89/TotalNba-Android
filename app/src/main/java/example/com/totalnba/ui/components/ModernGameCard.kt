package example.com.totalnba.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import example.com.totalnba.R
import example.com.totalnba.data.model.PredictedMatch
import example.com.totalnba.util.imageResolverId
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ModernGameCard(
    match: PredictedMatch,
    onGameClick: () -> Unit,
    onTeamClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onGameClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Game time and venue info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatGameTime(match.matchDate),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Game Center",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Teams and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Away team
                TeamSection(
                    teamName = match.awayTeam ?: "",
                    teamAlias = match.awayAlias ?: "",
                    score = match.awayScore?.toInt()?.toString() ?: "-",
                    isWinner = (match.awayScore ?: 0.0) > (match.homeScore ?: 0.0),
                    onTeamClick = {
                        onTeamClick(match.awayTeam ?: "", match.homeTeam ?: "")
                    },
                    modifier = Modifier.weight(1f)
                )

                // VS and total
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "VS",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${match.total?.toInt() ?: "-"}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "TOTAL",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }

                // Home team
                TeamSection(
                    teamName = match.homeTeam ?: "",
                    teamAlias = match.homeAlias ?: "",
                    score = match.homeScore?.toInt()?.toString() ?: "-",
                    isWinner = (match.homeScore ?: 0.0) > (match.awayScore ?: 0.0),
                    onTeamClick = {
                        onTeamClick(match.homeTeam ?: "", match.awayTeam ?: "")
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Game status or spread
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                match.spread?.let { spread ->
                    Text(
                        text = "Spread: ${if (spread > 0) "+" else ""}${spread.toInt()}",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamSection(
    teamName: String,
    teamAlias: String,
    score: String,
    isWinner: Boolean,
    onTeamClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { onTeamClick() }
    ) {
        // Team logo
        val logoRes = try {
            imageResolverId(teamName)
        } catch (e: Exception) {
            R.drawable.splash_icon
        }

        Image(
            painter = painterResource(id = logoRes),
            contentDescription = teamName,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Team alias
        Text(
            text = teamAlias.ifEmpty { teamName.take(3).uppercase() },
            style = MaterialTheme.typography.body2,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        // Score
        Text(
            text = score,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = if (isWinner) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

private fun formatGameTime(date: Date?): String {
    return if (date != null) {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.format(date)
    } else {
        "TBD"
    }
}

@Preview
@Composable
fun ModernGameCardPreview() {
    MaterialTheme {
        ModernGameCard(
            match = PredictedMatch(
                commonMatchId = "1",
                matchTitle = "Lakers vs Warriors",
                homeTeam = "Los Angeles Lakers",
                awayTeam = "Golden State Warriors",
                homeAlias = "LAL",
                awayAlias = "GSW",
                homeScore = 110.0,
                awayScore = 104.0,
                total = 214.0,
                spread = 6.0,
                matchDate = Date()
            ),
            onGameClick = { },
            onTeamClick = { _, _ -> }
        )
    }
}