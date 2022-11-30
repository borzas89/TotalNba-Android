package example.com.totalnba.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import example.com.totalnba.R

val Colors.primary: Color
    @Composable
    get() = colorResource(R.color.colorPrimary)

val Colors.commonBackground: Color
    @Composable
    get() = colorResource(R.color.grey)

val Colors.commonFont: Color
    @Composable
    get() = colorResource(R.color.white)

val Colors.divider
    @Composable
    get() = colorResource(R.color.divider)

// Team colors
val Colors.teamRed: Color
    @Composable
    get() = colorResource(R.color.team_red)

val Colors.teamRedLight: Color
    @Composable
    get() = colorResource(R.color.team_red_light)

val Colors.teamRedDark: Color
    @Composable
    get() = colorResource(R.color.team_red_dark)

val Colors.teamGreen: Color
    @Composable
    get() = colorResource(R.color.team_green)

val Colors.teamGreenLight: Color
    @Composable
    get() = colorResource(R.color.team_green_light)

val Colors.teamGreenDark: Color
    @Composable
    get() = colorResource(R.color.team_green_dark)

val Colors.teamPurple: Color
    @Composable
    get() = colorResource(R.color.team_purple)