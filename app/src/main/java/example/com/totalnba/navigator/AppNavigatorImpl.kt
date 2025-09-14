package example.com.totalnba.navigator

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import example.com.totalnba.R
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    private val activity: Activity
) : AppNavigator {

    private val navController by lazy {
        Navigation.findNavController(activity, R.id.nav_host_fragment)
    }

    override fun navigateToPlayerSearch() {
       navController.navigate(R.id.PlayerSearchFragment)
    }

    override fun navigateToResults(teamName: String, opponentName: String) {
        val bundle = bundleOf(
            "teamName" to teamName,
            "opponentName" to opponentName
        )
        navController.navigate(R.id.ResultFragment, bundle)
    }

    override fun navigateToStandings() {
        navController.navigate(R.id.StandingsFragment)
    }

    override fun navigateToSettings() {
        navController.navigate(R.id.SettingsFragment)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}