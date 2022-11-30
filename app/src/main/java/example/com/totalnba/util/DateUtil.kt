package example.com.totalnba.util

import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeParseException
import java.util.*

object DateUtil {

    val String.asInstant: Instant
        get() = try {
            Instant.parse(this)
        } catch (_: DateTimeParseException) {
            org.threeten.bp.LocalDate.parse(this)
                .atStartOfDay().toInstant(ZoneOffset.UTC)
        }

    private val monthAndDayFormat: DateTimeFormatter
        get() = DateTimeFormatter
            .ofPattern("MM.dd.")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

    val Instant.stringWithMonthAndDay: String get() =
        monthAndDayFormat.format(this)


}