package example.com.totalnba.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.R
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.ui.common.AppBar
import example.com.totalnba.ui.common.NavigationIcon
import example.com.totalnba.ui.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

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
                    SettingsScreen(
                        onBackPressed = {
                            navigator.popBackStack()
                        },
                        context = requireContext()
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    context: Context
) {
    var currentThemeMode by remember {
        mutableStateOf(
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES -> ThemeMode.DARK
                AppCompatDelegate.MODE_NIGHT_NO -> ThemeMode.LIGHT
                else -> ThemeMode.SYSTEM
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Header
        AppBar(
            title = "Settings",
            contentColor = MaterialTheme.colors.onPrimary,
            backgroundColor = MaterialTheme.colors.primary,
            navigationIcon = NavigationIcon(
                tint = MaterialTheme.colors.onPrimary,
                description = R.string.content_description_back_nav,
                vector = Icons.Filled.ArrowBack,
                action = onBackPressed
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsSection(title = "Appearance") {
                    ThemeSettingCard(
                        currentTheme = currentThemeMode,
                        onThemeChanged = { newTheme ->
                            currentThemeMode = newTheme
                            when (newTheme) {
                                ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                ThemeMode.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            }
                        }
                    )
                }
            }

            item {
                SettingsSection(title = "About") {
                    InfoCard(
                        title = "TotalNBA",
                        subtitle = "NBA Game Predictions & Stats",
                        icon = Icons.Filled.Settings
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun ThemeSettingCard(
    currentTheme: ThemeMode,
    onThemeChanged: (ThemeMode) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ThemeMode.values().forEach { theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onThemeChanged(theme) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (theme) {
                        ThemeMode.LIGHT -> {
                            Icon(
                                painter = painterResource(id = R.drawable.light_mode_24px),
                                contentDescription = theme.displayName,
                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                        ThemeMode.DARK -> {
                            Icon(
                                painter = painterResource(id = R.drawable.dark_mode_24px),
                                contentDescription = theme.displayName,
                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                        ThemeMode.SYSTEM -> {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = theme.displayName,
                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = theme.displayName,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    RadioButton(
                        selected = currentTheme == theme,
                        onClick = { onThemeChanged(theme) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

enum class ThemeMode(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System Default")
}

@Preview
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(
            onBackPressed = { },
            context = androidx.compose.ui.platform.LocalContext.current
        )
    }
}