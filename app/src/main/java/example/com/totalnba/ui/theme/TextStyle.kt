package example.com.totalnba.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun TextStyle.color(color: Color) = copy(
    color = color
)

val Typography.appBarHeader: TextStyle
    @Composable get() = MaterialTheme.typography.h6

// highLightAwayWin

val Typography.boldText: TextStyle
    @Composable get() = MaterialTheme.typography.body2.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
