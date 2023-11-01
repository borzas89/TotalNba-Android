package example.com.totalnba.util

import example.com.totalnba.R
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

fun imageResolverId(key: String) : Int {
    val teamsMap: MutableMap<String, Int> = HashMap()
    // NBA teams
    teamsMap["Atlanta Hawks"] = R.drawable.atl
    teamsMap["Boston Celtics"] =   R.drawable.bos
    teamsMap["Brooklyn Nets"] =  R.drawable.bkn
    teamsMap["Charlotte Hornets"] = R.drawable.cha
    teamsMap["Chicago Bulls"] =  R.drawable.chi
    teamsMap["Cleveland Cavaliers"] = R.drawable.cle
    teamsMap["Dallas Mavericks"] = R.drawable.dal
    teamsMap["Denver Nuggets"] = R.drawable.den
    teamsMap["Detroit Pistons"] =  R.drawable.det
    teamsMap["Golden State Warriors"] = R.drawable.gsw
    teamsMap["Houston Rockets"] = R.drawable.hou
    teamsMap["Indiana Pacers"] = R.drawable.ind
    teamsMap["LA Clippers"] = R.drawable.lac
    teamsMap["Los Angeles Lakers"] = R.drawable.lal
    teamsMap["Memphis Grizzlies"] = R.drawable.mem
    teamsMap["Miami Heat"] = R.drawable.mia
    teamsMap["Milwaukee Bucks"] =  R.drawable.mil
    teamsMap["Minnesota Timberwolves"] = R.drawable.min
    teamsMap["New Orleans Pelicans" ] = R.drawable.nop
    teamsMap["New York Knicks" ] = R.drawable.nyk
    teamsMap["Oklahoma City Thunder" ] =  R.drawable.okc
    teamsMap["Orlando Magic"] =  R.drawable.orl
    teamsMap["Philadelphia 76ers"] = R.drawable.phi
    teamsMap["Phoenix Suns"] = R.drawable.phx
    teamsMap["Portland Trail Blazers"] =  R.drawable.por
    teamsMap["Sacramento Kings"] =  R.drawable.sac
    teamsMap["San Antonio Spurs" ] = R.drawable.sas
    teamsMap["Toronto Raptors"] = R.drawable.tor
    teamsMap["Utah Jazz" ] = R.drawable.uta
    teamsMap["Washington Wizards"] =  R.drawable.was
    // WNBA teams
    teamsMap["Atlanta Dream"] =  R.drawable.atlw
    teamsMap["Chicago Sky"] =  R.drawable.chiw
    teamsMap["Connecticut Sun"] =  R.drawable.conw
    teamsMap["Dallas Wings"] =  R.drawable.dalw2
    teamsMap["Indiana Fever"] =  R.drawable.indw2
    teamsMap["Las Vegas Aces"] =  R.drawable.lvaw2
    teamsMap["Los Angeles Sparks"] =  R.drawable.lasw
    teamsMap["Minnesota Lynx"] =  R.drawable.minw2
    teamsMap["New York Liberty"] =  R.drawable.nyl2
    teamsMap["Phoenix Mercury"] =  R.drawable.phow
    teamsMap["Seattle Storm"] =  R.drawable.seaw
    teamsMap["Washington Mystics"] =  R.drawable.wasw2

    return teamsMap[key] ?: R.drawable.splash_icon
}

