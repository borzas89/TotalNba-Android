package example.com.totalnba.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import example.com.totalnba.R
import example.com.totalnba.data.model.PlayerStat
import example.com.totalnba.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerDetailBottomSheet(
    player: PlayerStat,
    onDismiss: () -> Unit
) {
    // Create a mutable state that will be updated when player changes
    var currentPlayer by remember { mutableStateOf(player) }

    // Update the current player whenever the prop changes
    LaunchedEffect(player) {
        currentPlayer = player
    }

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
            PlayerDetailContent(
                player = currentPlayer,
                onClose = onDismiss
            )
        },
        modifier = Modifier.fillMaxSize(),
        content = {}
    )
}

@Composable
fun PlayerDetailContent(
    player: PlayerStat,
    onClose: () -> Unit
) {
    // Debug logging
    LaunchedEffect(player.playerPicsId) {
        android.util.Log.d("PlayerDetailBottomSheet", "Player: ${player.fullName}, Pics ID: ${player.playerPicsId}, Image URL: ${player.imageUrl}")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .padding(bottom = 100.dp) // Extra padding to avoid floating menu overlap
    ) {
        // Header with close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Player Header Section
        PlayerHeaderSection(player = player)

        Spacer(modifier = Modifier.height(24.dp))

        // Player Stats Section
        PlayerStatsSection(player = player)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PlayerHeaderSection(player: PlayerStat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = colorResource(id = player.jerseyTint),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Player Image
            val playerPicsId = player.playerPicsId
            if (!playerPicsId.isNullOrEmpty() && playerPicsId != "no_image") {
                val context = LocalContext.current
                // Use key to force recomposition when playerPicsId changes
                key(playerPicsId) {
                    AndroidView(
                        factory = { ctx ->
                            android.widget.ImageView(ctx).apply {
                                clipToOutline = true
                                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                            }
                        },
                        update = { imageView ->
                            Glide.with(context)
                                .load(player.imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .circleCrop()
                                .placeholder(R.drawable.jersey)
                                .error(R.drawable.jersey)
                                .into(imageView)
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    )
                }
            } else {
                // Fallback jersey icon if no image
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.jersey),
                        contentDescription = "Jersey",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Player Name
            Text(
                text = player.fullName,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Team Name
            Text(
                text = player.team,
                style = MaterialTheme.typography.h6,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PlayerStatsSection(player: PlayerStat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Basic Info Stats
            PlayerStatItem(label = "Position", value = player.position)
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PlayerStatItem(label = "Games Played", value = player.gamePlayed.toString())
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PlayerStatItem(
                label = "Points Per Game",
                value = String.format("%.1f", player.pointsPerGame),
                highlighted = true
            )
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PlayerStatItem(
                label = "Rebounds Per Game",
                value = String.format("%.1f", player.reboundsPerGame)
            )
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PlayerStatItem(
                label = "Assists Per Game",
                value = String.format("%.1f", player.assistsPerGame)
            )
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PlayerStatItem(
                label = "Steals Per Game",
                value = String.format("%.1f", player.stealsPerGame)
            )

            // Advanced Stats - calculate from existing data if not provided
            val pra = player.pointsReboundsAssists?.takeIf { it != 0.0 }
                ?: (player.pointsPerGame + player.reboundsPerGame + player.assistsPerGame)

            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PlayerStatItem(
                label = "PRA Per Game",
                value = String.format("%.1f", pra)
            )

            val pa = player.pointsAssists?.takeIf { it != 0.0 }
                ?: (player.pointsPerGame + player.assistsPerGame)

            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PlayerStatItem(
                label = "Points + Assists Per Game",
                value = String.format("%.1f", pa)
            )

            val twoP = player.twoPointsMade
            if (twoP != null && twoP != 0.0) {
                Divider(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                PlayerStatItem(
                    label = "2-Point FG Made",
                    value = String.format("%.1f", twoP)
                )
            }

            val threeP = player.threePointsMade
            if (threeP != null && threeP != 0.0) {
                Divider(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                PlayerStatItem(
                    label = "3-Point FG Made",
                    value = String.format("%.1f", threeP)
                )
            }
        }
    }
}

@Composable
private fun PlayerStatItem(
    label: String,
    value: String,
    highlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (highlighted) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
            textAlign = TextAlign.End
        )
    }
}

@Preview
@Composable
fun PlayerDetailContentPreview() {
    val samplePlayer = PlayerStat(
        id = 1,
        fullName = "LeBron James",
        team = "LAL",
        position = "SF",
        gamePlayed = 56,
        pointsPerGame = 25.7,
        reboundsPerGame = 7.3,
        assistsPerGame = 7.3,
        stealsPerGame = 1.3,
        playerPicsId = "2544",
        pointsReboundsAssists = 40.3,
        twoPointsMade = 8.2,
        threePointsMade = 2.1,
        pointsAssists = 33.0
    )
    AppTheme {
        Surface {
            PlayerDetailContent(
                player = samplePlayer,
                onClose = {}
            )
        }
    }
}
