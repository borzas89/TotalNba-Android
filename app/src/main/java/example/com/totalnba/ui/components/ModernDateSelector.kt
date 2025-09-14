package example.com.totalnba.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

data class DateItem(
    val date: LocalDate,
    val day: String,
    val isSelected: Boolean
)

@Composable
fun ModernDateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateItems = remember(selectedDate) {
        generateDateItems(selectedDate)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Month and Year
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            }

            // Date buttons
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(dateItems) { dateItem ->
                    DateButton(
                        dateItem = dateItem,
                        onSelected = { onDateSelected(dateItem.date) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateButton(
    dateItem: DateItem,
    onSelected: () -> Unit
) {
    val backgroundColor = if (dateItem.isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.surface
    }

    val contentColor = if (dateItem.isSelected) {
        MaterialTheme.colors.onPrimary
    } else {
        MaterialTheme.colors.onSurface
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(48.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .clickable { onSelected() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = dateItem.day,
            fontSize = 16.sp,
            fontWeight = if (dateItem.isSelected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor
        )
        Text(
            text = dateItem.date.dayOfMonth.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = contentColor.copy(alpha = 0.8f)
        )
    }
}

private fun generateDateItems(selectedDate: LocalDate): List<DateItem> {
    val items = mutableListOf<DateItem>()
    val startDate = selectedDate.minusDays(4)

    for (i in 0..8) {
        val date = startDate.plusDays(i.toLong())
        val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase()

        items.add(
            DateItem(
                date = date,
                day = dayName,
                isSelected = date.isEqual(selectedDate)
            )
        )
    }

    return items
}

@Preview
@Composable
fun ModernDateSelectorPreview() {
    MaterialTheme {
        ModernDateSelector(
            selectedDate = LocalDate.now(),
            onDateSelected = { }
        )
    }
}