package example.com.totalnba.navigator

interface AppNavigator {
    fun navigateToPlayerSearch()
    fun navigateToResults(teamName: String, opponentName: String = "")
    fun popBackStack()
}