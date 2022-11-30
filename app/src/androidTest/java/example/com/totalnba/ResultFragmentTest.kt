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

class ResultFragmentTest {

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

    }



    private val resultItemAwayImage: SemanticsNodeInteraction
        get() = composeRule.onNodeWithTag("ResultItem.AwayImage", useUnmergedTree = true)
}