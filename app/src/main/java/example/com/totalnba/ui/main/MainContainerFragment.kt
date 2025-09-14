package example.com.totalnba.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.R
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.ui.components.BottomFloatingNavigation
import example.com.totalnba.ui.components.NavigationItem
import example.com.totalnba.ui.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainContainerFragment : Fragment() {

    @Inject
    lateinit var navigator: AppNavigator

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    MainContainer(
                        onNavigationItemSelected = { item ->
                            handleNavigation(item)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the NavHostFragment within this fragment
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_container) as? NavHostFragment
        navController = navHostFragment?.navController ?: throw IllegalStateException("NavController not found")
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
                    navController.navigate(R.id.PlayerSearchFragment)
                }
            }
            NavigationItem.STANDINGS -> {
                if (navController.currentDestination?.id != R.id.StandingsFragment) {
                    navController.navigate(R.id.StandingsFragment)
                }
            }
            NavigationItem.PROFILE -> {
                if (navController.currentDestination?.id != R.id.SettingsFragment) {
                    navController.navigate(R.id.SettingsFragment)
                }
            }
        }
    }
}

@Composable
fun MainContainer(
    onNavigationItemSelected: (NavigationItem) -> Unit
) {
    var selectedNavItem by remember { mutableStateOf(NavigationItem.PREDICTIONS) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content area with bottom padding for floating nav
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(bottom = 90.dp)
        ) {
            // NavHostFragment content will be inserted here by the fragment manager
            // This space is filled by the actual fragment content
        }

        // Persistent Bottom Floating Navigation
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