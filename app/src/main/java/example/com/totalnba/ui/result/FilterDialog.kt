package example.com.totalnba.ui.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

enum class FilterType {
    LAST_TEN_GAMES,
    HOME_GAMES,
    AWAY_GAMES,
    HEAD_TO_HEAD,
    ALL_GAMES
}

@Composable
fun FilterDialog(
    currentFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit,
    onDismiss: () -> Unit,
    homeTeamName: String,
    awayTeamName: String
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Filter Options",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val filterOptions = listOf(
                    FilterType.ALL_GAMES to "All Games",
                    FilterType.LAST_TEN_GAMES to "Last 10 Games",
                    FilterType.HOME_GAMES to "Home Games Only",
                    FilterType.AWAY_GAMES to "Away Games Only",
                    FilterType.HEAD_TO_HEAD to "$homeTeamName vs $awayTeamName"
                )

                filterOptions.forEach { (filterType, title) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = currentFilter == filterType,
                                onClick = { onFilterSelected(filterType) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentFilter == filterType,
                            onClick = null
                        )
                        Text(
                            text = title,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onDismiss) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}