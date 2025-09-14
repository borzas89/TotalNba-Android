package example.com.totalnba.ui.result

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.R
import example.com.totalnba.data.model.Adjustment
import example.com.totalnba.data.model.Result
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.ui.common.AppBar
import example.com.totalnba.ui.common.NavigationIcon
import example.com.totalnba.ui.preview.ResultSample
import example.com.totalnba.ui.theme.*
import javax.inject.Inject

@AndroidEntryPoint
class ResultFragment : Fragment() {
    private val viewModel: ResultViewModel by viewModels()

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent(content)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateBack.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                navigator.popBackStack()
            }
        }
    }

    val content: @Composable () -> Unit = {
        val teamColor by viewModel.teamColorState
        val teamImage by viewModel.teamImageState
        val teamName by viewModel.teamNameState
        val opponentName by viewModel.opponentNameState
        val adjustment by viewModel.adjustment
        val results by viewModel.teamResultListState
        val showDialog by viewModel.showFilterDialog
        val currentFilter by viewModel.currentFilter

        MaterialTheme {
            Screen(
                uiActions = viewModel,
                teamColor = teamColor,
                teamImage = teamImage,
                teamName = teamName,
                opponentName = opponentName,
                adjustment = adjustment,
                elements = results,
                showFilterDialog = showDialog,
                currentFilter = currentFilter,
                onFilterSelected = viewModel::onFilterSelected,
                onDismissDialog = viewModel::hideFilterDialog
            )
        }
    }

    @Composable
    fun Screen(
        uiActions: ResultViewModelUiActions,
        teamColor: Int,
        teamImage: Int,
        teamName: String,
        opponentName: String,
        adjustment: Adjustment?,
        elements: List<Result>,
        showFilterDialog: Boolean,
        currentFilter: FilterType,
        onFilterSelected: (FilterType) -> Unit,
        onDismissDialog: () -> Unit
    ) {
        setStatusBarColor(colorResource(id = teamColor))
        Column(modifier = Modifier.fillMaxSize()) {
            AppBar(
                title = "",
                contentColor = Color.White,
                backgroundColor = colorResource(id = teamColor),
                navigationIcon = NavigationIcon(
                    tint = Color.White,
                    description = R.string.content_description_back_nav,
                    vector = Icons.Filled.ArrowBack,
                    action = uiActions.navigateUp
                ),
                actions = {
                    IconButton(onClick = uiActions.onShowFilterDialog) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = teamColor)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    painterResource(id = teamImage),
                    modifier = Modifier.testTag("Result.TeamImage"),
                    contentDescription = ""
                )

            }
            Column(
                Modifier
                    .background(colorResource(id = teamColor))
                    .fillMaxWidth()
            ) {
                Text(
                    text = teamName,
                    style = MaterialTheme.typography.subtitle1.color(Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .testTag("Result.TeamName")
                        .padding(start = 12.dp)
                )
                Text(
                    text = stringResource(
                        id = R.string.wins_and_losses_count_title,
                        adjustment?.wins ?: 0,
                        adjustment?.losses?:0),
                    style = MaterialTheme.typography.subtitle1.color(Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .testTag("Result.WinAndLose")
                        .padding(start = 12.dp, bottom = 12.dp)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.commonBackground)
            ) {
                items(elements) { result ->
                    ResultItem(item = result)
                    Divider(
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colors.divider,
                        thickness = (dimensionResource(id = R.dimen.divider_size))
                    )
                }
            }
        }

        // Filter Dialog
        if (showFilterDialog) {
            FilterDialog(
                currentFilter = currentFilter,
                onFilterSelected = onFilterSelected,
                onDismiss = onDismissDialog,
                homeTeamName = teamName,
                awayTeamName = opponentName.ifEmpty { "Opponent" }
            )
        }
    }


//region preview
    private val previewUiActions = object : ResultViewModelUiActions {
        override val navigateUp: () -> Unit = {}
        override val onShowFilterDialog: () -> Unit = {}
    }

    data class ResultScreenPreviewData(
        val teamColor: Int,
        val teamImage: Int,
        val teamName: String,
        val opponentName: String,
        val adjustment: Adjustment,
        val results: List<Result>,
    )

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light Mode", showSystemUi = false)
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark Mode", showSystemUi = false)
    @Composable
    fun ResultFragmentPreview(@PreviewParameter(ResultFragmentProvider::class) data: ResultScreenPreviewData) {
        AndroidThreeTen.init(LocalContext.current)
        AppTheme {
            Screen(
                uiActions = previewUiActions,
                teamColor = data.teamColor,
                teamImage = data.teamImage,
                teamName = data.teamName,
                opponentName = data.opponentName,
                adjustment = data.adjustment,
                elements = data.results,
                showFilterDialog = false,
                currentFilter = FilterType.ALL_GAMES,
                onFilterSelected = {},
                onDismissDialog = {}
            )
        }
    }
}

class ResultFragmentProvider : PreviewParameterProvider<ResultFragment.ResultScreenPreviewData> {
    override val values: Sequence<ResultFragment.ResultScreenPreviewData> = sequenceOf(
        ResultFragment.ResultScreenPreviewData(
            teamColor = R.color.team_blue,
            teamImage = R.drawable.gsw,
            teamName = "Golden State Warriors",
            opponentName = "Los Angeles Lakers",
            adjustment = Adjustment(1, "GSW","GSW",
            0.0,0.0,11,2,
                0.5, 0.5, 0.5, 0.5),
            results = ResultSample.results
        )
    )
}
//endregion preview