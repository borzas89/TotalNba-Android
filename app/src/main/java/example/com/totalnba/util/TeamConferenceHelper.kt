package example.com.totalnba.util

/**
 * Helper for NBA team conference and division information
 */
enum class Conference {
    EASTERN,
    WESTERN
}

object TeamConferenceHelper {

    private val easternTeams = setOf(
        "ATL",  // Atlanta Hawks
        "BOS",  // Boston Celtics
        "BKN",  // Brooklyn Nets
        "CHA",  // Charlotte Hornets
        "CHI",  // Chicago Bulls
        "CLE",  // Cleveland Cavaliers
        "DET",  // Detroit Pistons
        "IND",  // Indiana Pacers
        "MIA",  // Miami Heat
        "MIL",  // Milwaukee Bucks
        "NYK",  // New York Knicks
        "ORL",  // Orlando Magic
        "PHI",  // Philadelphia 76ers
        "TOR",  // Toronto Raptors
        "WAS"   // Washington Wizards
    )

    private val westernTeams = setOf(
        "DAL",  // Dallas Mavericks
        "DEN",  // Denver Nuggets
        "GSW",  // Golden State Warriors
        "HOU",  // Houston Rockets
        "LAC",  // LA Clippers
        "LAL",  // Los Angeles Lakers
        "MEM",  // Memphis Grizzlies
        "MIN",  // Minnesota Timberwolves
        "NOP",  // New Orleans Pelicans
        "OKC",  // Oklahoma City Thunder
        "PHX",  // Phoenix Suns
        "POR",  // Portland Trail Blazers
        "SAC",  // Sacramento Kings
        "SAS",  // San Antonio Spurs
        "UTA"   // Utah Jazz
    )

    /**
     * Get conference for a team by abbreviation
     */
    fun getConference(teamAbbr: String): Conference {
        return when {
            easternTeams.contains(teamAbbr.uppercase()) -> Conference.EASTERN
            westernTeams.contains(teamAbbr.uppercase()) -> Conference.WESTERN
            else -> {
                // Default to Western if unknown
                Conference.WESTERN
            }
        }
    }

    /**
     * Check if team is in Eastern Conference
     */
    fun isEasternConference(teamAbbr: String): Boolean {
        return easternTeams.contains(teamAbbr.uppercase())
    }

    /**
     * Check if team is in Western Conference
     */
    fun isWesternConference(teamAbbr: String): Boolean {
        return westernTeams.contains(teamAbbr.uppercase())
    }

    /**
     * Get all Eastern Conference team abbreviations
     */
    fun getEasternTeams(): Set<String> = easternTeams

    /**
     * Get all Western Conference team abbreviations
     */
    fun getWesternTeams(): Set<String> = westernTeams
}
