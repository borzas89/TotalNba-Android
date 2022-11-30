package example.com.totalnba

import example.com.totalnba.data.network.model.ResultDto
import example.com.totalnba.testdata.ResultTestData
import org.junit.Test
import kotlin.test.assertEquals

class ResultDtoTest {

    @Test
    fun GIVEN_initialized_result_dto_WHEN_convert_to_model_THEN_converted_correctly() {
        val resultDto = ResultDto(
            id = 1,
            matchId = "20e10265-2408-48fd-950f-5709453a1c97",
            matchTime = "2022-10-20T00:00:00Z",
            homeName = "Minnesota Timberwolves",
            awayName = "Oklahoma City Thunder",
            homeScore = 115,
            awayScore = 108
        )
        val resultModel = ResultTestData.default

        assertEquals(
            resultDto.map(),
            resultModel
        )
    }
}