fun backgroundResolverId(key: String) : Int {
    val colorsMap: MutableMap<String, Int> = HashMap()
    colorsMap["Atlanta Hawks"] = R.color.team_red
    colorsMap["Boston Celtics"] = R.color.team_green
    colorsMap["Brooklyn Nets"] =  R.color.black
    colorsMap["Charlotte Hornets"] = R.color.team_blue_dark
    colorsMap["Chicago Bulls"] =  R.color.team_red_dark
    colorsMap["Cleveland Cavaliers"] = R.color.team_wine
    colorsMap["Dallas Mavericks"] = R.color.team_blue_light
    colorsMap["Denver Nuggets"] = R.color.team_blue_dark
    colorsMap["Detroit Pistons"] =  R.color.team_red_light
    colorsMap["Golden State Warriors"] = R.color.team_blue_light
    colorsMap["Houston Rockets"] = R.color.team_red_dark
    colorsMap["Indiana Pacers"] = R.color.team_blue_dark
    colorsMap["LA Clippers"] = R.color.team_red
    colorsMap["Los Angeles Lakers"] = R.color.team_purple
    colorsMap["Memphis Grizzlies"] = R.color.team_blue_grey
    colorsMap["Miami Heat"] = R.color.team_wine_dark
    colorsMap["Milwaukee Bucks"] =  R.color.team_green_dark
    colorsMap["Minnesota Timberwolves"] = R.color.team_blue_dark
    colorsMap["New Orleans Pelicans" ] = R.color.team_blue_dark
    colorsMap["New York Knicks" ] = R.color.team_orange
    colorsMap["Oklahoma City Thunder" ] =  R.color.team_blue
    colorsMap["Orlando Magic"] =  R.color.team_blue
    colorsMap["Philadelphia 76ers"] = R.color.team_blue_light
    colorsMap["Phoenix Suns"] = R.color.team_orange_dark
    colorsMap["Portland Trail Blazers"] =  R.color.team_red
    colorsMap["Sacramento Kings"] =  R.color.team_purple_light
    colorsMap["San Antonio Spurs" ] = R.color.team_black
    colorsMap["Toronto Raptors"] = R.color.team_red_light
    colorsMap["Utah Jazz" ] = R.color.team_blue_dark
    colorsMap["Washington Wizards"] =  R.color.team_blue_dark
    // WNBA
    colorsMap["Atlanta Dream"] =  R.color.team_black
    colorsMap["Chicago Sky"] =  R.color.team_blue_grey
    colorsMap["Connecticut Sun"] =  R.color.team_orange_dark
    colorsMap["Dallas Wings"] =  R.color.team_blue_light
    colorsMap["Indiana Fever"] =  R.color.team_blue_dark
    colorsMap["Las Vegas Aces"] =  R.color.team_red_dark
    colorsMap["Los Angeles Sparks"] =  R.color.team_purple_light
    colorsMap["Minnesota Lynx"] =  R.color.team_blue_grey
    colorsMap["New York Liberty"] =  R.color.team_green_light
    colorsMap["Phoenix Mercury"] =  R.color.team_orange
    colorsMap["Seattle Storm"] =  R.color.team_green
    colorsMap["Washington Mystics"] =  R.color.team_blue_dark

    return colorsMap[key] ?: R.color.purple_200
}

fun backgroundResolverByAbbreviation(key: String) : Int {
    val colorsMap: MutableMap<String, Int> = HashMap()
    colorsMap["ATL"] = R.color.team_red
    colorsMap["BOS"] = R.color.team_green
    colorsMap["BRO"] =  R.color.black
    colorsMap["CHA"] = R.color.team_blue_dark
    colorsMap["CHI"] =  R.color.team_red_dark
    colorsMap["CLE"] = R.color.team_wine
    colorsMap["DAL"] = R.color.team_blue_light
    colorsMap["DEN"] = R.color.team_blue_dark
    colorsMap["DET"] =  R.color.team_red_light
    colorsMap["GOL"] = R.color.team_blue_light
    colorsMap["HOU"] = R.color.team_red_dark
    colorsMap["IND"] = R.color.team_blue_dark
    colorsMap["LAC"] = R.color.team_red
    colorsMap["LAL"] = R.color.team_purple
    colorsMap["MEM"] = R.color.team_blue_grey
    colorsMap["MIA"] = R.color.team_wine_dark
    colorsMap["MIL"] =  R.color.team_green_dark
    colorsMap["MIN"] = R.color.team_blue_dark
    colorsMap["NOR" ] = R.color.team_blue_dark
    colorsMap["NYK" ] = R.color.team_orange
    colorsMap["OKL" ] =  R.color.team_blue
    colorsMap["ORL"] =  R.color.team_blue
    colorsMap["PHI"] = R.color.team_blue_light
    colorsMap["PHO"] = R.color.team_orange_dark
    colorsMap["POR"] =  R.color.team_red
    colorsMap["SAC"] =  R.color.team_purple_light
    colorsMap["SAN" ] = R.color.team_black
    colorsMap["TOR"] = R.color.team_red_light
    colorsMap["UTA" ] = R.color.team_blue_dark
    colorsMap["WAS"] =  R.color.team_blue_dark

    return colorsMap[key] ?: R.color.purple_200
}

