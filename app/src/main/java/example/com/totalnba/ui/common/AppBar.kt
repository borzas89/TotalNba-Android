package example.com.totalnba.ui.common

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import example.com.totalnba.R
import example.com.totalnba.ui.theme.AppTheme
import example.com.totalnba.ui.theme.appBarHeader
import example.com.totalnba.ui.theme.color


data class NavigationIcon(
    val tint: Color? = null,
    @StringRes val description: Int = R.string.content_description_back_nav,
    val vector: ImageVector = Icons.Filled.ArrowBack,
    val action: () -> Unit
)
@Composable
fun ColumnScope.AppBar(
    title: String,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    navigationIcon: NavigationIcon? = null,
    actions: @Composable RowScope.() -> Unit = {},
) = AppBar(
    title = { AppBarTitle(title, contentColor) },
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    navigationIcon = navigationIcon,
    actions = actions
)

@Composable
fun ColumnScope.AppBar(
    title: @Composable () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    navigationIcon: NavigationIcon? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = title,
        actions = actions,
        backgroundColor = backgroundColor,
        modifier = Modifier.testTag("AppBar"),
        navigationIcon = if (navigationIcon != null) {
            {
                AppBarNavigation(
                    navigationIcon.tint ?: contentColor,
                    navigationIcon.description,
                    navigationIcon.vector,
                    navigationIcon.action
                )
            }
        } else null
    )
}

@Composable
private fun AppBarTitle(
    title: String,
    titleColor: Color
) {
    Text(
        text = title,
        maxLines = 1,
        style = MaterialTheme.typography.appBarHeader.color(titleColor),
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("AppBar.Title"),
    )
}

@Composable
private fun AppBarNavigation(
    tint: Color,
    description: Int = R.string.content_description_back_nav,
    vector: ImageVector = Icons.Filled.ArrowBack,
    action: () -> Unit
) {
    IconButton(onClick = action) {
        Icon(
            imageVector = vector,
            contentDescription = stringResource(description),
            tint = tint,
            modifier = Modifier.testTag("AppBar.Back")
        )
    }
}

//region preview
data class AppBarPreviewData(
    val title: String,
    val contentColor: Color = Color.Magenta,
    val backgroundColor: Color = Color.Cyan,
    val actions: @Composable RowScope.() -> Unit = {},
)

@Preview(group = "Day", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppBarPreview(@PreviewParameter(AppBarProvider::class) data: AppBarPreviewData) {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppBar(
                title = data.title,
                contentColor = data.contentColor,
                backgroundColor = data.backgroundColor,
                actions = data.actions
            )
        }
    }
}

class AppBarProvider : PreviewParameterProvider<AppBarPreviewData> {
    override val values: Sequence<AppBarPreviewData> = sequenceOf(
        AppBarPreviewData(
            title = "TotalNBA"
        ),
        AppBarPreviewData(
            title = "TotalNBA",
            contentColor = Color.White,
            backgroundColor = Color.Gray,
            actions = {
                TextButton(onClick = {}) {
                    Text(text = "Close", style = MaterialTheme.typography.button)
                }
            }
        ),
        AppBarPreviewData(
            title = "Golden State Warriors vs Oklahoma City Thunder",
            contentColor = Color.Black,
            backgroundColor = Color.White,
            actions = {
                TextButton(onClick = {}) {
                    Text(text = "Close", style = MaterialTheme.typography.button)
                }
            }
        )
    )
}
//endregion