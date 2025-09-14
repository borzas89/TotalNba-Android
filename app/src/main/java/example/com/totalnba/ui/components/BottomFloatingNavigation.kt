package example.com.totalnba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class NavigationItem(
    val icon: ImageVector,
    val label: String
) {
    PREDICTIONS(Icons.Filled.Home, "Home"),
    PLAYER_SEARCH(Icons.Filled.Search, "Search"),
    STANDINGS(Icons.Filled.List, "Standings"),
    PROFILE(Icons.Filled.Settings, "Settings")
}

@Composable
fun BottomFloatingNavigation(
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem.values().forEach { item ->
                FloatingNavItem(
                    item = item,
                    isSelected = item == selectedItem,
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        Color.Transparent
    }

    val iconTint = if (isSelected) {
        MaterialTheme.colors.onPrimary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun BottomFloatingNavigationPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            BottomFloatingNavigation(
                selectedItem = NavigationItem.PREDICTIONS,
                onItemSelected = { }
            )
        }
    }
}

@Preview
@Composable
fun BottomFloatingNavigationDarkPreview() {
    MaterialTheme(colors = androidx.compose.material.darkColors()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            BottomFloatingNavigation(
                selectedItem = NavigationItem.STANDINGS,
                onItemSelected = { }
            )
        }
    }
}