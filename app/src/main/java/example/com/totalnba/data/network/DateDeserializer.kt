package example.com.totalnba.data.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.Date

/**
 * Custom Gson deserializer that handles both Unix timestamps (milliseconds) and ISO date strings
 */
class DateDeserializer : JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date {
        return try {
            // Try parsing as Unix timestamp (number)
            if (json.isJsonPrimitive && json.asJsonPrimitive.isNumber) {
                Date(json.asLong)
            } else {
                // Try parsing as ISO date string
                val dateString = json.asString
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString)
                    ?: throw JsonParseException("Unable to parse date: $dateString")
            }
        } catch (e: Exception) {
            throw JsonParseException("Failed to parse date: ${json.asString}", e)
        }
    }
}
