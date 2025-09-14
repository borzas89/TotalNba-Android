package example.com.totalnba.ui.prediction

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.R
import example.com.totalnba.databinding.FragmentPredictionListBinding
import example.com.totalnba.navigator.AppNavigator
import example.com.totalnba.ui.detail.DetailBottomSheetDialogFragment
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PredictionListFragment : Fragment(),
    OnDateSelectedListener {

    private var _binding: FragmentPredictionListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PredictionListViewModel by viewModels()

    lateinit var adapter: PredictionAdapter

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.apply {
            viewModel = this@PredictionListFragment.viewModel
        }

        binding.adapter = PredictionAdapter(listOf(), viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.inflateMenu(R.menu.menu_prediction)

        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_filter -> {
                    setCalendarToday()
                    true
                }
                R.id.action_search -> {
                    navigator.navigateToPlayerSearch()
                    true
                }
                else -> false
            }
        }

        changeStatusBarColor()

        binding.calendarViewSingle.setOnDateChangedListener(this)
        binding.calendarViewSingle.setDateSelected(CalendarDay.today(), true)

        viewModel.showDetail.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { prediction ->
                val id = prediction.commonMatchId
                val awayTeam = prediction.awayTeam?: ""
                val homeTeam = prediction.homeTeam ?: ""
                openDetailDialog(id, homeTeam, awayTeam)
            }
        }

        viewModel.showResult.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { teamData ->
                val parts = teamData.split("|")
                val teamName = parts[0]
                val opponentName = if (parts.size > 1) parts[1] else ""
                navigator.navigateToResults(teamName, opponentName)
            }
        }
    }

    private fun openDetailDialog(
        id: String,
        homeName: String,
        awayName: String
    ) {
        DetailBottomSheetDialogFragment.newInstance(id, homeName, awayName)
            .show(this.requireFragmentManager(), "DetailBottomSheetDialog")
    }

    @SuppressLint("ResourceAsColor")
    private fun changeStatusBarColor(){
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = R.color.team_blue
    }

    private fun setCalendarToday() {
        binding.calendarViewSingle.selectedDate = CalendarDay.today()
        binding.calendarViewSingle.currentDate = CalendarDay.today()
        binding.calendarViewSingle.setDateSelected(CalendarDay.today(), true)
        viewModel.filterToday()
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        val format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
        val formatter = format1.format(date.date.atStartOfDay())
        viewModel.filterDay.onNext(formatter)
    }
}