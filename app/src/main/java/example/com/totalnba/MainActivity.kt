package example.com.totalnba

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.databinding.ActivityMainBinding
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.ui.components.BottomFloatingNavigation
import example.com.totalnba.ui.components.NavigationItem
import example.com.totalnba.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var navController: NavController

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Add floating navigation overlay
        setupFloatingNavigation(binding)
        askForNotificationPermission()
    }

    private fun setupFloatingNavigation(binding: ActivityMainBinding) {
        val composeView = ComposeView(this)
        composeView.setContent {
            AppTheme {
                FloatingNavigationOverlay(
                    navController = navController,
                    onNavigationItemSelected = { item ->
                        handleNavigation(item)
                    }
                )
            }
        }

        // Add the ComposeView as an overlay
        binding.root.addView(composeView)
    }

    private fun handleNavigation(item: NavigationItem) {
        when (item) {
            NavigationItem.PREDICTIONS -> {
                if (navController.currentDestination?.id != R.id.ListFragment) {
                    navController.navigate(R.id.ListFragment)
                }
            }
            NavigationItem.PLAYER_SEARCH -> {
                if (navController.currentDestination?.id != R.id.PlayerSearchFragment) {
                    navigator.navigateToPlayerSearch()
                }
            }
            NavigationItem.STANDINGS -> {
                if (navController.currentDestination?.id != R.id.StandingsFragment) {
                    navigator.navigateToStandings()
                }
            }
            NavigationItem.PROFILE -> {
                if (navController.currentDestination?.id != R.id.SettingsFragment) {
                    navigator.navigateToSettings()
                }
            }
        }
    }

    private fun askForNotificationPermission() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) {
            if(it){
            // Permission is granted
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )  {
                    CoroutineScope(Dispatchers.Main).launch {
                        requestPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingNavigationOverlay(
    navController: NavController,
    onNavigationItemSelected: (NavigationItem) -> Unit
) {
    var selectedNavItem by remember { mutableStateOf(NavigationItem.PREDICTIONS) }
    val currentDestination by navController.currentBackStackEntryAsState()

    // Determine if floating nav should be visible (hide on detail screens)
    val shouldShowFloatingNav = when (currentDestination?.destination?.id) {
        R.id.ResultFragment -> false // Hide on Result detail screen
        R.id.TotalStatsWebViewFragment -> false // Hide on WebView screen
        R.id.PlayerSearchFragment -> false // Hide on Player Search screen
        // Add other detail screens here if needed
        else -> true
    }

    // Update selected item based on current destination
    LaunchedEffect(currentDestination) {
        selectedNavItem = when (currentDestination?.destination?.id) {
            R.id.ListFragment -> NavigationItem.PREDICTIONS
            R.id.PlayerSearchFragment -> NavigationItem.PLAYER_SEARCH
            R.id.StandingsFragment -> NavigationItem.STANDINGS
            R.id.SettingsFragment -> NavigationItem.PROFILE
            else -> selectedNavItem // Keep current selection for detail screens
        }
    }

    if (shouldShowFloatingNav) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Transparent overlay - just for positioning the navigation
            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomFloatingNavigation(
                    selectedItem = selectedNavItem,
                    onItemSelected = { item ->
                        selectedNavItem = item
                        onNavigationItemSelected(item)
                    }
                )
            }
        }
    }
}