fun abbreviationResolverByTeamName(key: String) : String{
    val teamMap: MutableMap<String, String> = HashMap()
    teamMap["Atlanta Hawks"] = "ATL"
    teamMap["Boston Celtics"] = "BOS"
    teamMap["Brooklyn Nets"] =  "BKN"
    teamMap["Charlotte Hornets"] = "CHA"
    teamMap["Chicago Bulls"] = "CHI"
    teamMap["Cleveland Cavaliers"] = "CLE"
    teamMap["Dallas Mavericks"] = "DAL"
    teamMap["Denver Nuggets"] = "DEN"
    teamMap["Detroit Pistons"] =  "DET"
    teamMap["Golden State Warriors"] = "GSW"
    teamMap["Houston Rockets"] = "HOU"
    teamMap["Indiana Pacers"] = "IND"
    teamMap["LA Clippers"] = "LAC"
    teamMap["Los Angeles Lakers"] = "LAL"
    teamMap["Memphis Grizzlies"] = "MEM"
    teamMap["Miami Heat"] = "MIA"
    teamMap["Milwaukee Bucks"] =  "MIL"
    teamMap["Minnesota Timberwolves"] = "MIN"
    teamMap["New Orleans Pelicans"] = "NOP"
    teamMap["New York Knicks"] = "NYK"
    teamMap["Oklahoma City Thunder" ] =  "OKC"
    teamMap["Orlando Magic"] =  "ORL"
    teamMap["Philadelphia 76ers"] = "PHI"
    teamMap["Phoenix Suns"] = "PHX"
    teamMap["Portland Trail Blazers"] =  "POR"
    teamMap["Sacramento Kings"] =  "SAC"
    teamMap["San Antonio Spurs" ] = "SAS"
    teamMap["Toronto Raptors"] = "TOR"
    teamMap["Utah Jazz" ] = "UTA"
    teamMap["Washington Wizards"] =  "WAS"

    // WNBA
    teamMap["Atlanta Dream"] =  "ATLW"
    teamMap["Chicago Sky"] =  "CHIW"
    teamMap["Connecticut Sun"] =  "CONW"
    teamMap["Dallas Wings"] =  "DALW"
    teamMap["Indiana Fever"] =  "INDW"
    teamMap["Las Vegas Aces"] =  "LVAW"
    teamMap["Los Angeles Sparks"] =  "LASW"
    teamMap["Minnesota Lynx"] =  "MINW"
    teamMap["New York Liberty"] =  "NYLW"
    teamMap["Phoenix Mercury"] =  "PHOW"
    teamMap["Seattle Storm"] =  "SEAW"
    teamMap["Washington Mystics"] =  "WASW"

    return teamMap[key] ?: ""
}

fun teamNameResolverByAbbreviation(key: String) : String {
    val abbreviationMap: MutableMap<String, String> = HashMap()
    abbreviationMap["ATL"] = "Atlanta Hawks"
    abbreviationMap["BOS"] = "Boston Celtics"
    abbreviationMap["BKN"] = "Brooklyn Nets"
    abbreviationMap["CHA"] = "Charlotte Hornets"
    abbreviationMap["CHI"] = "Chicago Bulls"
    abbreviationMap["CLE"] = "Cleveland Cavaliers"
    abbreviationMap["DAL"] = "Dallas Mavericks"
    abbreviationMap["DEN"] = "Denver Nuggets"
    abbreviationMap["DET"] =  "Detroit Pistons"
    abbreviationMap["GOL"] = "Golden State Warriors"
    abbreviationMap["GSW"] = "Golden State Warriors"
    abbreviationMap["HOU"] = "Houston Rockets"
    abbreviationMap["IND"] = "Indiana Pacers"
    abbreviationMap["LAC"] = "LA Clippers"
    abbreviationMap["LAL"] = "Los Angeles Lakers"
    abbreviationMap["MEM"] = "Memphis Grizzlies"
    abbreviationMap["MIA"] = "Miami Heat"
    abbreviationMap["MIL"] =  "Milwaukee Bucks"
    abbreviationMap["MIN"] = "Minnesota Timberwolves"
    abbreviationMap["NOP" ] = "New Orleans Pelicans"
    abbreviationMap["NYK" ] = "New York Knicks"
    abbreviationMap["OKC" ] =  "Oklahoma City Thunder"
    abbreviationMap["ORL"] =  "Orlando Magic"
    abbreviationMap["PHI"] = "Philadelphia 76ers"
    abbreviationMap["PHX"] = "Phoenix Suns"
    abbreviationMap["POR"] =  "Portland Trail Blazers"
    abbreviationMap["SAC"] =  "Sacramento Kings"
    abbreviationMap["SAS" ] = "San Antonio Spurs"
    abbreviationMap["TOR"] = "Toronto Raptors"
    abbreviationMap["UTA" ] = "Utah Jazz"
    abbreviationMap["WAS"] =  "Washington Wizards"

    return abbreviationMap[key] ?: ""
}

fun roundingDouble(value: Double): String{
    return BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).toString()
}

fun formattedToday(): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
    return formatter.format(LocalDate.now().atStartOfDay())
}
fun rounding(value: Double): Int {
    var bd = BigDecimal(value.toString())
    bd = bd.setScale(0, RoundingMode.HALF_UP)
    return bd.toInt()
}
