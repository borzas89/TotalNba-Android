package example.com.totalnba.ui.result

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jakewharton.threetenabp.AndroidThreeTen
import example.com.totalnba.data.model.Result
import example.com.totalnba.ui.theme.AppTheme
import example.com.totalnba.ui.theme.boldText
import example.com.totalnba.ui.theme.commonBackground
import example.com.totalnba.util.DateUtil.asInstant
import example.com.totalnba.util.imageResolverId

@Composable
fun ResultItem(
    item: Result
) = Column {
    Row {
        Column(
            modifier = Modifier.weight(1f)
                .background(MaterialTheme.colors.commonBackground)
        ) {
            Row(
            ){
                Image(
                    painterResource(id = imageResolverId(item.awayName)),
                    modifier = Modifier.testTag("ResultItem.AwayImage")
                        .size(60.dp,60.dp),
                    contentDescription = ""
                )
                Text(
                    text = item.awayScore.toString(),
                    style = if(item.highLightAwayWin) MaterialTheme.typography.boldText else MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("ResultItem.AwayScore")
                        .padding(start = 12.dp,
                            top = 12.dp)

                )
                Text(
                    text = item.awayName,
                    style = if(item.highLightAwayWin) MaterialTheme.typography.boldText else MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("ResultItem.AwayName")
                        .padding(start = 12.dp,
                            top = 12.dp)
                )

            }

            Row(
            ){
                Image(
                    painterResource(id = imageResolverId(item.homeName)),
                    modifier = Modifier.testTag("ResultItem.HomeImage")
                        .size(60.dp,60.dp),
                    contentDescription = ""
                )
                Text(
                    text = item.homeScore.toString(),
                    style = if(item.highLightAwayWin) MaterialTheme.typography.body2 else MaterialTheme.typography.boldText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("ResultItem.HomeScore")
                        .padding(start = 12.dp,
                        top = 12.dp)
                )
                Text(
                    text = item.homeName,
                    style = if(item.highLightAwayWin) MaterialTheme.typography.body2 else MaterialTheme.typography.boldText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("ResultItem.HomeName")
                        .padding(start = 12.dp,
                            top = 12.dp)
                )
            }
        }

        Column(
        ){
            Text(
                text = item.formattedMatchDate,
                style = MaterialTheme.typography.boldText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("ResultItem.MatchDate")
                    .padding(end = 12.dp,
                        top = 12.dp)
            )
        }
    }
}

// region preview
data class ResultPreviewData(
    val result: Result
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Day", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Night", showBackground = true)
@Composable
fun PreviousEvaluationItemPreview(@PreviewParameter(EvaluationPreviewProvider::class) data: ResultPreviewData) {
    AndroidThreeTen.init(LocalContext.current)
    AppTheme {
        ResultItem(
            item = data.result
        )
    }
}

class EvaluationPreviewProvider :
    PreviewParameterProvider<ResultPreviewData> {
    override val values: Sequence<ResultPreviewData> = sequenceOf(
        ResultPreviewData(
            result = Result(
                id = 10,
                matchId = "20e10265-2408-48fd-950f-5709453a1c97",
                matchTime = "2022-10-20T00:00:00Z".asInstant,
                homeName = "Minnesota Timberwolves",
                awayName = "Oklahoma City Thunder",
                homeScore = 115,
                awayScore = 108
            )
        )
    )
}
//endregion preview

