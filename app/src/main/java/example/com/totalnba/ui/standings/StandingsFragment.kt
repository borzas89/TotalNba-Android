package example.com.totalnba.ui.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.R
import example.com.totalnba.ui.theme.AppTheme

data class StandingItem(
    val rank: Int,
    val teamName: String,
    val teamLogo: Int,
    val wins: Int,
    val losses: Int,
    val winPercentage: Float,
    val gamesBack: String
)

@AndroidEntryPoint
class StandingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    StandingsScreen()
                }
            }
        }
    }
}

@Composable
fun StandingsScreen() {
    val easternStandings = getDummyEasternStandings()
    val westernStandings = getDummyWesternStandings()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NBA STANDINGS",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "2024-25 Season",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        // Conferences Tabs
        var selectedConference by remember { mutableStateOf("Eastern") }

        TabRow(
            selectedTabIndex = if (selectedConference == "Eastern") 0 else 1,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Tab(
                selected = selectedConference == "Eastern",
                onClick = { selectedConference = "Eastern" },
                text = {
                    Text(
                        "Eastern Conference",
                        fontWeight = if (selectedConference == "Eastern") FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedConference == "Western",
                onClick = { selectedConference = "Western" },
                text = {
                    Text(
                        "Western Conference",
                        fontWeight = if (selectedConference == "Western") FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        // Standings List
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header row
            item {
                StandingsHeaderRow()
            }

            // Data rows
            val standings = if (selectedConference == "Eastern") easternStandings else westernStandings
            itemsIndexed(standings) { index, team ->
                StandingsRow(
                    standing = team,
                    isPlayoffPosition = index < 8
                )
            }
        }
    }
}

@Composable
private fun StandingsHeaderRow() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#",
                modifier = Modifier.width(30.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "TEAM",
                modifier = Modifier.weight(2f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "W",
                modifier = Modifier.width(40.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "L",
                modifier = Modifier.width(40.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "PCT",
                modifier = Modifier.width(60.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "GB",
                modifier = Modifier.width(50.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun StandingsRow(
    standing: StandingItem,
    isPlayoffPosition: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = if (isPlayoffPosition) {
            MaterialTheme.colors.primary.copy(alpha = 0.05f)
        } else {
            MaterialTheme.colors.surface
        },
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Text(
                text = standing.rank.toString(),
                modifier = Modifier.width(30.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPlayoffPosition) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )

            // Team logo and name
            Row(
                modifier = Modifier.weight(2f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = standing.teamLogo),
                    contentDescription = standing.teamName,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = standing.teamName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
            }

            // Wins
            Text(
                text = standing.wins.toString(),
                modifier = Modifier.width(40.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface
            )

            // Losses
            Text(
                text = standing.losses.toString(),
                modifier = Modifier.width(40.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface
            )

            // Win Percentage
            Text(
                text = String.format("%.3f", standing.winPercentage),
                modifier = Modifier.width(60.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface
            )

            // Games Back
            Text(
                text = standing.gamesBack,
                modifier = Modifier.width(50.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private fun getDummyEasternStandings(): List<StandingItem> {
    return listOf(
        StandingItem(1, "Boston Celtics", R.drawable.bos, 52, 30, 0.634f, "-"),
        StandingItem(2, "Philadelphia 76ers", R.drawable.phi, 50, 32, 0.610f, "2.0"),
        StandingItem(3, "Milwaukee Bucks", R.drawable.mil, 49, 33, 0.598f, "3.0"),
        StandingItem(4, "Cleveland Cavaliers", R.drawable.cle, 48, 34, 0.585f, "4.0"),
        StandingItem(5, "New York Knicks", R.drawable.nyk, 47, 35, 0.573f, "5.0"),
        StandingItem(6, "Miami Heat", R.drawable.mia, 46, 36, 0.561f, "6.0"),
        StandingItem(7, "Indiana Pacers", R.drawable.ind, 45, 37, 0.549f, "7.0"),
        StandingItem(8, "Atlanta Hawks", R.drawable.atl, 44, 38, 0.537f, "8.0"),
        StandingItem(9, "Toronto Raptors", R.drawable.tor, 43, 39, 0.524f, "9.0"),
        StandingItem(10, "Chicago Bulls", R.drawable.chi, 42, 40, 0.512f, "10.0"),
        StandingItem(11, "Brooklyn Nets", R.drawable.bkn, 41, 41, 0.500f, "11.0"),
        StandingItem(12, "Orlando Magic", R.drawable.orl, 40, 42, 0.488f, "12.0"),
        StandingItem(13, "Charlotte Hornets", R.drawable.cha, 39, 43, 0.476f, "13.0"),
        StandingItem(14, "Washington Wizards", R.drawable.was, 38, 44, 0.463f, "14.0"),
        StandingItem(15, "Detroit Pistons", R.drawable.det, 37, 45, 0.451f, "15.0")
    )
}

private fun getDummyWesternStandings(): List<StandingItem> {
    return listOf(
        StandingItem(1, "Denver Nuggets", R.drawable.den, 54, 28, 0.659f, "-"),
        StandingItem(2, "Golden State Warriors", R.drawable.gsw, 52, 30, 0.634f, "2.0"),
        StandingItem(3, "Phoenix Suns", R.drawable.phx, 51, 31, 0.622f, "3.0"),
        StandingItem(4, "Sacramento Kings", R.drawable.sac, 50, 32, 0.610f, "4.0"),
        StandingItem(5, "LA Clippers", R.drawable.lac, 49, 33, 0.598f, "5.0"),
        StandingItem(6, "Los Angeles Lakers", R.drawable.lal, 48, 34, 0.585f, "6.0"),
        StandingItem(7, "Memphis Grizzlies", R.drawable.mem, 47, 35, 0.573f, "7.0"),
        StandingItem(8, "Dallas Mavericks", R.drawable.dal, 46, 36, 0.561f, "8.0"),
        StandingItem(9, "New Orleans Pelicans", R.drawable.nop, 45, 37, 0.549f, "9.0"),
        StandingItem(10, "Minnesota Timberwolves", R.drawable.min, 44, 38, 0.537f, "10.0"),
        StandingItem(11, "Oklahoma City Thunder", R.drawable.okc, 43, 39, 0.524f, "11.0"),
        StandingItem(12, "Utah Jazz", R.drawable.uta, 42, 40, 0.512f, "12.0"),
        StandingItem(13, "Portland Trail Blazers", R.drawable.por, 41, 41, 0.500f, "13.0"),
        StandingItem(14, "San Antonio Spurs", R.drawable.sas, 40, 42, 0.488f, "14.0"),
        StandingItem(15, "Houston Rockets", R.drawable.hou, 39, 43, 0.476f, "15.0")
    )
}

@Preview
@Composable
fun StandingsScreenPreview() {
    AppTheme {
        StandingsScreen()
    }
}