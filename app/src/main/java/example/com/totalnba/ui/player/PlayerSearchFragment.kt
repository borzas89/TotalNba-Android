package example.com.totalnba.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.R
import example.com.totalnba.data.model.PlayerStat
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.ui.common.AppBar
import example.com.totalnba.ui.common.NavigationIcon
import example.com.totalnba.ui.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class PlayerSearchFragment : Fragment() {

    @Inject
    lateinit var navigator: AppNavigator

    private val viewModel: PlayerSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    PlayerSearchScreen(
                        viewModel = viewModel,
                        onBackPressed = {
                            navigator.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerSearchScreen(
    viewModel: PlayerSearchViewModel,
    onBackPressed: () -> Unit
) {
    val playerStats by viewModel.playerStatList.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var selectedPlayerId by remember { mutableStateOf<Int?>(null) }

    // Find the selected player from the current list (will have updated pics_id)
    val selectedPlayer = selectedPlayerId?.let { id ->
        playerStats.find { it.id == id }
    }

    // Update ViewModel search text when query changes
    LaunchedEffect(searchQuery) {
        viewModel.searchText.set(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // App Bar
        AppBar(
            title = "Player Search",
            contentColor = MaterialTheme.colors.onPrimary,
            backgroundColor = MaterialTheme.colors.primary,
            navigationIcon = NavigationIcon(
                tint = MaterialTheme.colors.onPrimary,
                description = R.string.content_description_back_nav,
                vector = Icons.Filled.ArrowBack,
                action = onBackPressed
            )
        )

        // Search Bar
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            modifier = Modifier.padding(16.dp)
        )

        // Player List
        if (playerStats.isEmpty() && searchQuery.isNotEmpty()) {
            EmptyStateView()
        } else {
            PlayerList(
                players = playerStats,
                onPlayerClick = { player ->
                    selectedPlayerId = player.id
                }
            )
        }
    }

    // Bottom Sheet for player details
    selectedPlayer?.let { player ->
        PlayerDetailBottomSheet(
            player = player,
            onDismiss = {
                selectedPlayerId = null
            }
        )
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Search players...",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colors.primary
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.primary,
            textColor = MaterialTheme.colors.onSurface
        ),
        singleLine = true
    )
}

@Composable
fun PlayerList(
    players: List<PlayerStat>,
    onPlayerClick: (PlayerStat) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(players) { player ->
            PlayerListItem(
                player = player,
                onClick = { onPlayerClick(player) }
            )
        }
    }
}

@Composable
fun PlayerListItem(
    player: PlayerStat,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Jersey Icon with team color
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = colorResource(id = player.jerseyTint),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.jersey),
                    contentDescription = "Jersey",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Player Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = player.fullName,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = player.team,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }

            // Points
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = String.format("%.1f", player.pointsPerGame),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "PPG",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "No results",
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No players found",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = "Try a different search term",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}

@Preview
@Composable
fun PlayerListItemPreview() {
    val samplePlayer = PlayerStat(
        id = 1,
        fullName = "LeBron James",
        team = "LAL",
        pointsPerGame = 25.7,
        reboundsPerGame = 7.3,
        assistsPerGame = 7.3
    )
    AppTheme {
        PlayerListItem(player = samplePlayer, onClick = {})
    }
}
