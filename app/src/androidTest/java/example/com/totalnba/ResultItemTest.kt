package example.com.totalnba

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import example.com.totalnba.data.model.Result
import example.com.totalnba.ui.result.ResultItem
import example.com.totalnba.util.DateUtil.asInstant
import org.junit.Rule
import org.junit.Test

class ResultItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun WHEN_component_is_created_THEN_displays_item_properly() {
        val result = Result(
            id = 10,
            matchId = "20e10265-2408-48fd-950f-5709453a1c97",
            matchTime = "2022-10-20T00:00:00Z".asInstant,
            homeName = "Minnesota Timberwolves",
            awayName = "Oklahoma City Thunder",
            homeScore = 115,
            awayScore = 108
        )
        composeRule.setContent {
            ResultItem(
                item = result
            )
        }

        resultItemAwayImage.assertExists()
        resultItemAwayTeamText.assertExists()
        resultItemAwayTeamText.assertTextEquals(result.awayName)
        resultItemAwayScoreText.assertExists()
        resultItemAwayScoreText.assertTextEquals(result.awayScore.toString())

        resultItemHomeImage.assertExists()
        resultItemHomeTeamText.assertExists()
        resultItemHomeTeamText.assertTextEquals(result.homeName)
        resultItemHomeScoreText.assertExists()
        resultItemHomeScoreText.assertTextEquals(result.homeScore.toString())

        resultItemMatchDateText.assertExists()
        resultItemMatchDateText.assertTextEquals(result.formattedMatchDate)

    }

    private val resultItemAwayImage: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.AwayImage", useUnmergedTree = true)

    private val resultItemAwayTeamText: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.AwayName", useUnmergedTree = true)

    private val resultItemAwayScoreText: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.AwayScore", useUnmergedTree = true)

    private val resultItemHomeImage: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.HomeImage", useUnmergedTree = true)

    private val resultItemHomeTeamText: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.HomeName", useUnmergedTree = true)

    private val resultItemHomeScoreText: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.HomeScore", useUnmergedTree = true)

    private val resultItemMatchDateText: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.MatchDate", useUnmergedTree = true)

}