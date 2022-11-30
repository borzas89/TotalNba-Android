package example.com.totalnba.data.database

import androidx.room.TypeConverter
import org.threeten.bp.Instant

class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): Instant? {
        return if (value == null) null else Instant.parse(value)
    }

    @TypeConverter
    fun instantToTimestamp(date: Instant?): String? {
        return date?.toString()
    }
}